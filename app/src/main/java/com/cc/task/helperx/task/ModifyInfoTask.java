package com.cc.task.helperx.task;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.cc.task.helperx.entity.NewsEntry;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.entity.UserInfoEntity;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import java.io.File;


/**
 * 修改個人信息
 * Created by fangying on 2017/9/4.
 */

public class ModifyInfoTask {

    private HelperQQService service;
    private Handler handler;

    public ModifyInfoTask(HelperQQService service, Handler handler) {
        this.handler = handler;
        this.service = service;
    }

    public void modifyInfo(final TaskEntry taskEntry) {
        //http://qq.down50.com/weixinoutput/wxapi.php?t=getinfosign&str1=12933fe2538a22ba8bd90dd3f4afcc8a&str2=1
        HttpHandler.requestModifyPersonal(taskEntry.getWx_sign(), new HttpTask.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.logInfo("个人信息=" + data);
                if (!TextUtils.isEmpty(data)) {
                    UserInfoEntity infoEntity = Utils.getGson().fromJson(data, UserInfoEntity.class);
                    modifyInfo(infoEntity);
                } else {
                    HttpHandler.reportError(taskEntry.getWx_id(), "个人信息数据为空!");
                    FileUtils.writeFileToSDCard("\t \t 个人信息数据为空：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(String errMsg) {
                LogUtils.logError("个人信息数据请求失败!!   " + errMsg);
                HttpHandler.reportError(taskEntry.getWx_sign(), "个人信息数据请求失败!");
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void modifyInfo(UserInfoEntity infoEntity) {
        if (!Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)) {
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        AccessibilityNodeInfo nodeInfo = Utils.findViewByDesc(service, "帐户及设置");
        if (nodeInfo == null) {
            FileUtils.writeFileToSDCard("\t \t 帐户及设置未找到执行下一个任务：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(nodeInfo);
        Utils.sleep(2000);
        Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/head_layout"));
        Utils.sleep(2000);

        Utils.clickCompone(Utils.findViewByText(service, "编辑资料"));
        Utils.sleep(2000);

        ModifyAutograph(infoEntity);

    }

    /***
     * 设置生日
     *
     */
    private void modifyBirthday(UserInfoEntity infoEntity) {
        String bthday = infoEntity.getBirthday();
        LogUtils.logInfo("  设置生日  " + bthday);
        if (!TextUtils.isEmpty(bthday)) {
            AccessibilityNodeInfo birthday = Utils.findViewByText(service, "生日");
            if (birthday != null) {
                String birthdayText = birthday.getParent().getChild(5).getChild(1).getText() + "";
                LogUtils.i(" birthdayText = " + birthdayText + "  " + birthdayText.equals(bthday));
                if (!birthdayText.equals(bthday)) {
                    if (birthdayText.equals("你的生日")) {
                        birthdayText = "1990-1-1";
                    }
                    Utils.clickCompone(birthday);
                    String[] birthes = birthdayText.split("-");
                    String[] newBirthes = bthday.split("-");

                    int newYear = Integer.parseInt(newBirthes[0]);
                    int newMonth = Integer.parseInt(newBirthes[1]);
                    int newDay = Integer.parseInt(newBirthes[2]);
                    int oldYear = Integer.parseInt(birthes[0]);
                    int oldMonth = Integer.parseInt(birthes[1]);
                    int oldDay = Integer.parseInt(birthes[2]);
                    //month     day
                    LogUtils.logError("oldYear =" + Integer.parseInt(birthes[0]));
//                    AccessibilityNodeInfo nodeInfo = Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView");
//                    if (nodeInfo != null) {
                    String model = Build.MODEL;
                    LogUtils.i(" 手机型号 " + model);
                    if (model.contains("SCL")) {
                        settingDateHonor(infoEntity, newYear, newMonth, newDay, oldYear, oldMonth, oldDay);
                    } else if (model.contains("MOS")) {
                        settingDateRamos(infoEntity, newYear, newMonth, newDay, oldYear, oldMonth, oldDay);
                    }
//                    }
                } else {
                    LogUtils.i("生日已设置");
                    downloadPicture(infoEntity.getHead_url());
                }
            } else {
                LogUtils.i("生日已设置");
                downloadPicture(infoEntity.getHead_url());
            }
        } else {
            LogUtils.i("未找到生日");
            downloadPicture(infoEntity.getHead_url());
        }
    }

    /***
     *
     * 设置年月日  （华为）
     * @param newYear
     * @param newMonth
     * @param newDay
     * @param oldYear
     * @param oldMonth
     * @param oldDay
     */
    private void settingDateHonor(UserInfoEntity infoEntity, int newYear, int newMonth, int newDay, int oldYear, int oldMonth, int oldDay) {
        LogUtils.i("华为");
        if (newYear > oldYear) {
            for (int i = 0; i < newYear - oldYear; i++) {
                Utils.tapScreenXY("138 1073");
                Utils.sleep(1500);
            }
        } else if (newYear < oldYear) {
            for (int i = 0; i < oldYear - newYear; i++) {
                Utils.tapScreenXY("134 944");
                Utils.sleep(1500);
            }
        }

        if (newMonth > oldMonth) {
            for (int i = 0; i < newMonth - oldMonth; i++) {
                Utils.tapScreenXY("390 1081");
                Utils.sleep(1500);
            }
        } else if (oldMonth > newMonth) {
            for (int i = 0; i < oldMonth - newMonth; i++) {
                Utils.tapScreenXY("396 961");
                Utils.sleep(1500);
            }
        }

        if (newDay > oldDay) {
            for (int i = 0; i < newDay - oldDay; i++) {
                Utils.tapScreenXY("605 1081");
                Utils.sleep(1500);
            }
        } else if (oldDay > newDay) {
            for (int i = 0; i < oldDay - newDay; i++) {
                Utils.tapScreenXY("617 964");
                Utils.sleep(1500);
            }
        }

        Utils.clickCompone(Utils.findViewByText(service, "完成"));
        Utils.sleep(1500);

        AccessibilityNodeInfo back = Utils.findViewByText(service, "返回");//com.tencent.mobileqq:id/ivTitleBtnLeft
        if (back != null) {
            Utils.clickCompone(back);
            Utils.sleep(2000);
        }

        //"http://img2.woyaogexing.com/2017/09/15/249e6094b234877d!400x400_big.jpg"
        LogUtils.logInfo("infoEntity.getHeadurl()=" + infoEntity.getHead_url());
        downloadPicture(infoEntity.getHead_url());
    }

    /***
     * 设置年月日  （蓝魔）
     * @param newYear
     * @param newMonth
     * @param newDay
     * @param oldYear
     * @param oldMonth
     * @param oldDay
     */
    private void settingDateRamos(UserInfoEntity infoEntity, int newYear, int newMonth, int newDay, int oldYear, int oldMonth, int oldDay) {
        LogUtils.i("蓝魔");
        if (newYear > oldYear) {
            for (int i = 0; i < newYear - oldYear; i++) {
                Utils.tapScreenXY("180 1560");
                Utils.sleep(2000);
            }
        } else if (newYear < oldYear) {
            for (int i = 0; i < oldYear - newYear; i++) {
                Utils.tapScreenXY("200 1420");
                Utils.sleep(2000);
            }
        }

        if (newMonth > oldMonth) {
            for (int i = 0; i < newMonth - oldMonth; i++) {
                Utils.tapScreenXY("590 1560");
                Utils.sleep(2000);
            }
        } else if (oldMonth > newMonth) {
            for (int i = 0; i < oldMonth - newMonth; i++) {
                Utils.tapScreenXY("590 1425");
                Utils.sleep(2000);
            }
        }

        if (newDay > oldDay) {
            for (int i = 0; i < newDay - oldDay; i++) {
                Utils.tapScreenXY("920 1564");
                Utils.sleep(2000);
            }
        } else if (oldDay > newDay) {
            for (int i = 0; i < oldDay - newDay; i++) {
                Utils.tapScreenXY("920 1425");
                Utils.sleep(2000);
            }
        }

        Utils.clickCompone(Utils.findViewByText(service, "完成"));
        Utils.sleep(1500);

        AccessibilityNodeInfo back = Utils.findViewByText(service, "返回");//com.tencent.mobileqq:id/ivTitleBtnLeft
        if (back != null) {
            Utils.clickCompone(back);
            Utils.sleep(2000);
        }

        //"http://img2.woyaogexing.com/2017/09/15/249e6094b234877d!400x400_big.jpg"
        LogUtils.logInfo("infoEntity.getHeadurl()=" + infoEntity.getHead_url());
        downloadPicture(infoEntity.getHead_url());
    }

    /***
     *
     * 設置性別
     */
    private void settingSex(UserInfoEntity infoEntity) {
        LogUtils.logInfo("設置性別");
        AccessibilityNodeInfo sexNodeInfo = Utils.findViewByText(service, "性别");
        if (sexNodeInfo != null) {
            String sex = sexNodeInfo.getParent().getChild(4).getChild(1).getText() + "";
            if (!(sex.equals(infoEntity.getSex()))) {
                Utils.clickCompone(sexNodeInfo);
                Utils.sleep(3000);
                AccessibilityNodeInfo sexinfo = Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView");
                if (sexinfo != null) {
                    String phonetype = Utils.getSystemModel();
                    switch (phonetype) {
                        case Constants.HONOR_PHONE_MODEL:
                            if (infoEntity.getSex().equals("男")) {
                                for (int i = 0; i < 2; i++) {
                                    Utils.tapScreenXY("333 964");
                                    Utils.sleep(1000);
                                }
                            } else {
                                for (int i = 0; i < 2; i++) {
                                    Utils.tapScreenXY("333 1074");
                                    Utils.sleep(1000);
                                }
                            }
                            break;
                        case Constants.RAMOS_PHONE_MODEL:
                            if (infoEntity.getSex().equals("男")) {
                                for (int i = 0; i < 2; i++) {
                                    Utils.tapScreenXY("530 1420");
                                    Utils.sleep(1000);
                                }
                            } else {
                                for (int i = 0; i < 2; i++) {
                                    Utils.tapScreenXY("510 1570");
                                    Utils.sleep(1000);
                                }
                            }
                            break;
                    }
                    Utils.clickCompone(Utils.findViewByText(service, "完成"));
                    Utils.sleep(1500);
                    modifyBirthday(infoEntity);
                }
            } else {
                modifyBirthday(infoEntity);
            }
        } else {
            LogUtils.logInfo("未找到标识");
            modifyBirthday(infoEntity);
        }
    }

    /***
     *
     * 設置昵稱
     */
    private void modifyNickName(UserInfoEntity infoEntity) {
        LogUtils.logInfo("設置昵稱");
        AccessibilityNodeInfo editinfo = Utils.findViewByType(service, EditText.class.getName());
        if (editinfo != null) {
            Utils.componeFocus(editinfo);
            Utils.sleep(1000);
            Utils.selectAllText(editinfo);
            Utils.sleep(1000);
            Utils.inputText(service, editinfo, infoEntity.getNickname());
            Utils.sleep(3000);
            Utils.pressBack(service);
            Utils.sleep(1500L);
            settingSex(infoEntity);
        }
    }

    /***
     * 修改签名
     */
    private void ModifyAutograph(UserInfoEntity infoEntity) {
        LogUtils.logInfo("修改签名  " + infoEntity.getSignature());
        String sign = infoEntity.getSignature();
        if (!TextUtils.isEmpty(sign)) {
            AccessibilityNodeInfo signature = Utils.findViewByText(service, "签名");
            if (signature != null) {
                String signtxt = signature.getParent().getChild(2).getChild(1).getText() + "";
                LogUtils.logError("   签名  = " + signtxt);
                if (!(sign.equals(signtxt)) || TextUtils.isEmpty(signtxt) || signtxt.equals("null")) {
                    Utils.clickCompone(signature);
                    Utils.sleep(3000L);

                    AccessibilityNodeInfo info = Utils.findViewByText(service, "写签名");
                    if (info != null) {
                        Utils.clickCompone(info);
                        Utils.sleep(2000);
                        inputText(sign);
                        modifyNickName(infoEntity);
                    } else {
                        AccessibilityNodeInfo et_text = Utils.findViewByType(service, EditText.class.getName());
                        if (et_text == null) {
                            Utils.pressBack(service);
                            Utils.sleep(2000L);
                        } else {
                            LogUtils.i("找到签名输入框");
                            Utils.componeFocus(et_text);
                            Utils.sleep(1000);
                            Utils.selectAllText(et_text);
                            Utils.sleep(1000);
                            Utils.inputText(service, et_text, sign);
                            Utils.sleep(4000);
                            Utils.clickCompone(Utils.findViewByTextMatch(service, "发布签名"));
                            Utils.sleep(5000);

                            if (Utils.findViewByTextMatch(service, "发布签名") != null) {
                                Utils.findViewById(service, "com.qqindividuality:id/ivTitleBtnLeft");
                                Utils.sleep(2000L);
                                if (Utils.findViewByText(service, "你的签名修改后还没") != null) {
                                    Utils.findViewByTextMatch(service, "退出");
                                    Utils.sleep(2000L);
                                }
                            }
                        }
                        modifyNickName(infoEntity);
                    }
                } else {
                    modifyNickName(infoEntity);
                }
            }
        }
    }

    /**
     * 选择相册
     */
    private void settingHeadPortrait(String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            if (Utils.findViewByText(service, "头像") != null) {
                Utils.clickCompone(Utils.findViewByText(service, "头像"));
                Utils.sleep(1500L);

                Utils.clickCompone(Utils.findViewByText(service, "从相册选择图片"));
                Utils.sleep(5000L);

                AccessibilityNodeInfo photos = Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnLeft");
                if (photos != null) {
                    LogUtils.logInfo(" 找到相冊 ");
//            Utils.clickCompone(photos);// 相册  com.tencent.mobileqq:id/ivTitleBtnLeft
                    Utils.clickComponeByXY(photos);
                    Utils.sleep(4000L);

                    AccessibilityNodeInfo camera = Utils.findViewByText(service, "Camera");
                    AccessibilityNodeInfo photo = Utils.findViewByText(service, "QQPhoto");
                    if (camera != null) {
                        Utils.clickComponeByXY(camera);
                        Utils.sleep(4000L);
                        settingHeard(1, filepath);
                    } else if (photo != null) {
                        Utils.clickComponeByXY(photo);
                        Utils.sleep(4000L);
                        settingHeard(1, filepath);
                    } else {
                        Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                        Utils.sleep(2000L);
                    }
                }
            }
        }
    }

    private void returnBack(String data) {
        Utils.delImageToPhoto();
        FileUtils.deleteFile(data);
        boolean isfind = true;
        while (isfind) {
            AccessibilityNodeInfo nodeInfo = Utils.findViewByDesc(service, "帐户及设置");
            if (nodeInfo != null) {
                isfind = false;
            } else {
                Utils.pressBack(service);
                Utils.sleep(2000);
            }
        }
        LogUtils.logInfo("修改信息執行完成");
        FileUtils.writeFileToSDCard("\t \t 修改信息執行完成：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
        handler.sendEmptyMessage(1);
    }

    /***
     * 设置头像
     */
    private void settingHeard(int index, String images) {
        AccessibilityNodeInfo imges = Utils.findViewById(service, "com.tencent.mobileqq:id/photo_list_gv");
        if (imges != null && imges.getChildCount() > 0) {
            LogUtils.logInfo("size = " + imges.getChildCount() + "    ,   ");
            Utils.clickComponeByXY(imges.getChild(0));
            Utils.sleep(3000L);
        }

        Utils.clickCompone(Utils.findViewByText(service, "完成"));
        Utils.sleep(1500L);

        if (index == 2) {
            Utils.delImageToPhoto();
            FileUtils.deleteFile(images);
            Utils.sleep(3000L);
            if (Utils.findViewByType(service, ProgressBar.class.getName()) != null) {
                Utils.sleep(4000L);
            }
            returnBack(images);
        } else {
            Utils.clickCompone(Utils.findViewByText(service, "返回"));
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(2000L);
            setBackground(images);
        }


    }

    /**
     * Edittext 输入
     *
     * @param str
     */
    private void inputText(String str) {
        LogUtils.logInfo("编辑签名");
        AccessibilityNodeInfo edit = Utils.findViewByText(service, "编辑签名");
        if (edit != null) {
            AccessibilityNodeInfo et_text = Utils.findViewByType(service, EditText.class.getName());
            if (et_text == null) {
                return;
            }
            LogUtils.logInfo("   " + et_text.getText());
            LogUtils.logInfo("new 簽名=" + str);
            Utils.sleep(2000);
            if (str.equals(et_text.getText().toString())) {
                LogUtils.logInfo("  簽名一致 ");
                AccessibilityNodeInfo backInfo = Utils.findViewByText(service, "返回");
                if (backInfo != null) {
                    Utils.clickComponeByXY(backInfo);//返回
                    Utils.sleep(3000);
                }

                AccessibilityNodeInfo dialog = Utils.findViewById(service, "android:id/content");
                if (dialog != null) {
                    Utils.clickComponeByXY(Utils.findViewByDesc(service, "退出按钮"));//com.tencent.mobileqq:id/dialogLeftBtn
                    Utils.sleep(2500);
                }
            } else {
                if (!TextUtils.isEmpty(str)) {
                    Utils.componeFocus(et_text);
                    Utils.sleep(1000);
                    Utils.selectAllText(et_text);
                    Utils.sleep(1000);
                    Utils.inputText(service, et_text, str);
                    Utils.sleep(4000);
                }
                Utils.clickCompone(Utils.findViewByText(service, "发布签名"));
                Utils.sleep(5000);
                if (Utils.findViewByTextMatch(service, "发布签名") != null) {
                    LogUtils.i("发布签名未成功");
                    Utils.clickComponeByXY(Utils.findViewById(service, "com.qqindividuality:id/ivTitleBtnLeft"));
                    Utils.sleep(2000L);
                    if (Utils.findViewByText(service, "你的签名修改后还没") != null) {
                        Utils.findViewByTextMatch(service, "退出");
                        Utils.sleep(2000L);
                    }
                }
            }
        }
    }

    /***
     *
     * 下载图片
     * */
    private void downloadPicture(String url) {
        if (url != null) {
            HttpTask.getInstance().download(url, new HttpTask.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    LogUtils.logInfo("    data    " + data);
                    if (!TextUtils.isEmpty(data)) {
                        File fileName = new File(data);
//                        LogUtils.logInfo("    fileName    " + fileName.toString());
                        Utils.saveImageToPhoto(service, fileName);
                        Message msg = currentHandler.obtainMessage();
                        msg.obj = data;
                        msg.what = 1;
                        msg.sendToTarget();
                    } else {
                        LogUtils.logInfo("未下载图片");
                        FileUtils.writeFileToSDCard("\t \t 未下载图片：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                        returnBack(data);
                    }
                }

                @Override
                public void onFailure(String errMsg) {
                    LogUtils.logInfo("头像下载失败  " + errMsg);
                    returnBack("");
                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            LogUtils.logInfo("url 为空 无值");
            returnBack("");
        }
    }

    /***
     * 设置职业
     * @param occustr 职业
     */
    private void settingOccupation(String occustr) {
//        String[] occues = occustr.split("");
        AccessibilityNodeInfo occupationInfo = Utils.findViewByTextMatch(service, "职业");
        if (occupationInfo != null) {
            Utils.clickCompone(occupationInfo);
            Utils.sleep(2000L);
            Utils.clickCompone(Utils.findViewByText(service, occustr));
            Utils.sleep(2000L);
        }
    }

    private boolean isrun = true;
    private int index = 0;

    /**
     * 设置个人说明
     *
     * @param explanation
     */
    private void settingPersonalExplanation(String explanation) {
        if (index == 2) {
            index = 0;
            isrun = false;
        }
        if (isrun) {
            AccessibilityNodeInfo explanationInfo = Utils.findViewByText(service, "个人说明");
            if (explanationInfo != null) {
                Utils.clickCompone(explanationInfo);
                Utils.sleep(2000L);
                AccessibilityNodeInfo explanationEdit = Utils.findViewByType(service, EditText.class.getName());
                if (explanationEdit != null) {
                    Utils.inputText(service, explanationEdit, explanation);
                    Utils.sleep(2000L);
                }
                Utils.clickCompone(Utils.findViewByText(service, "完成"));
                Utils.sleep(2000L);
            } else {
                Utils.pressSmallScrollDown();
                Utils.sleep(2000L);
                index++;
                settingPersonalExplanation(explanation);
            }
        }
    }

    private void setLocation(String area) {
        String[] areaes = area.split("@@@");
    }

    private Handler currentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Object o = msg.obj;
                String data = o.toString();
                settingHeadPortrait(data);
            }
        }
    };

    private void setBackground(String data) {
        AccessibilityNodeInfo info = Utils.findViewByDesc(service, "点击更换背景");
        if (info != null) {
            Utils.clickCompone(info);
            Utils.sleep(2000L);
            AccessibilityNodeInfo sheetInfo = Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView");
            if (sheetInfo != null) {
                Utils.clickCompone(Utils.findViewByText(service, "从手机相册选择"));
                Utils.sleep(2000L);

                Utils.clickCompone(Utils.findViewByTextMatch(service, "相册"));
                Utils.sleep(2000L);
                AccessibilityNodeInfo camera = Utils.findViewByText(service, "Camera");
                AccessibilityNodeInfo photo = Utils.findViewByText(service, "QQPhoto");
                if (camera != null) {
                    Utils.clickComponeByXY(camera);
                    Utils.sleep(4000L);
                    settingHeard(2, data);
                } else if (photo != null) {
                    Utils.clickComponeByXY(photo);
                    Utils.sleep(4000L);
                    settingHeard(2, data);
                } else {
                    Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                    Utils.sleep(2000L);
                }
            }
        }
    }

}
