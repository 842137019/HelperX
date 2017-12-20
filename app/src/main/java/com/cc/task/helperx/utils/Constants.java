package com.cc.task.helperx.utils;

/**
 * 常量标识
 * Created by Administrator on 2017/8/21.
 */

public class Constants {


    public static final int SUPPORT_SERVICE_START = 1;

    public static final int SUPPORT_SERVICE_STOP = 0;

    /**
     * 机器标识文件
     */
    public static final String MAC_ID_FILE = "/sdcard/WXHelper/tel_sign.txt";
    public static final String MAC_ID_FILE_R = "/storage/emulated/0/phoneFile/tel_sign.txt";
    public static final String KEY_CHECK_SEND = "isCheckSend";
    public static final String CACHE_PATH_NAME = "QQTask";
    public static final String QQ_PHOTO = "QQPhoto";
    public static final String QQ_INFO = "AccountInfo";
    public static final String APP_NAME = "com.tencent.mobileqq";
    public static final String TASK_EXECUTE_TIME = "taskStartTime";

    // QQ主页
    public static final String QQ_HOME_ACTIVITY = "com.tencent.mobileqq/.activity.SplashActivity";
    // 登陆界面
    public static final String QQ_LOGIN_ACTIVITY = "com.tencent.mobileqq/.activity.LoginActivity";
    // 编辑个人信息界面
    public static final String QQ_MODIFYINFO_ACTIVITY = "com.tencent.mobileqq/.activity.FriendProfileMoreInfoActivity";
    // 個人資料界面
    public static final String QQ_USERINFO_ACTIVITY = "com.tencent.mobileqq/.activity.FriendProfileCardActivity";
    //QQ注册界面
    public static final String QQ_REGISTER_ACTIVITY = "com.tencent.mobileqq/.activity.RegisterPhoneNumActivity";
    // QQ新手任务
    public static final String QQ_BROWSER_ACTIVITY = "com.tencent.mobileqq/.activity.QQBrowserActivity";
    // 通知界面
    public static final String QQ_NOTIFICATION_ACTIVITY= " com.tencent.mobileqq/.activity.NotificationActivity";
    // 绑定手机号码界面
    public static final String QQ_ = "com.tencent.mobileqq/.activity.phone.BindNumberActivity";

    //加群 需要问题验证
    public static final String ADDGROUP_PROBLEM_SERIFICATION="com.tencent.mobileqq/.activity.AddFriendVerifyActivity";
    //加群 需要身份验证
    public static final String ADDGROUP_IDENTITY_SERIFICATION ="";

    public static final String QQ_Bind = "com.tencent.mobileqq/.activity.PhoneUnityIntroductionActivity";
    //群详情
    public static final String QQ_GROUP_TROOP="com.tencent.mobileqq/.activity.ChatSettingForTroop";
    // 添加好友/群/公众号等
    public static final String QQ_ADD_CONTACTS_ACTIVITY= "com.tencent.mobileqq/.activity.contact.addcontact.AddContactsActivity";

    // 搜索好友/群/公众号等
    public static final String QQ_SEARCH_CONTACTS_ACTIVITY= "com.tencent.mobileqq/.activity.contact.addcontact.SearchContactsActivity";

    // 联合搜索
    public static final String QQ_UNITE_SEARCH_ACTIVITY= "com.tencent.mobileqq/.search.activity.UniteSearchActivity";

    public static final String QQ_NEWS_CONTENT_PAGE = "";
    public static final String XPOSED_PACKAGE = "de.robv.android.xposed.installer";

    public static final String TASK_SERVER_URL = "http://qq.down50.com/weixinoutput/wxapi.php";

    public static final String QQ_LOGIN_ACCOUNT = "请输入QQ号码或手机或邮箱";
    public static final String QQ_LOGIN_PASSWORD = "com.tencent.mobileqq:id/password";//com.tencent.mobileqq:id/password  密码 安全
    public static final String QQ_LOGIN_BTN = "登 录";

    // 華爲
    public static final String HONOR_PHONE_MODEL = "SCL-AL00";
    // 藍魔
    public static final String RAMOS_PHONE_MODEL = "ramos MOS 1";

    // 加人
    public static final int TYPE_ADD_PEOPLE_TASK = 1;
    // 加群
    public static final int TYPE_ADD_GROUP_TASK = 7;
    // 還原
    public static final int TYPE_REDUCTION_TASK = 5;
    // 修改信息
    public static final int TYPE_MODIFYINFO_TASK = 2;
    // 備份
    public static final int TYPE_BACKUPS_TASK = 4;
    // 发群消息
    public static final int TYPE_MASSMSG_TASK = 13;
    // 关闭通知
    public static final int TYPE_CLOSE_NOTICE_TASK = 29;
    // 浏览新闻
    public static final int TYPE_BROWS_ENEWS_TASK = 3;

    public static final int TYPE_CLEAR_CHATRECORD_TASK = 23;

    public static final int TYPE_GROUP_MEMBERS_MSG_TASK = 32;

    // 圖靈機器人請求地址
    public static final String AUTO_REPONSE_URL = "http://www.tuling123.com/openapi/api";

//adb shell am start -n com.huawei.systemmanager/com.huawei.notificationmanager.ui.NotificationManagmentActivity
    public static final String PAGE_NEWS_CONTENT_PAGE = "com.tencent.mobileqq/com.tencent.biz.pubaccount.PublicAccountBrowser";
    public static final String WX_LIST_ID = "com.tencent.mobileqq:id/recent_chat_list";
    public static final String WX_LIST_ITEM_ID = "com.tencent.mobileqq:id/title";
    public static final String UPDATE_URL = "http://qq.down50.com/weixinoutput/updateinfo.php";

    // 手机设置通知栏
    //adb shell am start -n com.huawei.systemmanager/com.huawei.notificationmanager.ui.NotificationManagmentActivity
    public static final String NOTIFICATION_ACTIVITY="com.huawei.systemmanager/com.huawei.notificationmanager.ui.NotificationManagmentActivity";
    public static final String MOS_NOTIFICATION_ACTIVITY="com.android.settings/.Settings";
}
