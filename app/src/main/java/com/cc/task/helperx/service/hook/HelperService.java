package com.cc.task.helperx.service.hook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.HookUtils;
import com.cc.task.helperx.utils.KeyWordUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 *
 * Created by Administrator on 2017/8/23.
 */

public class HelperService implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log(" hook进入 ");
//        LogUtils.i(" hook进入 ");
        hookSnak(loadPackageParam);
        hookPhoneInfo(loadPackageParam);
        HookUtils.HookAndChange(loadPackageParam.classLoader);
        if (loadPackageParam.packageName.equals(Constants.APP_NAME)) {
            XposedBridge.log("hook当前类：" + loadPackageParam.appInfo.className);
            discussionMemberInfo(loadPackageParam);//成员信息
            //openTroopAction(loadPackageParam);//打开群的intent
            chatFrahmentGetIntente(loadPackageParam);
        }

//        businessBrowse(loadPackageParam);
    }

    private void hookIntent(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Bundle b;
        XposedHelpers.findAndHookMethod("android.os.Bundle", loadPackageParam.classLoader, "putString", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object k = param.args[0];
                Object v = param.args[1];
                if (k instanceof String) {
                    String kText = (String) k;
                    LogUtils.logInfo("kText=" + kText);
                }
                if (v instanceof String) {
                    String vText = (String) v;
                    LogUtils.logInfo("vText=" + vText);
                }
            }
        });
    }

    private void hookSnak(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) throws Throwable {
        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("android.hardware.SystemSensorManager$SensorEventQueue", paramLoadPackageParam.classLoader), "dispatchSensorEvent", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) throws Throwable {
                    Boolean isShake = false;
                    Object obj = Utils.getProperty("shnak");
                    if (obj != null) {
                        isShake = Boolean.valueOf(obj.toString());
                    }

                    if (isShake) {
                        ((float[]) paramAnonymousMethodHookParam.args[1])[0] = (Utils.getRandom().nextFloat() * 1200.0F + 125.0F);
                        Utils.setProperty("shnak", false);
                    }
                }
            });
        } catch (Throwable e) {
            throw e;
        }
    }

    private void hookPhoneInfo(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) throws Throwable {
        try {
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", paramLoadPackageParam.classLoader, "getDeviceId", new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
//                    LogUtils.e(" taskEntry = " + obj);
                    if (obj != null) {
                        TaskEntry taskEntry = (TaskEntry) obj;
//                        LogUtils.i(" askEntry.getImei()   " + taskEntry.getImei());
                        param.setResult(taskEntry.getImei());
                    }
                }
            });

            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", paramLoadPackageParam.classLoader, "getSubscriberId", new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
                    if (obj != null) {
                        TaskEntry taskEntry = (TaskEntry) obj;
                        param.setResult(taskEntry.getSid());
                    }
                }
            });

            XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", paramLoadPackageParam.classLoader, "getMacAddress", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
                    if (obj != null) {
                        TaskEntry taskEntry = (TaskEntry) obj;
                        param.setResult(taskEntry.getMac());
                    }
                }
            });

        } catch (Throwable e) {
            throw e;
        }
    }

