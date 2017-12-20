package com.cc.task.helperx.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cc.task.helperx.MyApplication;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.recver.TimerSchemer;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.EventBusUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.TimeManager;
import com.cc.task.helperx.utils.Utils;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class TaskControllerService extends Service {

    private Handler handler = new Handler();
    private TaskEntry taskEntry;
    private List<String> tasks;

    private int index = 0;
    private TimeManager timer;
    protected String tel_sign = "";
    long TASK_STEP_TIME = 60 * 1000L;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.logInfo("任务控制服务创建");
        EventBusUtils.getInstance().register(this);
        if (TextUtils.isEmpty(tel_sign)){
            getModel();
        }
        if (MyApplication.debugMode) {
            //调试模式
            requestTask();
        } else {
            registerTimer();
            startTask();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private void startTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.logInfo("获取任务");
                requestTask();
            }
        }, TASK_STEP_TIME);
    }


    private void requestTask() {
        if (!TextUtils.isEmpty(tel_sign)) {
            //http://qq.down50.com/weixinoutput/wxapi.php?t=gettasktelsign&str1=8305c35245306da9dda55b2c4d03a1ab
            HttpHandler.requestTask(tel_sign, new HttpTask.HttpCallback() {
                @Override
                public void onSuccess(String result) {

                    LogUtils.logInfo("任务：" + result);
                    if (!TextUtils.isEmpty(result)) {
                        taskEntry = Utils.getGson().fromJson(result, TaskEntry.class);
                    }
                }

                @Override
                public void onFailure(String errMsg) {
                    LogUtils.logInfo("任务请求失败 ：" + errMsg);
                    startNextTask();
                }

                @Override
                public void onFinished() {
                    if (taskEntry != null && taskEntry.getTel_task_status() == 1 && !TextUtils.isEmpty(taskEntry.getTask_id())) {
                        LogUtils.logInfo(taskEntry.toString());
                        Utils.saveObject(taskEntry, TaskEntry.class.getSimpleName());
                        String[] taskArray = taskEntry.getDo_task().split(",");
                        tasks = Arrays.asList(taskArray);
                        FileUtils.writeFileToSDCard("[开始任务"+ DateUtils.format(System.currentTimeMillis(),DateUtils.DEFAULT_DATE_FORMAT),"RunTime","runTimeLog",true,true);
                        taskRun(false);
                    } else {
                        startNextTask();
                    }
                }
            });
        } else {
            // tel_sign 为空
            LogUtils.logError("tel_sign等于空!");
            startNextTask();
        }
    }

    @Subscriber( tag = "taskRun" )
    public void taskRun(boolean isStop) {
        Utils.saveObject(0, Constants.TASK_EXECUTE_TIME);
        Utils.removeObject(Constants.KEY_CHECK_SEND);
        if (tasks != null && !tasks.isEmpty() && index < tasks.size() && !isStop) {
            String type = tasks.get(index);
            index += 1;
            EventBusUtils.getInstance().sendMessage("executeTask", type);
        } else {
            if (taskEntry != null && !TextUtils.isEmpty(taskEntry.getTask_id())) {
                reportRresult(taskEntry.getTask_id());
            } else {
                redirectNextTask();
            }
        }
    }

//    public void taskRun(MessageEvent.RunMsg msg) {
//        Utils.saveObject(taskEntry, TaskEntry.class.getSimpleName());
////        Utils.saveObject(0, Constants.TASK_EXECUTE_TIME);//创建执行任务时间
//        Utils.sleep(2000);
//        LogUtils.logInfo("!msg.isRun=" + !msg.isRun);
//        if (tasks != null && !tasks.isEmpty() && index < tasks.size() && !msg.isRun) {
//            String type = tasks.get(index);
//            LogUtils.logInfo(" task type=" + type);
//            EventBus.getDefault().post(new MessageEvent.TaskMsg(type));
//            index += 1;
//        } else {
//            Utils.sleep(2000);
//            LogUtils.logInfo(" taskEntry =" + taskEntry.toString() + "   ,  " + taskEntry.getTask_id());
//            if (taskEntry != null && !TextUtils.isEmpty(taskEntry.getTask_id())) {
//                //isRun = true  时进入 ->执行下一个任务
//                reportRresult(taskEntry.getTask_id());
//            } else {
//                //没有任务了，清理数据，开始计时
//                redirectTask();
//            }
//        }
//    }

    /**
     * 上报任务完成
     *
     * @param task_id
     */
    private void reportRresult(String task_id) {
        //任务上报完成  开始计时后 重新计时
        HttpHandler.requestEndTask(task_id, new HttpTask.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.optInt("code");
                    if (code == 200) {
                        FileUtils.writeFileToSDCard("结束任务"+ DateUtils.format(System.currentTimeMillis(),DateUtils.DEFAULT_DATE_FORMAT)+"]","RunTime","runTimeLog",true,true);
                        LogUtils.logInfo("任务流程全部执行成功.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errMsg) {

            }

            @Override
            public void onFinished() {
                taskEntry.setDo_task("");
//                Utils.saveObject(taskEntry, TaskEntry.class.getSimpleName());
                redirectNextTask();
            }
        });
    }


    //进入间隔时间
    private void startNextTask() {
        index = 0;
        tasks = null;
        taskEntry = null;
        Utils.saveObject(0, Constants.TASK_EXECUTE_TIME);
        startTask();
    }

    /**
     * 切换到下一批任务
     */
    private void redirectNextTask() {
        index = 0;
        tasks = null;
        taskEntry = null;
        //   Utils.saveObject(0, Constants.TASK_EXECUTE_TIME);
        //   Utils.removeObject(TaskEntry.class.getSimpleName());
        requestTask();
    }

    private void registerTimer() {
        timer = new TimeManager();
        TimerSchemer recver = new TimerSchemer(this);//默认一个任务时间为20分钟
        timer.schedule(recver, 0, 60 * 1000L); //两分钟检测一次任务是否过期
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
        EventBusUtils.getInstance().unRegister(this);
        LogUtils.logInfo("任务控制服务停止");
    }

    public void unRegister() {
        if (timer != null) {
            timer.cnacelAll();
        }
    }

    public void getModel() {
        String typephone = Utils.getSystemModel();
        switch (typephone) {
            case Constants.HONOR_PHONE_MODEL:
                tel_sign = FileUtils.readStringToFile(Constants.MAC_ID_FILE);
                break;
            case Constants.RAMOS_PHONE_MODEL:
                tel_sign = FileUtils.readStringToFile(Constants.MAC_ID_FILE_R);
                break;
        }
    }
}


