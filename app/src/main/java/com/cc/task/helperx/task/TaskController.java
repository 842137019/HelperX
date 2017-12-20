package com.cc.task.helperx.task;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cc.task.helperx.entity.GroupInfo;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.EventBusUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.QQError;
import com.cc.task.helperx.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fangying on 2017/8/30.
 */

public class TaskController {

    private HelperQQService service;
    private AddGroupTask addGroupTask;
    private AddPeopleTask addPeopleTask;
    private ModifyInfoTask modifyInfoTask;
    private ReductionTask reductionTask;
    private BackupsTask backupsTask;
    private ReplyMessageTask messageTask;
    private MassMessageTask massMessageTask;
    private CloseMsgNoticeTask noticeTask;
    private ClearChatRecordTask clearChatRecordTask;
    private GroupMembersTask groupMembersTask;
    private CreateMultiplayerChatTask createMultiplayerChatTask;
    private CloseFunctionTask closeFunctionTask;
    private DleSQLiteTask dleSQLiteTask;
    private ClosePhoneNotificationTask phoneNotificationTask;
    private RegisterTask registerTask;
    private GroupMembersSendMessagesTask sendMessagesTask;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //TODO 执行下个分任务
                    LogUtils.logInfo("执行下个分任务");
                    EventBusUtils.getInstance().sendMessage("taskRun", new Boolean(false));
                    break;
                case 2:
                    //TODO 账号错误，执行下一个任务
                    EventBusUtils.getInstance().sendMessage("taskRun", new Boolean(true));
                    break;
            }
        }
    };

    public TaskController(HelperQQService service) {
        this.service = service;
    }

    public void executeTask(String type) {
        if (TextUtils.isEmpty(type)) {
            handler.sendEmptyMessage(2);
            return;
        }
        Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
        if (obj != null) {
            TaskEntry taskEntry = (TaskEntry) obj;
            if (type.equals("5")) {
                assignments(Integer.valueOf(type), taskEntry);
            } else {
                if (QQError.validate(service, taskEntry)) {
                    LogUtils.i(" 选择执行任务 " + type);
                    QQError.loginPromptAcitvity(service);
                    assignments(Integer.valueOf(type), taskEntry);
                } else {
                    if (Utils.wxIsRun()) {
                        Utils.exitApp(Constants.APP_NAME);
                        Utils.sleep(5000L);
                    }
                    handler.sendEmptyMessage(2);
                }
            }
        } else {
            handler.sendEmptyMessage(2);
        }
    }

    private void assignments(int type, TaskEntry taskEntry) {
//        Utils.scrollScreenXY("340 417 325 692");
        switch (type) {
            case Constants.TYPE_MODIFYINFO_TASK:
                LogUtils.logInfo("修改个人信息");
                FileUtils.writeFileToSDCard("\t \t [修改个人信息：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (modifyInfoTask == null) {
                    modifyInfoTask = new ModifyInfoTask(service, handler);
                }
                modifyInfoTask.modifyInfo(taskEntry);
                break;

            case Constants.TYPE_BROWS_ENEWS_TASK:
                LogUtils.logInfo("浏览新闻");
                BrowseNewsTask newsTask = new BrowseNewsTask(service,handler);
                newsTask.browseNews();
                break;

            case Constants.TYPE_ADD_PEOPLE_TASK:
                LogUtils.logInfo(" 添加好友 ");
                FileUtils.writeFileToSDCard("\t \t [添加好友：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (addPeopleTask == null) {
                    addPeopleTask = new AddPeopleTask(service, handler);
                }
                addPeopleTask.addContacts(taskEntry);
                break;

            case Constants.TYPE_ADD_GROUP_TASK:
                LogUtils.logInfo(" 添加群組 ");
                FileUtils.writeFileToSDCard("\t \t [添加群組：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (addGroupTask == null) {
                    addGroupTask = new AddGroupTask(service, handler);
                }
                addGroupTask.addGroup(taskEntry, 0);
                break;

            case Constants.TYPE_REDUCTION_TASK:
                LogUtils.logInfo("还原文件");
                FileUtils.writeFileToSDCard("\t \t [还原文件：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (reductionTask == null) {
                    reductionTask = new ReductionTask(service, handler);
                }
                reductionTask.ReductionData(taskEntry);
                break;

            case Constants.TYPE_BACKUPS_TASK:
                LogUtils.logInfo("备份文件");
                FileUtils.writeFileToSDCard("\t \t [备份文件：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (backupsTask == null) {
                    backupsTask = new BackupsTask(service, handler);
                }
                backupsTask.backups(taskEntry);
                break;

//            case Constants.TYPE_REPLY_MESSAGE_TASK:
//                LogUtils.logInfo("群发消息");
//                TestMassMsgTask testMassMsgTask = new TestMassMsgTask(service, handler);
//                testMassMsgTask.getSendMassMsgDB(taskEntry);
//                break;

            case Constants.TYPE_MASSMSG_TASK:
                LogUtils.logInfo("在QQ群内发消息");
                FileUtils.writeFileToSDCard("\t \t [在QQ群内发消息：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (massMessageTask == null) {
                    massMessageTask = new MassMessageTask(service, handler);
                }
                massMessageTask.sendTypeMsg(taskEntry);
                break;

            case Constants.TYPE_CLOSE_NOTICE_TASK:
                LogUtils.logInfo("关闭消息通知");
                FileUtils.writeFileToSDCard("\t \t [关闭消息通知：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (noticeTask == null) {
                    noticeTask = new CloseMsgNoticeTask(service, handler);
                }
                noticeTask.closeNoticeGetInfo();
                break;

            case Constants.TYPE_CLEAR_CHATRECORD_TASK:
                LogUtils.logInfo("清空聊天记录");
                FileUtils.writeFileToSDCard("\t \t [清空聊天记录：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (clearChatRecordTask == null) {
                    clearChatRecordTask = new ClearChatRecordTask(service, handler);
                }
                clearChatRecordTask.emptyRecords();
                break;

            case Constants.TYPE_GROUP_MEMBERS_MSG_TASK:
                LogUtils.logInfo("执行任务 32--群成员聊天");

                FileUtils.writeFileToSDCard("\t \t [与群成员聊天：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                List<GroupInfo> groupInfoList = DataSupport.where(" wxsign = ? and groupType = ? ", taskEntry.getWx_sign(), "success").find(GroupInfo.class);
                List<String> groupList = new ArrayList<>();
//                int index = 0;
                for (int i = 0; i < groupInfoList.size(); i++) {
                    if ((!TextUtils.isEmpty(groupInfoList.get(i).getMember_sentNum())) && (!TextUtils.isEmpty(groupInfoList.get(i).getMembersNum()))) {
                        int sent = Integer.parseInt(groupInfoList.get(i).getMember_sentNum());
                        int memnum = Integer.parseInt(groupInfoList.get(i).getMembersNum());
//                        index = 1;
                        if (sent >= memnum) {
                            groupList.add(groupInfoList.get(i).getGroupId());
                        }
                    }
                }
                LogUtils.logInfo( "  groupInfoList  size = " + groupInfoList.size() + "  groupList size = " + groupList.size());
//                if (index != 0) {

                if ((groupInfoList.size() < 3) || ((groupInfoList.size() - groupList.size()) < 3)) {
                    LogUtils.i("先加群后执行聊天");
                    if (addPeopleTask == null) {
                        addGroupTask = new AddGroupTask(service, handler);
                    }
                    addGroupTask.addGroup(taskEntry, 1);
                } else {
                    LogUtils.logInfo("执行与成员聊天");
                    if (sendMessagesTask == null) {
                        sendMessagesTask = new GroupMembersSendMessagesTask(service, handler);
                    }
                    sendMessagesTask.sendTypeMsg(taskEntry);
                }
//                } else {
//                    LogUtils.logInfo("执行与成员聊天");
//                    if (sendMessagesTask == null) {
//                        sendMessagesTask = new GroupMembersSendMessagesTask(service, handler);
//                    }
//                    sendMessagesTask.sendTypeMsg(taskEntry);
//                }
                break;

            case 33:
                LogUtils.logInfo("执行任务:33--获取群信息");
                FileUtils.writeFileToSDCard("\t \t [点击群列表：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);

                if (registerTask == null) {
                    registerTask = new RegisterTask(service, handler);
                }
                registerTask.findALocalGroup(taskEntry);
                break;

            case 34:
                LogUtils.logInfo("关闭通知");
                FileUtils.writeFileToSDCard("\t \t [关闭通知：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);

                if (phoneNotificationTask == null) {
                    phoneNotificationTask = new ClosePhoneNotificationTask(service, handler);
                }
                phoneNotificationTask.setqNotifcation();
                break;

            case 36:
                LogUtils.logInfo("关闭功能");
                FileUtils.writeFileToSDCard("\t \t [关闭功能：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (closeFunctionTask == null) {
                    closeFunctionTask = new CloseFunctionTask(service, handler);
                }
                closeFunctionTask.closeFunction();
                break;

            case 35:
                LogUtils.logInfo("创建讨论组");
                FileUtils.writeFileToSDCard("\t \t [创建讨论组：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (createMultiplayerChatTask == null) {
                    createMultiplayerChatTask = new CreateMultiplayerChatTask(service, handler);
                }
                createMultiplayerChatTask.CreateChat(taskEntry);
                break;

            case 37:
                LogUtils.logInfo("执行任务:37--删除数据库中 表 ");
                FileUtils.writeFileToSDCard("\t \t [删除数据库中 表：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT), "RunTime", "runTimeLog", true, true);
                if (dleSQLiteTask == null) {
                    dleSQLiteTask = new DleSQLiteTask(handler);
                }
                dleSQLiteTask.deleteSQLite(taskEntry.getWx_sign());
                break;

            case 40:
                LogUtils.logInfo("执行任务:" + type + "--退出群组 ");
                ExitingGroupTask exitingGroupTask = new ExitingGroupTask(service, handler);
                exitingGroupTask.exitingGroup();
                break;
        }
    }
}