//    private void hookTroopMemberList(XC_LoadPackage.LoadPackageParam loadPackageParam) {
//
//        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.activity.TroopMemberListActivity$ListAdapter", loadPackageParam.classLoader, "a", new XC_MethodHook() {
//
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                ContentValues cv = (ContentValues) param.args[2];
//                XposedBridge.log(" --------" + cv.toString());
//                List<Object> listObj = (List<Object>) getObjectField(param.thisObject, "fHE");
//                XposedBridge.log(" --------" + listObj.size());
//
//            }
//        });
//
//
//        //com.tencent.mobileqq.activity.TroopMemberListActivity$ListAdapter
//        XposedBridge.hookAllConstructors(XposedHelpers.findClass("com.tencent.mobileqq.activity.TroopMemberListActivity$ListAdapter", loadPackageParam.classLoader),
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        Class self = param.getClass();
//                        XposedBridge.log(" --------" + self.getSimpleName());
//
//                        Method[] mothod = self.getMethods();
//                        XposedBridge.log(" --------" + mothod.length);
///*                        // 获取对象的所有属性
//                        Field[] fields = entityClass.getDeclaredFields();
//
//                        // 获取对象的所有方法
//                        Method[] methods = entityClass.getDeclaredMethods();
//                        List<String> listfeld = new ArrayList<>();
//                        for (Field field : fields) {
//                            System.out.println("属性名称有：" + field.getName());
//
//                        }
//                        for (Method method : methods) {
//                            System.out.println("方法名称有：" + method.getName());
//                        }*/
//                    }
//                }
//        );
//    }

    private void discussionMemberInfo(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        Class<?> TroopMemberInfo = XposedHelpers.findClass("com.tencent.mobileqq.data.TroopMemberInfo", loadPackageParam.classLoader);//loadPackageParam.classLoader.loadClass("com.tencent.mobileqq.data.TroopMemberInfo");
        Class<?> FriendsManager1 = XposedHelpers.findClass("com.tencent.mobileqq.app.FriendsManager", loadPackageParam.classLoader);//; loadPackageParam.classLoader.loadClass("com.tencent.mobileqq.app.FriendsManager");
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.activity.TroopMemberListActivity", loadPackageParam.classLoader, "a", TroopMemberInfo, FriendsManager1, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                        /* active_point.setAccessible(true);// 设置操作权限为true
                        for (Field field : fs) {
                        field.setAccessible(true);// 设置操作权限为true
                        sb = sb + field.getName() + ":" + field.get(o) + "  ";
                    } */
//                LogUtils.e("开始hook");
                Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
                TaskEntry taskEntry = null;
                if (obj != null) {
                    taskEntry = (TaskEntry) obj;
                }
                Object o = param.args[0];
                if (o != null) {
                    Class cls = o.getClass();
                    //  Field[] fs = cls.getDeclaredFields();
                    StringBuffer sb = new StringBuffer();

                    Field friendnick = cls.getDeclaredField("friendnick");
                    friendnick.setAccessible(true);
                    String memberNick = friendnick.get(o) + "";
                    if (!KeyWordUtils.ContainKeyWord(memberNick)) {

                        sb.append("{");
                        sb.append("\"" + friendnick.getName() + "\"" + ":" + "\"" + Utils.getBASE64(memberNick) + "\",");

                        Field memberuin = cls.getDeclaredField("memberuin");
                        memberuin.setAccessible(true);
                        sb.append("\"" + memberuin.getName() + "\"" + ":" + "\"" + memberuin.get(o) + "\",");

                        Field troopuin = cls.getDeclaredField("troopuin");
                        troopuin.setAccessible(true);
                        sb.append("\"" + troopuin.getName() + "\"" + ":" + "\"" + troopuin.get(o) + "\",");

                        Field troopnick = cls.getDeclaredField("troopnick");
                        troopnick.setAccessible(true);
                        sb.append("\"" + troopnick.getName() + "\"" + ":" + "\"" + Utils.getBASE64(troopnick.get(o) + "") + "\",");

                        Field sex = cls.getDeclaredField("sex");
                        sex.setAccessible(true);
                        sb.append("\"" + sex.getName() + "\"" + ":" + "\"" + sex.get(o) + "\",");

                        Field age = cls.getDeclaredField("age");
                        age.setAccessible(true);
                        sb.append("\"" + age.getName() + "\"" + ":" + "\"" + age.get(o) + "\"");

                        sb.append("},");
                        FileUtils.writeFileToSDCard(sb.toString(), "GroupMembers", taskEntry.getWx_sign() + ".txt", true, true);

//                        FileUtils.writeFileToSDCard(sb.toString(), "GroupMembers",  "hsuds.txt", true, true);
                    }
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                LogUtils.e("结束hook");
            }
        });
    }

    private void openTroopAction(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.activity.TroopMemberListActivity", loadPackageParam.classLoader, "a", Context.class, String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object o = param.args[1];
                Object obj = param.args[2];
                if (o != null) {
                    String paramString = (String) o;
                    //  FileUtils.writeFileToSDCard("Intent-String:"+paramString, "/sdcard/Financail","Exception_Log.txt",true,true);
                }
                if (obj != null) {
                    int paramInt = (int) obj;
                    // FileUtils.writeFileToSDCard("Intent-int:"+paramInt, "/sdcard/Financail","Exception_Log.txt",true,true);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }

    private void chatFrahmentGetIntente(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.activity.ChatFragment", loadPackageParam.classLoader, "a",
                Intent.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
//                        LogUtils.e("开始hook");
                        XposedBridge.log("开始hook");
                        Object o = param.args[0];
                        if (o != null) {
                            Intent paramIntent = (Intent) o;
                            boolean bool1 = paramIntent.getBooleanExtra("open_chatfragment_fromphoto", false);
                            String str1 = paramIntent.getStringExtra("uin");
                            int uintype = paramIntent.getIntExtra("uintype", -1);
                            int actionid = paramIntent.getExtras().getInt("apollo_actionid_key");
                            int pkgid = paramIntent.getExtras().getInt("apollo_pkgid_key");
                            String chat_subType = paramIntent.getStringExtra("chat_subType");
                            String bool2 = paramIntent.getStringExtra("isGameRoom");
                            boolean isFromMainTab = paramIntent.getBooleanExtra("isFromMainTab", false);
                            boolean open_chatfragment = paramIntent.getBooleanExtra("open_chatfragment", false);
                            String PREVIOUS_WINDOW = paramIntent.getStringExtra("PREVIOUS_WINDOW");
                            String PREVIOUS_UIN = paramIntent.getStringExtra("PREVIOUS_UIN");
                            String uinname = paramIntent.getStringExtra("uinname");
                            String troop_code = paramIntent.getStringExtra("troop_code");
                            String troop_uin = paramIntent.getStringExtra("troop_uin");
                            String troop_name = paramIntent.getStringExtra("troop_name");
                            int cSpecialFlag = paramIntent.getIntExtra("cSpecialFlag", 100);
                            String at_member_uin = paramIntent.getStringExtra("at_member_uin");
                            String at_member_name = paramIntent.getStringExtra("at_member_name");
                            int at_member_source = paramIntent.getIntExtra("at_member_source", 500);
                            LogUtils.e("开始hook  AAAAAA   " + uinname + "@@@@@"
                                    + troop_code + "@@@@@"
                                    + troop_uin + "@@@@@"
                                    + troop_name + "  ******  ");
//                            SharePreferencesUtils.getInstance().setGroupParameter(uinname + "@@@@@"
//                                    + troop_code + "@@@@@"
//                                    + troop_uin + "@@@@@"
//                                    + troop_name);

                            StringBuffer sb = new StringBuffer();
//                            sb.append("{");
//                            sb.append("\"" + friendnick.getName() + "\"" + ":" + "\"" + Utils.getBASE64(memberNick) + "\",");
//                            FileUtils.writeFileToSDCardUin(
//                                    "chatFrahmentGetIntente-[open_chatfragment_fromphoto :" + bool1
//                                            + "]\n[uin-:" + str1
//                                            + "]\n[uintype-:" + uintype
//                                            + "]\n[actionid:" + actionid
//                                            + "]\n[pkgid:" + pkgid
//                                            + "]\n[chat_subType:" + chat_subType
//                                            + "]\n[bool2" + bool2
//                                            + "]\n[isFromMainTab:" + isFromMainTab
//                                            + "]\n[open_chatfragment:" + open_chatfragment
//                                            + "]\n[PREVIOUS_WINDOW:" + PREVIOUS_WINDOW
//                                            + "]\n[PREVIOUS_UIN:" + PREVIOUS_UIN
//                                            + "]\n[uinname:" + uinname
//                                            + "]\n[troop_code:" + troop_code
//                                            + "]\n[troop_uin:" + troop_uin
//                                            + "]\n[troop_name:" + troop_name
//                                    uinname + "@@@@@"
//                                            + troop_code + "@@@@@"
//                                            + troop_uin + "@@@@@"
//                                            + troop_name
//                                            + "]\n[cSpecialFlag:" + cSpecialFlag
//                                            + "]\n[at_member_uin:" + at_member_uin
//                                            + "]\n[at_member_name:" + at_member_name
//                                            + "]\n[at_member_source:" + at_member_source
//                                    ,
//                                    "HookDemo",
//                                    "getIntent1",
//                                    true,
//                                    false);

                            LogUtils.e(" AAAAAAAAAA  [uin-:" + str1
                                    + "][uintype-:" + uintype
                                    + "][actionid:" + actionid
                                    + "][pkgid:" + pkgid
                                    + "][chat_subType:" + chat_subType
                                    + "][bool2" + bool2
                                    + "][isFromMainTab:" + isFromMainTab
                                    + "][open_chatfragment:" + open_chatfragment
                                    + "][PREVIOUS_WINDOW:" + PREVIOUS_WINDOW
                                    + "][PREVIOUS_UIN:" + PREVIOUS_UIN
                                    + "][uinname:" + uinname
                                    + "][troop_code:" + troop_code
                                    + "][troop_uin:" + troop_uin
                                    + "][troop_name:" + troop_name
                                    + "][cSpecialFlag:" + cSpecialFlag
                                    + "][at_member_uin:" + at_member_uin
                                    + "][at_member_name:" + at_member_name
                                    + "][at_member_source:" + at_member_source);
                        }

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
//                        LogUtils.e("结束hook");
//                        XposedBridge.log("结束hook");
//                        Object o = param.thisObject;
//                        if (o != null) {
//                            boolean bool1 = (boolean) o;
//                            FileUtils.writeFileToSDCard("chatFrahmentGetIntente-结束之后返回参数 :" + bool1, "HookDemo", "getIntent1", false, false);
//                        }

                    }
                });


        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.activity.ChatFragment", loadPackageParam.classLoader, "b",
                Intent.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        LogUtils.e("开始hook");
                        XposedBridge.log("开始hook");
                        Object o = param.args[0];

                        if (o != null) {
                            Intent paramIntent = (Intent) o;
                            boolean bool1 = paramIntent.getBooleanExtra("open_chatfragment_fromphoto", false);
                            String str1 = paramIntent.getStringExtra("uin");
                            int uintype = paramIntent.getIntExtra("uintype", -1);
                            int actionid = paramIntent.getExtras().getInt("apollo_actionid_key");
                            int pkgid = paramIntent.getExtras().getInt("apollo_pkgid_key");
                            String chat_subType = paramIntent.getStringExtra("chat_subType");
                            boolean bool2 = paramIntent.getBooleanExtra("isGameRoom", false);
                            boolean isFromMainTab = paramIntent.getBooleanExtra("isFromMainTab", false);
                            boolean open_chatfragment = paramIntent.getBooleanExtra("open_chatfragment", false);
                            String PREVIOUS_WINDOW = paramIntent.getStringExtra("PREVIOUS_WINDOW");
                            String PREVIOUS_UIN = paramIntent.getStringExtra("PREVIOUS_UIN");
                            String uinname = paramIntent.getStringExtra("uinname");
                            String troop_code = paramIntent.getStringExtra("troop_code");
                            String troop_uin = paramIntent.getStringExtra("troop_uin");
                            String troop_name = paramIntent.getStringExtra("troop_name");
                            int cSpecialFlag = paramIntent.getIntExtra("cSpecialFlag", 100);
                            String at_member_uin = paramIntent.getStringExtra("at_member_uin");
                            String at_member_name = paramIntent.getStringExtra("at_member_name");
                            int at_member_source = paramIntent.getIntExtra("at_member_source", 500);
                            LogUtils.e("开始hook BBBBBBB " + uinname + "@@@@@"
                                    + troop_code + "@@@@@"
                                    + troop_uin + "@@@@@"
                                    + troop_name + "  ******  ");

                            FileUtils.writeFileToSDCardUin(
//                                    "chatFrahmentGetIntente-[open_chatfragment_fromphoto :"+bool1
//                                            +"]\n[uin-:"+str1
//                                            +"]\n[uintype-:"+uintype
//                                            +"]\n[actionid:"+actionid
//                                            +"]\n[pkgid:"+pkgid
//                                            +"]\n[chat_subType:"+chat_subType
//                                            +"]\n[bool2"+bool2
//                                            +"]\n[isFromMainTab:"+isFromMainTab
//                                            +"]\n[open_chatfragment:"+open_chatfragment
//                                            +"]\n[PREVIOUS_WINDOW:"+PREVIOUS_WINDOW
//                                            +"]\n[PREVIOUS_UIN:"+PREVIOUS_UIN
//                                            +"]\n[uinname:"+uinname
//                                            +"]\n[troop_code:"+troop_code
//                                            +"]\n[troop_uin:"+troop_uin
//                                            +"]\n[troop_name:"+troop_name
//                                            +"]\n[cSpecialFlag:"+cSpecialFlag
//                                            +"]\n[at_member_uin:"+at_member_uin
//                                            +"]\n[at_member_name:"+at_member_name
//                                            +"]\n[at_member_source:"+at_member_source

                                    uinname + "@@@@@"
                                            + troop_code + "@@@@@"
                                            + troop_uin + "@@@@@"
                                            + troop_name
                                    ,
                                    "HookDemo",
                                    "getIntent2",
                                    false,
                                    false);


                            LogUtils.e(" BBBBBBBBBB  [uin-:" + str1
                                    + "][uintype-:" + uintype
                                    + "][actionid:" + actionid
                                    + "][pkgid:" + pkgid
                                    + "][chat_subType:" + chat_subType
                                    + "][bool2" + bool2
                                    + "][isFromMainTab:" + isFromMainTab
                                    + "][open_chatfragment:" + open_chatfragment
                                    + "][PREVIOUS_WINDOW:" + PREVIOUS_WINDOW
                                    + "][PREVIOUS_UIN:" + PREVIOUS_UIN
                                    + "][uinname:" + uinname
                                    + "][troop_code:" + troop_code
                                    + "][troop_uin:" + troop_uin
                                    + "][troop_name:" + troop_name
                                    + "][cSpecialFlag:" + cSpecialFlag
                                    + "][at_member_uin:" + at_member_uin
                                    + "][at_member_name:" + at_member_name
                                    + "][at_member_source:" + at_member_source);
                        }


                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        LogUtils.e("结束hook");
                        XposedBridge.log("结束hook");
                    }
                });
    }

    private void businessBrowse(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        Class<?> paramWebViewClass = XposedHelpers.findClass("com.tencent.smtt.sdk.WebView", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.webview.ui.tools.WebViewUI.i", loadPackageParam.classLoader, "shouldOverrideUrlLoading", paramWebViewClass, String.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                LogUtils.i("  微信你 ");
                LogUtils.e("shouldOverrideUrlLoading:开始");
                Object o = param.args[1];
                if (o != null) {
                    String paramString = (String) o;
                    LogUtils.e("shouldOverrideUrlLoading值:" + paramString);
                    FileUtils.writeFileToSDCard("shouldOverrideUrlLoading值:" + paramString, "GroupMembers", "shouldOverrideUrlLoading" + ".txt", true, true);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtils.e("结束hook:" + param.getResult());
                FileUtils.writeFileToSDCard("结束hook:" + param.getResult(), "GroupMembers", "shouldOverrideUrlLoading" + ".txt", true, true);
            }
        });

        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.webview.ui.tools.WebViewUI.l", loadPackageParam.classLoader, " Fj", String.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                LogUtils.e("shouldOverrideUrlLoading:开始");
                Object o = param.args[0];
                if (o != null) {
                    String paramString = (String) o;
                    Uri localUri = Uri.parse(paramString);
                    String str1 = localUri.getQueryParameter("apKey");
                    String str2 = localUri.getQueryParameter("ticket");
                    LogUtils.e("Fj值:" + paramString);
                    FileUtils.writeFileToSDCard("str1:" + str1 + "str1:" + str2, "GroupMembers", "Fj", true, true);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtils.e("结束hook:" + param.getResult());
                FileUtils.writeFileToSDCard("结束hook:" + param.getResult(), "GroupMembers", "Fj", true, true);
            }
        });


        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.webview.ui.tools.WebViewUI.i", loadPackageParam.classLoader, "shouldInterceptRequest", paramWebViewClass, String.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                LogUtils.e("shouldInterceptRequest:开始");
                Object o = param.args[1];
                if (o != null) {
                    String paramString = (String) o;
                    LogUtils.e("shouldInterceptRequest值:" + paramString);
                    FileUtils.writeFileToSDCard("shouldInterceptRequest值:" + paramString, "GroupMembers", "shouldInterceptRequest" + ".txt", true, true);
                }

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtils.e("shouldInterceptRequest结束:" + param.getResult());
                FileUtils.writeFileToSDCard("shouldInterceptRequest值:" + param.getResult(), "GroupMembers", "shouldInterceptRequest" + ".txt", true, true);
            }
        });

        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.webview.ui.tools.WebViewUI.i", loadPackageParam.classLoader, "shouldOverrideUrlLoading", paramWebViewClass, String.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                LogUtils.e("shouldOverrideUrlLoading:开始");
                Object o = param.args[1];
                if (o != null) {
                    String paramString = (String) o;
                    LogUtils.e("shouldOverrideUrlLoading值:" + paramString);
                    FileUtils.writeFileToSDCard("shouldOverrideUrlLoading值:" + paramString, "GroupMembers", "shouldOverrideUrlLoading+" + ".txt", true, true);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtils.e("shouldOverrideUrlLoading结束:" + param.getResult());
                FileUtils.writeFileToSDCard("shouldOverrideUrlLoading结束:" + param.getResult(), "GroupMembers", "shouldOverrideUrlLoading+" + ".txt", true, true);
            }
        });
    }

}
