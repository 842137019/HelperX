package com.cc.task.helperx.task;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.cc.task.helperx.service.HelperQQService;

/**
 * 回復消息
 * Created by fangying on 2017/9/12.
 */

public class ReplyMessageTask {
    private HelperQQService service;
    private Handler handler;
    private static final int SEND_PHOTO = 1;
    private static final int SEND_TEXT = 2;
    private int index = 0;
    private SQLiteDatabase database;

    public ReplyMessageTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;

    }

    /***
     * 获取回复数据
     * @param taskEntry
     */
//    public void sendTypeMsg(final TaskEntry taskEntry) {
//        helper = DBHelper.getDBInstance(service, "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + "/databases/" + taskEntry.getWx_sign() + ".db", null, DbConfig.DB_NEW_VERSION);
//        if (taskEntry != null) {
//            LogUtils.logInfo("      请求获取回复数据       ");
//            HttpHandler.getqunfaMsg(taskEntry.getWx_sign(), "", new HttpTask.HttpCallback() {
//                @Override
//                public void onSuccess(String data) {
//                    LogUtils.logInfo("data  =" + data);
//                    if (!TextUtils.isEmpty(data) && data.length() > 4) {
//                        List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
//                        try {
//                            org.json.JSONArray jsonArray = new org.json.JSONArray(data);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                MessageEntity messageEntity = new MessageEntity();
//                                int type = jsonObject.getInt("type");
//                                String sc_id = jsonObject.getString("sc_id");
//                                String wx_sign = jsonObject.getString("wx_sign");
//                                String text = jsonObject.getString("text");
//                                String imgUrl = jsonObject.getString("imgUrl");
//                                String weburl = jsonObject.getString("weburl");
//
//                                messageEntity.setImgUrl(imgUrl);
//                                messageEntity.setWx_sign(wx_sign);
//                                messageEntity.setSc_id(sc_id);
//                                messageEntity.setText(text);
//                                messageEntity.setType(type);
//                                messageEntity.setWeburl(weburl);
//
//                                messageEntities.add(messageEntity);
//                                messageEntity = null;
//                            }
//                            replyMsg(messageEntities, taskEntry);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        HttpHandler.reportError(taskEntry.getWx_sign(), "获取回复數據为空!");
//                        handler.sendEmptyMessage(1);
//                    }
//                }
//
//                @Override
//                public void onFailure(String errMsg) {
//                    LogUtils.logInfo("err  =" + errMsg);
//                    HttpHandler.reportError(taskEntry.getWx_sign(), "获取回复數據失败!");
//                    handler.sendEmptyMessage(1);
//                }
//
//                @Override
//                public void onFinished() {
//                }
//            });
//        }
//    }
//
//    public void replyMsg(List<MessageEntity> messageEntityList, TaskEntry taskEntry) {
//        AccessibilityNodeInfo msg = Utils.findViewByText(service, "消息");
//        if (msg == null) {
//            handler.sendEmptyMessage(1);
//            return;
//        }
//        Utils.clickCompone(msg);
//        Utils.sleep(2000);
//
//        List<AccessibilityNodeInfo> msglist = Utils.findViewListById(service, "com.tencent.mobileqq:id/unreadmsg");
//        if (msglist != null) {
//            LogUtils.logInfo("消息标识");
//            Utils.sleep(2000);
//            if (msglist.size() > 0) {
//                LogUtils.logInfo("第" + index + "次        " + "msglist.size()=" + msglist.size());
//                Utils.sleep(1500);
//                runReplyMsg(messageEntityList, msglist.get(0), taskEntry);
//            } else {
//                LogUtils.logError("发送完成");
////                AccessibilityNodeInfo info = Utils.findViewById(service, "android:id/tabs");
////                if (info.getChild(0).getChild(1) != null && !TextUtils.isEmpty(info.getChild(0).getChild(1).getText().toString())) {
////                    if (Utils.isNumber(info.getChild(0).getChild(1).getText().toString())) {
////                        if (Utils.getSystemModel().equals(Constants.HONOR_PHONE_MODEL)) {
////                            Utils.swipeUp("159 1143 400 886");
////                        }
////                        Utils.sleep(2000);
////                    }
////                }
//                Utils.removeObject(MessageEntity.class.getSimpleName());
//                Utils.sleep(2000);
//                boolean isfind = true;
//                while (isfind) {
//                    if (Utils.findViewByDesc(service, "帐户及设置") != null) {
//                        isfind = false;
//                    } else {
//                        Utils.pressBack(service);
//                        Utils.sleep(2500L);
//                    }
//                }
//
//                LogUtils.logInfo("回复消息執行完成");
//                handler.sendEmptyMessage(1);
//            }
//        }
//    }
//
//    private void runReplyMsg(List<MessageEntity> messageEntityList, AccessibilityNodeInfo msg, TaskEntry taskEntry) {
//        int runNum = openMsg(msg, taskEntry);
//        String titleName = "";
//        if (runNum != 8) {
//            //com.tencent.mobileqq:id/title
//            Utils.sleep(1500);
//            AccessibilityNodeInfo title = Utils.findViewById(service, "com.tencent.mobileqq:id/title");
//            if (title != null) {
//                titleName = title.getText().toString();
//                sendReplyMsg(messageEntityList, runNum, titleName, taskEntry);
//            }
//            Utils.sleep(2000);
//        } else {
//            index++;
//            Utils.sleep(4000);
//            replyMsg(messageEntityList, taskEntry);
//        }
//    }
//
//    /***
//     * 判断聊天对象
//     * @param msglist
//     * @return
//     */
//    private int openMsg(AccessibilityNodeInfo msglist, TaskEntry taskEntry) {
//        Utils.clickCompone(msglist);
//        Utils.sleep(3000);
//
//        AccessibilityNodeInfo gv = Utils.findViewByType(service, GridView.class.getName());
//        if (gv != null) {
//            Utils.pressBack(service);
//            Utils.sleep(2000);
//        }
//
//        AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
//        if (groupNotice != null) {
//            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
//            Utils.sleep(3000);
//        }
//        AccessibilityNodeInfo title = Utils.findViewById(service, "com.tencent.mobileqq:id/title");
//        AccessibilityNodeInfo ivTitleName = Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleName");//com.tencent.mobileqq:id/ivTitleName
//        if (ivTitleName != null) {
//            LogUtils.logInfo("   " + ivTitleName.getText().toString() + "   ,");
//            if (otherText(ivTitleName) == 8) {
//                return 8;
//            }
//        } else if (title != null) {
//            if (otherText(title) == 8) {
//                return 8;
//            }
//            LogUtils.logInfo("其他账号    " + title.getText().toString());
//            if (!TextUtils.isEmpty(title.getText())) {
//                String strName = Utils.sqliteEscape(title.getText().toString());
//                database = helper.getWritableDatabase();
//                if (operator.isDataExists(database, DbConfig.TB_REPLYMSG, DbConfig.C_REPLYMSG_NAME, DbConfig.C_REPLYMSG_NAME, strName)) {
////                    List<ReplyMsgInfo> replyMsgInfoList = operator.queryReplyMsg(database, strName);
////                    LogUtils.logInfo("   replyMsgInfoList size = " + replyMsgInfoList.size());
//                    ReplyMsgInfo replyMsgInfo = operator.queryReplyMsg(database, strName);
//                    String replyTime = replyMsgInfo.getMsgTime();
//                    LogUtils.logInfo("  replyTime   =  " + replyTime);
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    String date = sdf.format(new java.util.Date());
//                    if (replyTime.contains(date)) {
//                        return 8;
//                    }
//                }
//                AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
//                AccessibilityNodeInfo singleChat = Utils.findViewByDesc(service, "聊天设置");
//                if (groupChat != null) {
//                    LogUtils.logInfo(" 與群聊天 ");
//                    AccessibilityNodeInfo nodeInfo = Utils.findViewByText(service, "全员禁言中");
//                    if (nodeInfo != null) {
//                        LogUtils.logInfo(" 全员禁言中 -------  返回");
//                        Utils.pressBack(service);
//                        Utils.sleep(2000);
//                        return 8;
//                    }
//                    return 1;
//                } else if (singleChat != null) {
//                    LogUtils.logInfo(" 與單個對象聊天 ");
//                    return 2;
//                }
//            }
//        }
//        return 0;
//    }
//
//    /***
//     *
//     * 其他聊天
//     * @param title
//     */
//    private int otherText(AccessibilityNodeInfo title) {
//        switch (title.getText().toString()) {
//            case "看点":
//                Utils.pressScrollDown();
//                Utils.sleep(3000);
//                Utils.pressBack(service);
//                Utils.sleep(2500);
//                break;
//            case "我的其他QQ帐号":
//            case "腾讯新闻":
//            case "QQ手游":
//            case "QQ邮箱提醒":
//            case "QQ会员":
//            case "一声问候":
//            case "QQ购物":
//            case "群发祝福":
//            case "闪电换机":
//                Utils.pressBack(service);
//                Utils.sleep(2500);
//                break;
//            case "服务号":
//                serviceNumber();
//                break;
//            case "群通知":
//                groupNotification();
//                break;
////            case "腾讯新闻":
//////                BrowseNewsTask newsTask = new BrowseNewsTask(service,handler);
//////                newsTask.exeReadNews();
////                break;
//            default:
//                return 0;
//        }
//        return 8;
//    }
//
//    /****
//     *
//     * 群通知
//     *
//     */
//    private void groupNotification() {
//        List<AccessibilityNodeInfo> agrees = Utils.findViewListByText(service, "同意");
//        if (agrees.size() > 0) {
//            for (int j = 0; j < agrees.size(); j++) {
//                Utils.clickCompone(agrees.get(0));
//                Utils.sleep(4000);
//            }
//            if (Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnRightImage") != null) {
//                Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnRightImage"));
//                Utils.sleep(2000);
//                if (Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView") != null) {
//                    Utils.clickCompone(Utils.findViewByText(service, "清空群通知"));
//                    Utils.sleep(3000);
//                }
//            }
//        }
//        if (Utils.findViewByText(service, "暂时没有群通知") != null) {
//            Utils.pressBack(service);
//            Utils.sleep(2000);
//        }
//    }
//
//    private void serviceNumber() {
//        List<AccessibilityNodeInfo> icons = Utils.findViewListById(service, "com.tencent.mobileqq:id/icon");
//        if (icons.size() > 0) {
//            for (int y = 0; y < icons.size(); y++) {
//                Utils.clickCompone(icons.get(0));
//                Utils.sleep(2000);
//
//                Utils.clickCompone(Utils.findViewByDesc(service, "查看帐号资料"));//com.tencent.mobileqq:id/ivTitleBtnRightImage
//                Utils.sleep(2000);
//
//                Utils.clickCompone(Utils.findViewByDesc(service, "更多操作"));//com.tencent.mobileqq:id/ivTitleBtnRightImage
//                Utils.sleep(4000);
//
//                if (Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView") != null) {//com.tencent.mobileqq:id/action_sheet_actionView
//                    if (Utils.findViewByText(service, "取消关注") != null) {
//                        Utils.clickCompone(Utils.findViewByText(service, "取消关注"));
//                        Utils.sleep(2000);
//
//                        if (Utils.findViewByText(service, "不再关注") != null) {
//
//                            Utils.clickCompone(Utils.findViewByText(service, "不再关注"));
//                            Utils.sleep(2000);
//                        }
//                    } else {
//                        for (int o = 0; o < 3; o++) {
//                            Utils.pressBack(service);
//                            Utils.sleep(2000);
//                        }
//                    }
//
//                }
//            }
//            Utils.pressBack(service);
//            Utils.sleep(2000);
//        }
//    }
//
//
//    /****
//     * 回复信息类别
//     * @param
//     */
//    private void sendReplyMsg(List<MessageEntity> messageEntityList, int objectType, String title, TaskEntry taskEntry) {
//        LogUtils.logInfo("  回复信息类别 ");
//        Utils.sleep(2000);
//
//        LogUtils.logInfo("   he");
//
//        int type = 0;
//        if (type < messageEntityList.size()) {
//            MessageEntity messageEntity = messageEntityList.get(type);
//            SendMsg(messageEntity, objectType, title, messageEntityList, taskEntry);
//            type++;
//        } else {
//            SendMsg(messageEntityList.get(type), objectType, title, messageEntityList, taskEntry);
//        }
//    }
//
//    private void SendMsg(MessageEntity messageEntity, int objectType, String title, List<MessageEntity> messageEntityList, TaskEntry taskEntry) {
//        switch (messageEntity.getType()) {
//            case SEND_PHOTO:
//                LogUtils.logInfo("發送圖片  url=" + messageEntity.getImgUrl());
//                sendAndDownloadImg(objectType, title, messageEntityList, messageEntity, taskEntry);
//                break;
//
//            case SEND_TEXT:
//                LogUtils.logInfo("發送文本");
//                List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, RelativeLayout.class.getName());
//                LogUtils.logInfo("group   %%%%%%%%------%%%%%%%" + list.get(list.size() - 2).getChild(list.get(list.size() - 2).getChildCount() - 1).toString());
//
//                Utils.sleep(2000);
//                sendMsg(taskEntry.getWx_sign(), messageEntity.getSc_id(), objectType, title, messageEntity.getText());
////                Utils.swipeUp("607 311 304 311");
////                Utils.sleep(2000);
////                AccessibilityNodeInfo delete = Utils.findViewByDesc(service, "删除");
////                if (delete != null) {
////                    Utils.clickCompone(delete);
////                    Utils.sleep(2000);
////                }
//                replyMsg(messageEntityList, taskEntry);
//                break;
//        }
//    }
//
//    private void sendAndDownloadImg(int type, String name, List<MessageEntity> messageEntityList, MessageEntity messageEntity, TaskEntry taskEntry) {
//        if (!TextUtils.isEmpty(messageEntity.getImgUrl())) {
//            List<String> datas = new ArrayList<>();
//            String[] imgUrls = messageEntity.getImgUrl().split("@@@");
//            LogUtils.logInfo("   imgUrls.length= " + imgUrls.length);
//            List<String> urlList = Arrays.asList(imgUrls);
//            Utils.sleep(1000);
//            downloadImage(type, name, messageEntityList, messageEntity, datas, urlList, 0, taskEntry);
//        }
//    }
//
//    /**
//     * @param type     类型
//     * @param name     聊天对象名称
//     * @param
//     * @param entity
//     * @param imgDatas
//     * @param imgUrls
//     * @param ind
//     */
//    private void downloadImage(final int type, final String name, final List<MessageEntity> messageEntityList, final MessageEntity entity, final List<String> imgDatas, final List<String> imgUrls, final int ind, final TaskEntry taskEntry) {
//        if (ind < imgUrls.size()) {
//            String url = imgUrls.get(ind);
//            LogUtils.logInfo("     下载图片     " + ind + "    " + url);
//            String[] names = url.split("/");
//            String fileName = names[names.length - 1];
//            String parentPath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + Constants.QQ_PHOTO;
//            String filePath = parentPath + "/" + fileName;
//            LogUtils.logInfo(" filePath = " + filePath);
//            try {
//                LogUtils.logInfo("" + FileUtils.fileIsExists(filePath));
//            } catch (Exception e) {
//                e.getMessage();
//            }
////            if (!FileUtils.fileIsExists(filePath)) {
////                Utils.delImageToPhoto();
//            HttpTask.getInstance().download(url, new HttpTask.HttpCallback() {
//                @Override
//                public void onSuccess(String data) {
//                    if (!TextUtils.isEmpty(data)) {
//                        LogUtils.logInfo("  yu   data     =" + data);
//                        File file = new File(data);
//                        Utils.saveImageToPhoto(service, file);
//                        imgDatas.add(data);
//                    }
//                }
//
//                @Override
//                public void onFailure(String errMsg) {
//                    LogUtils.logInfo(errMsg);
//                }
//
//                @Override
//                public void onFinished() {
//                    downloadImage(type, name, messageEntityList, entity, imgDatas, imgUrls, (ind + 1), taskEntry);
//                }
//            });
////            }
//        } else {
//            sendPhoto(taskEntry.getWx_sign(), entity.getSc_id(), type, name, imgDatas);
//            Utils.sleep(2000);
//            index++;
//            replyMsg(messageEntityList, taskEntry);
//        }
//    }
//
//    /***
//     * 发送图片
//     * @param datas
//     */
//    private void sendPhoto(String wx_sign, String sc_id, int type, String name, List<String> datas) {
//        LogUtils.logInfo("     发送图片     datas= " + datas.size());
//        if (datas.size() > 0) {
//            String phonetype = Utils.getSystemModel();
//            switch (phonetype) {
//                // 點擊發送圖片圖標
//                case Constants.HONOR_PHONE_MODEL:
//                    Utils.tapScreenXY("154 1185");
//                    Utils.sleep(3000);
//                    break;
//                case Constants.RAMOS_PHONE_MODEL:
//                    Utils.tapScreenXY("225 1750");
//                    Utils.sleep(3000);
//                    break;
//            }
//            AccessibilityNodeInfo photos = Utils.findViewByText(service, "相册");
//            if (photos != null) {
//                Utils.clickCompone(photos);
//                Utils.sleep(3000);
//            }
//
//            AccessibilityNodeInfo camera = Utils.findViewByText(service, "Camera");
//            AccessibilityNodeInfo photo = Utils.findViewByText(service, "QQPhoto");
//            if (camera != null) {
//                Utils.clickComponeByXY(camera);
//                Utils.sleep(4000);
//                sendPhotoMsg(wx_sign, sc_id, type, name, datas);
//            } else if (photo != null) {
//                Utils.clickComponeByXY(photo);
//                Utils.sleep(4000);
//                sendPhotoMsg(wx_sign, sc_id, type, name, datas);
//            } else {
//                Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
//                Utils.sleep(2000);
//
//                Utils.pressBack(service);
//                Utils.sleep(2000);
//            }
//        }
//    }
//
//    private void sendPhotoMsg(String sign, String sc_id,String groupId, int type, String name, List<String> datas) {
//        AccessibilityNodeInfo imges = Utils.findViewById(service, "com.tencent.mobileqq:id/photo_list_gv");
//        if (imges != null) {
//            LogUtils.logInfo("size = " + imges.getChildCount() + "    ,   ");
//            List<AccessibilityNodeInfo> checkes = Utils.findViewListByType(service, "android.widget.CheckBox");
//            if (checkes != null && checkes.size() > 0) {
//                LogUtils.logInfo("CheckBox size = " + checkes.size() + "    ,   ");
//                if (checkes.size() == 1) {
//                    Utils.clickCompone(checkes.get(0));
//                    Utils.sleep(2000);
//                } else {
//                    for (int i = 0; i < checkes.size() - 2; i++) {
//                        Utils.clickCompone(checkes.get(i));
//                        Utils.sleep(2000);
//                    }
//                }
//            }
//            Utils.clickCompone(Utils.findViewByText(service, "发送"));
//            Utils.sleep(1500);
//
//            if (Utils.findViewByTextMatch(service, "无法上传") != null) {
//                Utils.findViewByText(service, "我知道了");
//                Utils.sleep(2000L);
//
//                Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
//                Utils.sleep(2000);
//
//                Utils.pressBack(service);
//                Utils.sleep(2000);
//            } else {
//                LogUtils.logInfo("返回到消息页");
//                Utils.pressBack(service);
//                Utils.sleep(2000);
//                addDataToDB(sign, sc_id,groupId, type, name);
//            }
//        }
//    }
//
//    /***
//     *  输入文本并发送
//     * @param sendmsg
//     */
//    private void sendMsg(String wx_sign, String sc_id,String groupId, int type, String str, String sendmsg) {
//        if (!TextUtils.isEmpty(sendmsg)) {
//            AccessibilityNodeInfo editText = Utils.findViewByType(service, EditText.class.getName());
//            if (editText != null) {
//                Utils.componeFocus(editText);
//                Utils.sleep(2000L);
//                Utils.selectAllText(editText);
//                Utils.sleep(2000L);
//                Utils.inputText(service, editText, sendmsg);
//                Utils.sleep(2000L);
//                Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/fun_btn"));
//                Utils.sleep(3000L);
//                Utils.pressBack(service);
//                Utils.sleep(2000L);
////                switch (type){
////                    case 1:
////
////                        break;
////                    case 2:
////
////                        break;
////                    default:
////                        break;
////                }
////                qunfaFinish(wx_sign, sc_id);
//                addDataToDB(wx_sign, sc_id,groupId, type, str);
//
//            } else {
//                Utils.pressBack(service);
//                Utils.sleep(2000);
//            }
//        } else {
//            Utils.pressBack(service);
//            Utils.sleep(2000);
//
//        }
//    }
//
//    private void addDataToDB(String wx_sign, String sc_id,String groupId, int type, String str) {
//        LogUtils.logInfo("存入数据库");
//        qunfaFinish(wx_sign, sc_id, groupId);
//        String name = Utils.sqliteEscape(str);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//        String date = formatter.format(curDate);
//        ReplyMsgInfo replyMsgInfo = new ReplyMsgInfo();
//        replyMsgInfo.setMsgName(name);
//        replyMsgInfo.setMsgTime(date);
//        replyMsgInfo.setMsgType(type);
//        database = helper.getWritableDatabase();
//        if (operator.isDataExists(database, DbConfig.TB_REPLYMSG, DbConfig.C_REPLYMSG_NAME, DbConfig.C_REPLYMSG_NAME, name)) {
//            operator.updateReplyMsgData(database, replyMsgInfo);
//        } else {
//            operator.addReplyMsgData(database, replyMsgInfo);
//        }
//    }
//
//    private void qunfaFinish(String wxsign, String sc_id, String groupId) {
//        HttpHandler.qunfaFinish(wxsign, sc_id, groupId, new HttpTask.HttpCallback() {
//            @Override
//            public void onSuccess(String data) {
//                LogUtils.logInfo("  qunfaFinish    data = " + data);
//            }
//
//            @Override
//            public void onFailure(String errMsg) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }
//
//
//    private int findCount = 0;
//
//    private void resetListView(HelperQQService service) {
//        AccessibilityNodeInfo listView = Utils.findViewById(service, Constants.WX_LIST_ID);
//        if (listView != null) {
//            for (int x = 0; x < findCount; x++) {
//                Utils.scrollViewUp(listView);
//                Utils.sleep(3000L);
//            }
//            listView.recycle();
//        }
//        findCount = 0;
//    }
//
//    AccessibilityNodeInfo change;
//
//    public void exeReadNews() {
//        Utils.pressScrollDown();
//        Utils.sleep(5000L);
//        AccessibilityNodeInfo listView = Utils.findViewByType(service, AbsListView.class.getName());
//        if (listView != null && listView.getChildCount() > 0) {
//            AccessibilityNodeInfo lastChld = listView.getChild(listView.getChildCount() - 1);
//            if (lastChld != null) {
//                NewsEntry newsEntry = new NewsEntry(lastChld, 0);
//                traversalNews(newsEntry);
//            } else {
//                listView.recycle();
//                if ((change = Utils.findViewByText(service, "换一批看看")) != null) {
//                    Utils.pressBack(service);
//                    Utils.sleep(3 * 1000L);
//                }
////                Utils.clickCompone(Utils.findViewByDesc(service, "返回"));
////                Utils.sleep(3 * 1000L);
////                handler.sendEmptyMessage(1);
//            }
//        } else {
//            if ((change = Utils.findViewByText(service, "换一批看看")) != null) {
//                Utils.pressBack(service);
//                Utils.sleep(3 * 1000L);
//            }
////            Utils.clickCompone(Utils.findViewByDesc(service, "返回"));
////            Utils.sleep(3 * 1000L);
////            handler.sendEmptyMessage(1);
//        }
//    }
//
//    private void traversalNews(NewsEntry newsEntry) {
//        AccessibilityNodeInfo lastChld = newsEntry.getLastChld();
//        int index = newsEntry.getIndex();
//        if (index < lastChld.getChildCount()) {
//            AccessibilityNodeInfo finalView = newsEntry.getLastChld().getChild(index);
//            if (finalView != null && finalView.getClassName().equals(LinearLayout.class.getName())) {
//                Utils.clickComponeByXY(finalView);
//                Utils.sleep(5 * 1000L);
//                if (Utils.isTragetActivity(Constants.PAGE_NEWS_CONTENT_PAGE)) {
//                    AccessibilityNodeInfo goonBtn = Utils.findViewByTextMatch(service, "继续");
//                    if (goonBtn != null) {
//                        Utils.clickCompone(goonBtn);
//                        Utils.sleep(3 * 1000L);
//                    }
//                    new ScrollNewThread(newsEntry).start();
//                } else {
//                    newsEntry.setIndex(index + 1);
//                    traversalNews(newsEntry);
//                }
//            } else {
//                newsEntry.setIndex(index + 1);
//                traversalNews(newsEntry);
//            }
//        } else {
//            Utils.sleep(3 * 1000L);
//            if ((change = Utils.findViewByText(service, "换一批看看")) != null) {
//                Utils.pressBack(service);
//                Utils.sleep(3 * 1000L);
//            }
//            resetListView(service);
//            Utils.sleep(3 * 1000L);
////            handler.sendEmptyMessage(1);
//        }
//    }
//
//    private class ScrollNewThread extends Thread {
//
//        private NewsEntry newsEntry;
//
//        public ScrollNewThread(NewsEntry newsEntry) {
//            this.newsEntry = newsEntry;
//        }
//
//        @Override
//        public void run() {
//            Utils.pressScrollDown();
//            Utils.sleep(4 * 1000L);
//            Utils.pressScrollDown();
//            Utils.sleep(4 * 1000L);
//            Utils.pressScrollDown();
//            Utils.sleep(4 * 1000L);
//            Utils.pressBack(service);
//            Utils.sleep(3 * 1000L);
//
//            newsEntry.setIndex(newsEntry.getIndex() + 1);
//            Message msg = currentHandler.obtainMessage();
//            msg.obj = newsEntry;
//            msg.what = 1;
//            msg.sendToTarget();
//        }
//    }
//
//    private Handler currentHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) {
//                Object obj = msg.obj;
//                if (obj != null && obj instanceof NewsEntry) {
//                    NewsEntry newsEntry = (NewsEntry) obj;
//                    traversalNews(newsEntry);
//                }
//            }
//        }
//    };
}
