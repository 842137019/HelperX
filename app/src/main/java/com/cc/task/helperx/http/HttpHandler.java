package com.cc.task.helperx.http;



import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.LogUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Administrator on 2017/8/22.
 */

public class HttpHandler {
    /***
     * 下载图片
     * @param imageUrl
     * @param callback
     */
    public static void downloadImage(String imageUrl, HttpTask.HttpCallback callback) {
        HttpTask.getInstance().download(imageUrl, callback);
    }

    /***
     * 请求任务
     * @param macId
     * @param callback
     */
    public static void requestTask(String macId, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "gettasktelsign");
        params.put("str1", macId);
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 从图灵获取消息
     * @param msg
     * @param tel_key
     * @param callback
     */
    public static void autoRespone(String msg, String tel_key, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", tel_key);
        params.put("info", msg);
        HttpTask.getInstance().post(Constants.AUTO_REPONSE_URL, params, callback);
    }

    /****
     * 任务完成上报
     * @param taskId
     * @param callback
     */
    public static void requestEndTask(String taskId, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "taskcomplet");
        params.put("str1", taskId);
        params.put("str2", "1");
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

//    public static void accountInfo(HttpTask.HttpCallback callback) {
//        callback.onFinished();
//    }

    /***
     * 子任务完成上报
     * @param wxSign
     * @param sc_id
     */
    public static void requestReport(String wxSign, int sc_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "sucai_finish");
        params.put("str1", wxSign);
        params.put("str2", sc_id + "");
        params.put("str3", "1");
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, null);
    }

    /***
     * 修改信息
     * @param wx_sign
     * @param callback
     */
    public static void requestModifyPersonal(String wx_sign, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "getinfosign");
        params.put("str1", wx_sign); //Wx_sign
        params.put("str2", "1");
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 备份
     * @param IMEI
     * @param mac
     * @param sid
     * @param sim
     * @param account
     * @param callback
     */
    public static void reportLocalInfo(String qq_sign, String IMEI, String mac, String sid, String sim,
                                       String account, String qq, String telNum, String email, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "upwxhard");
        params.put("str1", qq_sign);
        params.put("str2", IMEI);
        params.put("str3", mac);
        params.put("str4", sid);
        params.put("str5", sim);
        params.put("str6", account);
        params.put("str7", qq);
        params.put("str8", telNum);
        params.put("str9", email);
        params.put("str10", "1");
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 错误上报
     * @param wxSing
     * @param errorMsg
     */
    public static void reportError(String wxSing, String errorMsg) {
        String msg;
        try {
            msg = URLEncoder.encode(errorMsg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.logError("上报错误是,格式化错误信息失败", e);
            msg = errorMsg;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "up_deal_log");
        params.put("str1", wxSing + "");
        params.put("str2", msg);
        params.put("str3", "1");
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, null);
    }

    /***
     * 獲取要加的群號
     * @param account
     */
    public static void getAddGroup(String account, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "getgroup");
        params.put("str1", account);
        params.put("str2", "1");
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 提交执行当前加群任务的加群状态
     * @param wx_sign
     * @param object
     * @param callback
     */
    public static void up_deal_group(String wx_sign, Object object, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "up_deal_group");
        params.put("str1", wx_sign);
        params.put("str2", object.toString());
        params.put("str3", "1");
        HttpTask.getInstance().post(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 獲取要加的人
     * @param account
     * @param callback
     */
    public static void getAddPeople(String account, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "");
        params.put("str1", account);
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /****
     * 獲取要關閉的功能
     * @param account
     * @param callback
     */
    public static void getCloseFunction(String account, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "");
        params.put("str1", account);
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 消息回復
     * @param account
     * @param callback
     */
    public static void getReplyMsg(String account, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "getreply");
        params.put("str1", account);
        params.put("str2", "1");
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 获取群发数据
     * @param wx_sign
     * @param groupId
     * @param callback
     */
    //http://qq.down50.com/weixinoutput/wxapi.php?t=getqunfa&str1=b02217f03185b8a85993d07004e9e27e&str2=2&str3=69942368
    public static void getqunfaMsg(String wx_sign, String groupId, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "getqunfa");
        params.put("str1", wx_sign);
        params.put("str2", "2");
        params.put("str3",groupId);
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 提交群发成功的数据
     * @param account
     * @param sc_id
     * @param groupId
     */
    public static void qunfaFinish(String account, String sc_id, String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "qunfa_finish");
        params.put("str1", account);
        params.put("str2", sc_id);
        params.put("str3", groupId);
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, null);
    }

    /***
     * 提交QQ异常信息
     * @param wxSign
     * @param errorCode
     * @param callback
     */
    //http://qq.down50.com/weixinoutput/wxapi.php?t=lockwxsign&str1=03808b70ca6796b0a6e030977b4a03db&str2=-4
    public static void requestAccountError(String wxSign, String errorCode, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "lockwxsign");
        params.put("str1", wxSign);
        params.put("str2", errorCode);
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    public static void requestDelbackInfo(String macId, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "get_all_wx_sign");
        params.put("str1", macId);
        HttpTask.getInstance().get(Constants.TASK_SERVER_URL, params, callback);
    }

    /***
     * 下载文件apk
     * @param downloadUrl
     * @param callback
     */
    public static void requestDowloadTools(String downloadUrl, HttpTask.HttpCallback callback) {
        String savePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "update" + File.separator + "helperqq.apk";
        HttpTask.getInstance().download(downloadUrl, savePath, null, callback);
    }

    /***
     * 检查是否更新Tools.APK
     * @param macId
     * @param callback
     */
      // http://qq.down50.com/weixinoutput/updateinfo.php?t=helperqq&str1=afc10f3b331ab4ae034c29fd1d25361b
    public static void requestUpdateTools(String macId, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "helperqq");
        params.put("str1", macId);
        HttpTask.getInstance().get(Constants.UPDATE_URL, params, callback);
    }
//http://qq.down50.com/weixinoutput/updateinfo.php?t=updatehelperqq&str1=afc10f3b331ab4ae034c29fd1d25361b
    public static void updatehelperqq(String macId, HttpTask.HttpCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("t", "updatehelperqq");
        params.put("str1", macId);
        HttpTask.getInstance().get(Constants.UPDATE_URL, params, callback);
    }
}
