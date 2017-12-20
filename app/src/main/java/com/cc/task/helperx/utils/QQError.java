package com.cc.task.helperx.utils;

import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.task.LoginTask;

/**
 * Created by fangying on 2017/9/26.
 */

public class QQError {
    private HelperQQService service;
    private Handler handler;

    public QQError(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    public static boolean validate(HelperQQService context, TaskEntry entry) {
        Utils.sleep(5000L);
        boolean isTrue = true;
        String txt = "";
        AccessibilityNodeInfo validateBtn = null;

//        if ((validateBtn = Utils.findViewByText(context, Button.class.getName(), "通过短信验证身份")) != null) {
//            validateBtn.recycle();
//            txt = "需要进行好友验证";
//            isTrue = false;
//            reportError(entry.getWx_sign(), "-4");
//        } else if ((validateBtn = Utils.findViewById(context, "com.tencent.mm:id/gs")) != null) {
//            txt = "参与内测";
//            Utils.clickCompone(validateBtn);
//            Utils.sleep(5000L);
//            if (Utils.obatinMsgDialogText(context, "有人正通过微信密码在")) {
//                txt = "有人正通过微信密码在";
//                AccessibilityNodeInfo okBtn = Utils.findViewByText(context, Button.class.getName(), "忽略");
//                if (okBtn != null) {
//                    Utils.clickCompone(okBtn);
//                    Utils.sleep(3000L);
//                }
//                okBtn = Utils.findViewByText(context, Button.class.getName(), "忽略");
//                if (okBtn != null) {
//                    Utils.clickCompone(okBtn);
//                    Utils.sleep(3000L);
//                }
//            }
//        } else if ((validateBtn = Utils.findViewByText(context, TextView.class.getName(), "暂不验证")) != null) {
//            validateBtn.recycle();
//            txt = "马上验证手机";
//            Utils.pressBack(context);
//            Utils.sleep(3000L);
//        } else if (Utils.obatinMsgDialogText(context, "你操作频率过快，请稍后重试")) {
//            txt = "你操作频率过快，请稍后重试";
//            AccessibilityNodeInfo okBtn = Utils.findViewByText(context, Button.class.getName(), "确定");
//            if (okBtn != null) {
//                Utils.clickCompone(okBtn);
//                Utils.sleep(3000L);
//            }
//            isTrue = false;
//        }else
//
//        if (Utils.isTragetActivity(Constants.QQ_LOGIN_ACTIVITY)) {
//            LogUtils.logInfo("  在登陆界面  ");
//            if ((validateBtn = Utils.findViewByText(context, Button.class.getName(), "忘记密码？")) == null) {
//                if ((closeBtn = Utils.findViewByText(context, Button.class.getName(), Constants.QQ_LOGIN_BTN)) != null) {
//                    Utils.clickCompone(closeBtn);
//                    Utils.sleep(3 * 1000L);
//                }
//            }
//            if (Utils.validateIsModify(context, entry)) {
//                LoginTask.login(context, entry);
//                isTrue = validateLogin(context, entry.getWx_sign());
//            } else {
//                //TODO还原不成功,刷新xposed
//                LogUtils.logInfo("XPOSED失效了");
//                Utils.rebootSystem(context);
//                Utils.sleep(3 * 1000L);
//            }
//        }else

        if (Utils.findViewByTextMatch(context, "身份过期") != null) {
            txt = "你的帐户信息已过期";
            Utils.sleep(1000);
            isTrue = loginOper(context, entry,0);
        } else if ((Utils.findListBtn(context, "手机空间不足")) != null) {
            txt = "手机空间严重不足";
            AccessibilityNodeInfo cancel = Utils.findViewByText(context, TextView.class.getName(), "取消");
            AccessibilityNodeInfo cancelBtn = Utils.findViewByDesc(context, "取消按钮");
            if (cancel != null) {
                Utils.clickCompone(cancel);
                Utils.sleep(2 * 1000L);
            } else if (cancelBtn != null) {
                Utils.clickCompone(cancelBtn);
                Utils.sleep(2 * 1000L);
            }
        } else if ((Utils.findViewByTextMatch(context, "帐号身份已过期，请重新登录。")) != null) {
            txt = "登录过期，请重新登录";
            Utils.sleep(1000);
            isTrue = loginOper(context, entry,0);
        }  else if (Utils.findViewByText(context, "设备存储已满，请释放空间，否则可能造成程序意外终止。") != null) {
            if ((validateBtn = Utils.findViewByText(context, Button.class.getName(), "取消")) != null) {
                Utils.clickCompone(validateBtn);
                Utils.sleep(2000);
            }
        } else if (Utils.findViewByTextMatch(context, "开启消息推送") != null) {
            txt = "开启消息推送";
            if ((validateBtn = Utils.findViewByDesc(context, "已关闭")) != null) {
                Utils.clickCompone(validateBtn);
                Utils.sleep(2000L);
            } else {
                Utils.pressBack(context);
                Utils.sleep(2000L);
            }
        } else if (Utils.obatinMsgDialogText(context, "该帐号涉嫌业务违规操作")) {
            txt = "该帐号涉嫌业务违规操作（如批量登录等）被暂时冻结";
            isTrue = false;
            reportError(entry.getWx_sign(), "-2");
        }  else if (Utils.obatinMsgDialogText(context, "该帐号涉嫌传播违法违规")) {
            txt = "该帐号涉嫌传播违法违规";
            isTrue = false;
            reportError(entry.getWx_sign(), "-2");
        }else if (Utils.obatinMsgDialogText(context, "为保障你的QQ帐号安全，当前设备需进行身份验证，通过后下次无需验证。")) {
            txt = "当前设备需进行身份验证";
            isTrue = false;
            reportError(entry.getWx_sign(), "-4");
        } else if (Utils.obatinMsgDialogText(context, "另一个设备上登陆")) {
            txt = "当前账号在另一个设备上登陆";
            if ((validateBtn = Utils.findViewByTextMatch(context, "重新登录")) != null) {
                Utils.clickCompone(validateBtn);
                Utils.sleep(2000L);
            }
        }else if (Utils.obatinMsgDialogText(context,"手机存储异常，请删除帐号重试。")){
            txt = "手机存储异常，请删除帐号重试。";
            isTrue = loginOper(context, entry,1);
        } else if (Utils.obatinMsgDialogText(context, "该帐号涉嫌业务违规注册")) {
            txt = "该帐号涉嫌业务违规注册";
            isTrue = false;
            reportError(entry.getWx_sign(), "-2");
        }
        LogUtils.logInfo(txt);
        return isTrue;
    }

    public static boolean validateLogin(HelperQQService context, String wsSign) {
        boolean isTrue = true;
        String txt = "";
        // com.tencent.mobileqq:id/dialogText
        if (Utils.obatinMsgDialogText(context, "该帐号被盗风险较高被暂时冻结")) {
            txt = "该帐号被盗风险较高被暂时冻结";
            isTrue = false;
            reportError(wsSign, "-8");
        } else if (Utils.obatinMsgDialogText(context, "帐号或密码错误")) {//帐号或密码错误，请重新输入。
            txt = "密码错误";
            isTrue = false;
            reportError(wsSign, "-1");
        }  else if (Utils.obatinMsgDialogText(context, "该帐号涉嫌业务违规操作（如批量登录等）被暂时冻结")) {
            txt = "该帐号涉嫌业务违规操作（如批量登录等）被暂时冻结";
            isTrue = false;
            reportError(wsSign, "-2");
        } else if (Utils.obatinMsgDialogText(context, "该帐号涉嫌传播违法违规")) {
            txt = "该帐号涉嫌传播违法违规";
            isTrue = false;
            reportError(wsSign, "-2");
        } else if (Utils.obatinMsgDialogText(context, "该帐号涉嫌业务违规注册")) {
            txt = "该帐号涉嫌业务违规注册";
            isTrue = false;
            reportError(wsSign, "-2");
        }

//        else if (Utils.obatinMsgDialogText(context, "该微信帐号因登录环境异常")) {
//            txt = "环境异常";
//            isTrue = false;
//            reportError(wsSign, "-2");
//        }
//        else if (Utils.obatinMsgDialogText(context, "你在新手机登录微信，我们需要验证你的身份")) {
//            txt = "你在新手机登录微信，我们需要验证你的身份";
//            isTrue = false;
//            reportError(wsSign, "-4");
//        } else if (Utils.obatinMsgDialogText(context, "你登录的微信需要进行好友验证")) {
//            txt = "需要进行好友验证";
//            isTrue = false;
//            reportError(wsSign, "-4");
//        } else if (Utils.obatinMsgDialogText(context, "你的微信绑定的手机号")) {
//            txt = "你的微信绑定的手机号";
//            isTrue = false;
//            reportError(wsSign, "-1");
//        } else if (Utils.findViewByText(context, Button.class.getName(), "通过短信验证身份") != null) {
//            txt = "需要进行好友验证";
//            isTrue = false;
//            reportError(wsSign, "-4");
//        } else if (Utils.findViewByText(context, TextView.class.getName(), "暂不验证") != null) {
//            txt = "验证手机号";
//            Utils.pressBack(context);
//            Utils.sleep(3000L);
//        } else if (Utils.obatinMsgDialogText(context, "该微信帐号因被投诉")) {
//            txt = "该微信帐号因被投诉并确定有违规行为被限制登录";
//            isTrue = false;
//            reportError(wsSign, "-8");
//        } else if (Utils.obatinMsgDialogText(context, "该微信帐号因被多人投诉")) {
//            txt = "该微信帐号因被多人投诉并确认有违规行为被限制登录";
//            isTrue = false;
//            reportError(wsSign, "-8");
//        } else if (Utils.obatinMsgDialogText(context, "长期没有登录，帐号已被回收")) {
//            txt = "长期没有登录，帐号已被回收";
//            isTrue = false;
//            reportError(wsSign, "-6");
//        } else if (Utils.obatinMsgDialogText(context, "该微信帐号因使用非官方微信客户端被永久限制登录")) {
//            txt = "该微信帐号因使用非官方微信客户端被永久限制登录";
//            isTrue = false;
//            reportError(wsSign, "-5");
//        } else if (Utils.obatinMsgDialogText(context, "看看手机通讯录里谁在使用微信")) {
//            txt = "看看手机通讯录里谁在使用微信";
//            AccessibilityNodeInfo noBtn = Utils.findViewByText(context, "否");
//            if (noBtn != null) {
//                Utils.clickCompone(noBtn);
//                Utils.sleep(20 * 1000L);
//            }
//        } else if (Utils.obatinMsgDialogText(context, "系统检测到帐号有被盗风险")) {
//            txt = "系统检测到帐号有被盗风险";
//            isTrue = false;
//            reportError(wsSign, "-9");
//        } else if (Utils.obatinMsgDialogText(context, "为保障你的QQ帐号安全，当前设备需进行身份验证，通过后下次无需验证。")) {
//            txt = "当前设备需进行身份验证";
//            isTrue = false;
//            reportError(wsSign, "-4");
//        } else if (Utils.obatinMsgDialogText(context, "该帐号涉嫌业务违规操作（如批量登录等）被暂时冻结")) {
//            txt = "该帐号涉嫌业务违规操作（如批量登录等）被暂时冻结";
//            isTrue = false;
//            reportError(wsSign, "-2");
//        } else if (Utils.obatinMsgDialogText(context, "帐号存储异常")) {
//            txt = "帐号存储异常";
//            isTrue = false;
//            reportError(wsSign, "-1");
//        }
        LogUtils.logInfo(txt);
        return isTrue;
    }

    public static boolean addAccountValidateLogin(HelperQQService context, String wsSign) {
        boolean isTrue = true;
        String txt = "";
        if (Utils.obatinMsgDialogText(context, "密码错误")) {
            txt = "密码错误";
            isTrue = false;
            reportError(wsSign, "-1");
        } else if (Utils.obatinMsgDialogText(context, "该微信帐号因登录环境异常")) {
            txt = "环境异常";
            isTrue = false;
            reportError(wsSign, "-2");
        } else if (Utils.obatinMsgDialogText(context, "你的微信绑定的手机号")) {
            txt = "你的微信绑定的手机号";
            isTrue = false;
            reportError(wsSign, "-1");
        } else if (Utils.obatinMsgDialogText(context, "该微信帐号因被投诉")) {
            txt = "该微信帐号因被投诉并确定有违规行为被限制登录";
            isTrue = false;
            reportError(wsSign, "-8");
        } else if (Utils.obatinMsgDialogText(context, "该微信帐号因被多人投诉")) {
            txt = "该微信帐号因被多人投诉并确认有违规行为被限制登录";
            isTrue = false;
            reportError(wsSign, "-8");
        } else if (Utils.obatinMsgDialogText(context, "长期没有登录，帐号已被回收")) {
            txt = "长期没有登录，帐号已被回收";
            isTrue = false;
            reportError(wsSign, "-6");
        } else if (Utils.obatinMsgDialogText(context, "该微信帐号因使用非官方微信客户端被永久限制登录")) {
            txt = "该微信帐号因使用非官方微信客户端被永久限制登录";
            isTrue = false;
            reportError(wsSign, "-5");
        } else if (Utils.obatinMsgDialogText(context, "看看手机通讯录里谁在使用微信")) {
            txt = "看看手机通讯录里谁在使用微信";
            AccessibilityNodeInfo noBtn = Utils.findViewByText(context, "否");
            if (noBtn != null) {
                Utils.clickCompone(noBtn);
                Utils.sleep(20 * 1000L);
            }
        }
        LogUtils.logInfo(txt);
        return isTrue;
    }

    private static boolean loginOper(HelperQQService context, TaskEntry entry,int type) {
        boolean isTrue = false;
        AccessibilityNodeInfo okBtn;
        if ((okBtn = Utils.findViewByText(context, "确定")) != null) {
            Utils.clickComponeByXY(okBtn);
        } else if ((okBtn = Utils.findViewByText(context, "是")) != null) {
            Utils.clickComponeByXY(okBtn);
        }
        Utils.sleep(10 * 1000L);
        if (Utils.validateIsModify(context, entry)) {
            LoginTask.login(context, entry,type);
            isTrue = validateLogin(context, entry.getWx_sign());
        } else {
            //TODO还原不成功,刷新xposed
            LogUtils.logInfo("XPOSED失效了");
            Utils.rebootSystem(context);
        }
        return isTrue;
    }

    public static void reportError(String wxSign, String errorCode) {
        HttpHandler.requestAccountError(wxSign, errorCode, new HttpTask.HttpCallback() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(String errMsg) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /***
     * 检测各种设置页面
     * @param context
     */
    public static void loginPromptAcitvity(HelperQQService context) {
        LogUtils.logInfo("检测各种设置页面");
        String txt = "  无提示  ";
        AccessibilityNodeInfo closeBtn = null;
        AccessibilityNodeInfo validateBtn = null;
        if ((Utils.findViewByText(context, "主题装扮")) != null) {
            txt = "主题装扮";
            Utils.sleep(1000);
            if ((closeBtn = Utils.findViewByTextMatch(context, "关闭")) != null) {
                Utils.clickCompone(closeBtn);
                Utils.sleep(2 * 1000L);
            }
            if ((closeBtn = Utils.findViewByTextMatch(context, "关闭")) != null) {
                Utils.clickCompone(closeBtn);
                Utils.sleep(2 * 1000L);
            }
        }
        AccessibilityNodeInfo web = Utils.findViewByType(context, WebView.class.getName());//com.tencent.mobileqq:id/webview
        if (web != null) {
            txt = "新手任务";
            if ((validateBtn = Utils.findViewByText(context, TextView.class.getName(), "跳过")) != null) {
                Utils.clickCompone(validateBtn);
                Utils.sleep(1000);
                if (Utils.findListBtn(context, "跳过新手任务，将错失加速QQ等级的机会。") != null) {
                    Utils.sleep(1000);
                    if ((closeBtn = Utils.findViewByTextMatch(context, "关闭")) != null) {
                        Utils.clickCompone(closeBtn);
                        Utils.sleep(2 * 1000L);
                    } else if ((closeBtn = Utils.findViewByTextMatch(context, "跳过")) != null) {
                        Utils.clickCompone(closeBtn);
                        Utils.sleep(2 * 1000L);
                    }
                }
            }
        }
        if ((Utils.findViewByText(context, "绑定手机号码")) != null) {
            txt = "绑定手机号码";
            if ((closeBtn = Utils.findViewByText(context, "关闭")) != null) {
                Utils.clickCompone(closeBtn);
                Utils.sleep(5 * 1000L);
                if (Utils.findListBtn(context, "确定关闭吗？") != null) {
                    LogUtils.logError("txt = \"确定关闭吗\";");
                    Utils.sleep(1000);
                    AccessibilityNodeInfo close = Utils.findViewByTextMatch(context, "关闭");//com.tencent.mobileqq:id/dialogRightBtn
                    LogUtils.logError("  文本 ");
                    Utils.sleep(1000);
                    AccessibilityNodeInfo rightBtn = Utils.findViewById(context, "com.tencent.mobileqq:id/dialogRightBtn");
                    LogUtils.logError("  id  ");
                    Utils.sleep(1000);
                    AccessibilityNodeInfo close_btn = Utils.findViewByDesc(context, "关闭按钮");
                    LogUtils.logError("  描述文本  ");

                    if (close != null) {
                        LogUtils.logError("  找到文本");
                        Utils.clickComponeByXY(close);
                        Utils.sleep(2 * 1000L);
                    } else if (rightBtn != null) {
                        LogUtils.logError("  找到控件");
                        Utils.clickComponeByXY(rightBtn);
                        Utils.sleep(2 * 1000L);
                    } else if (close_btn != null) {
                        LogUtils.logError("  找到描述文本");
                        Utils.clickComponeByXY(rightBtn);
                        Utils.sleep(2 * 1000L);
                    }
                }
            } else if ((closeBtn = Utils.findViewByText(context, "跳过")) != null) {
                txt = " ####  绑定手机号码  ##### ";
                Utils.clickCompone(closeBtn);
                Utils.sleep(3 * 1000L);
                if (Utils.findListBtn(context, "绑定手机号码能帮助你找到手机通讯录中的好友，且提高QQ帐号安全哦。") != null) {
                    LogUtils.logError("  绑定手机号码能帮助你找到手机通讯录中的好友  ");
                    Utils.sleep(1000);
                    AccessibilityNodeInfo close = Utils.findViewByText(context, "跳过");
                    Utils.sleep(1000);
                    AccessibilityNodeInfo rightBtn = Utils.findViewById(context, "com.tencent.mobileqq:id/dialogRightBtn");
                    Utils.sleep(1000);
                    AccessibilityNodeInfo close_btn = Utils.findViewByDesc(context, "跳过按钮");
                    if (close != null) {
                        Utils.clickCompone(close);
                        Utils.sleep(2 * 1000L);
                    } else if (rightBtn != null) {
                        Utils.clickCompone(rightBtn);
                        Utils.sleep(2 * 1000L);
                    } else if (close_btn != null) {
                        Utils.clickCompone(rightBtn);
                        Utils.sleep(2 * 1000L);
                    }
                }
            }
        }
        if (Utils.findViewByTextMatch(context, "开启消息推送") != null) {
            txt = "开启消息推送";
            Utils.clickCompone(Utils.findViewByDesc(context, "已关闭"));
            Utils.sleep(2000L);
        }
        AccessibilityNodeInfo info = Utils.findViewByText(context, "你的帐号价值较高，为保障你的帐号与财产安全，建议你开启设备锁保护");
        if (info != null) {
            AccessibilityNodeInfo backTxt = Utils.findViewByText(context, "取消");
            if (backTxt != null) {
                Utils.clickCompone(backTxt);
                Utils.sleep(3000L);
            } else {
                Utils.pressBack(context);
                Utils.sleep(3000L);
            }
        }

        LogUtils.logError(txt);
    }

    private void notifi(HelperQQService context){
        AccessibilityNodeInfo validateBtn = null;
        String txt="";
        if ((validateBtn = Utils.findViewByText(context, "禁止")) != null) {
            txt = "禁止授权";
            Utils.clickCompone(validateBtn);
            Utils.sleep(3 * 1000L);
        }
    }

}
