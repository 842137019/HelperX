package com.cc.task.helperx.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CheckBox;

import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.service.TaskControllerService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;


/**
 *
 *  G:\QQ\de.robv.android.xposed.installer_v33_36570c.apk
 *  adb  uninstall com.cc.helperqq
 * Created by yzq on 2017/1/5.
 */

public class Utils {

    private static Random random = new Random();
    private static Gson gson;
    private static MediaScannerConnection connection;

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private static ClipboardManager clipboardManager;
    private static TelephonyManager telephonyManager;

    /**
     * 給輸入框填充内容
     *
     * @param service
     * @param node
     * @param content
     */
    public static void inputText(Context service, AccessibilityNodeInfo node, String content) {
        if (node == null) {
            return;
        }
     /*   initClipManager(service);
        clipboardManager.setText(content);
        node.performAction(AccessibilityNodeInfo.ACTION_FOCUS); // 获取焦点
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE); // 执行粘贴*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(
                    AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content);
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(content);
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }

    public static void initClipManager(Context service) {
        if (clipboardManager == null) {
            clipboardManager = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
        }
    }

    public static String getDeviceId(Context service) {
        initTelphoneManager(service);
        return telephonyManager.getDeviceId();
    }

    public static String getSId(Context service) {
        initTelphoneManager(service);
        return telephonyManager.getSubscriberId();
    }

    public static String getSIM(Context service) {
        initTelphoneManager(service);
        return telephonyManager.getSimOperator();
    }

    public static void initTelphoneManager(Context service) {
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) service.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    /**
     * 让控件获取
     *
     * @param node
     */
    public static void componeFocus(AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        node.performAction(AccessibilityNodeInfo.ACTION_FOCUS); // 获取焦点
    }

    /**
     * 选择文本
     *
     * @param info 操作的输入框对象
     */
    public static void selectAllText(AccessibilityNodeInfo info) {
        if (info == null) {
            return;
        }

        int endIndex;
        CharSequence text = info.getText();
        if (TextUtils.isEmpty(text) || text.length() <= 0) {
            return;
        } else {
            endIndex = text.length();
        }

        Bundle arguments = new Bundle();
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, endIndex);
        info.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
    }

    /**
     * 给当前获取焦点的输入框直接输入文本
     *
     * @param content
     */
    public static void inputText(String content) {
        ShellUtils.execCommand("input text " + content, true);
    }

    /**
     * 判斷當前activity是否是指定的activity
     *
     * @param activityName
     * @return
     */
    public static boolean isTragetActivity(String activityName) {
        if (TextUtils.isEmpty(activityName)) {
            return false;
        }
        ShellUtils.CommandResult result = ShellUtils.execCommand("dumpsys activity | grep 'mFocusedActivity'", true);
        if (result.successMsg.contains(activityName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳轉頁面
     *
     * @param activityName activity的全名（package名称/.activity名称）
     */
    public static void startPage(String activityName) {
        ShellUtils.execCommand("am start -n " + activityName, true);
    }

    public static void pressBack(HelperQQService service) {
        LogUtils.logInfo("按下返回键");
        if (!service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)) {
            pressKey(KeyEvent.KEYCODE_BACK);
        }
    }

    public static void pressKey(int key) {
        ShellUtils.execCommand("input keyevent " + key, true);
    }

    /**
     * 讓當前綫程等待(單位：毫秒)
     *
     * @param time
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 單擊當前控件
     *
     * @param nodeInfo
     */
    public static boolean clickCompone(AccessibilityNodeInfo nodeInfo) {
        boolean isTrue = false;
        if (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                isTrue = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                nodeInfo.recycle();
            } else {
                AccessibilityNodeInfo parent = nodeInfo.getParent();
                nodeInfo.recycle();
                isTrue = clickCompone(parent);
            }
        }
        return isTrue;
    }

    public static void clickComponeNoRecycle(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                AccessibilityNodeInfo parent = nodeInfo.getParent();
                clickCompone(parent);
            }
        }
    }

    /**
     * 长按當前控件
     *
     * @param nodeInfo
     */
    public static void longClickCompone(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                nodeInfo.recycle();
            } else {
                AccessibilityNodeInfo parent = nodeInfo.getParent();
                nodeInfo.recycle();
                longClickCompone(parent);
            }
        }
    }

    /**
     * 單擊當前控件
     *
     * @param nodeInfo
     */
    public static void clickComponeByXY(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            Rect outBount = new Rect();
            nodeInfo.getBoundsInScreen(outBount);
            ShellUtils.execCommand("input tap " + outBount.centerX() + " " + outBount.centerY(), true);
            nodeInfo.recycle();
        }
    }

    /**
     * 單擊當前控件
     */
    public static void scrollScreenXY(String xy) {
        ShellUtils.execCommand("input swipe " + xy, true);
    }

    public static void tapScreenXY(String xy) {
        ShellUtils.execCommand("input tap " + xy, true);
    }

    /**
     * 根據類型查找控件
     *
     * @param service
     * @param widget
     * @return
     */
    public static AccessibilityNodeInfo findViewByType(HelperQQService service, String widget) {
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo rootWindow = service.getRootInActiveWindow();
        if (rootWindow == null) {
            return null;
        }

        // 通过文本找到当前的节点
        return getChildNode(rootWindow, widget);
    }

    /**
     * 在子控件中查找指定類型的控件
     *
     * @param node
     * @param widget
     * @return
     */
    public static AccessibilityNodeInfo getChildNode(AccessibilityNodeInfo node, String widget) {
        int childCount = node.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (child != null) {
                    if (child.getClassName().equals(widget)) {
                        return child;
                    } else {
                        AccessibilityNodeInfo info = getChildNode(child, widget);
                        if (info != null) {
                            return info;
                        }
                    }
                }
            }
        } else {
            if (node.getClassName().equals(widget)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 在子控件中查找控件
     *
     * @param node
     * @return
     */
    public static AccessibilityNodeInfo findChildNodeWidget(AccessibilityNodeInfo node, String widget) {
        int childCount = node.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (child != null) {
                    if (child.getClassName().equals(widget)) {
                        return child;
                    } else {
                        AccessibilityNodeInfo result = findChildNodeWidget(child, widget);
                        if (child.getClassName().equals(widget)) {
                            return result;
                        }
                    }
                }
            }
        } else {
            if (widget.equals(node.getClassName())) {
                return node;
            }
        }
        return null;
    }

    /**
     * 查找指定控件的子控件中是否有指定文本的控件
     *
     * @param node
     * @param widget
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo getChildNode(AccessibilityNodeInfo node, String widget, String text) {
        int childCount = node.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (child != null) {
                    if (child.getClassName().equals(widget) && child.isEnabled() && !TextUtils.isEmpty(node.getText()) && child.getText().toString().equals(text)) {
                        return child;
                    } else {
                        AccessibilityNodeInfo info = getChildNode(child, widget, text);
                        if (info != null) {
                            return info;
                        }
                    }
                }
            }
        } else {
            if (node.getClassName().equals(widget) && node.isEnabled() && !TextUtils.isEmpty(node.getText()) && node.getText().toString().equals(text)) {
                return node;
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo getChildNodeByText(AccessibilityNodeInfo node, String text) {
        int childCount = node.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (child != null) {
                    if (child.isEnabled() && !TextUtils.isEmpty(child.getText()) && child.getText().toString().equals(text)) {
                        return child;
                    } else {
                        AccessibilityNodeInfo info = getChildNode(child, text);
                        if (info != null) {
                            return info;
                        }
                    }
                }
            }
        } else {
            if (node.isEnabled() && !TextUtils.isEmpty(node.getText()) && node.getText().toString().equals(text)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 根據文本查找指定類型的控件
     *
     * @param service
     * @param widget
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo findViewByText(HelperQQService service, String widget, String text) {
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.getClassName().equals(widget) && node.isEnabled()) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * 根據文本查找指定類型的控件
     *
     * @param service
     * @param widget
     * @param text
     * @return
     */
    public static List<AccessibilityNodeInfo> findViewListByText(HelperQQService service, String widget, String text) {
        List<AccessibilityNodeInfo> result = new ArrayList<AccessibilityNodeInfo>();
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.getClassName().equals(widget) && node.isEnabled()) {
                    result.add(node);
                }
            }
        }
        return result;
    }

    /**
     * 根據文本查找指定類型的控件
     *
     * @param service
     * @param text
     * @return
     */
    public static List<AccessibilityNodeInfo> findViewListByText(HelperQQService service, String text) {
        List<AccessibilityNodeInfo> result = new ArrayList<AccessibilityNodeInfo>();
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled()) {
                    result.add(node);
                }
            }
        }
        return result;
    }

    /**
     * 根據文本查找指定類型的控件
     *
     * @param widget
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo nodeInfo, String widget, String text) {
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.getClassName().equals(widget) && node.isEnabled()) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * 根據文本查找指定類型的控件
     *
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo findViewByTextOther(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled()) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * 根據文id查找指定類型的控件
     *
     * @param nodeInfo
     * @param id
     * @return
     */
    @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR2 )
    public static AccessibilityNodeInfo findViewByNodeId(AccessibilityNodeInfo nodeInfo, String id) {
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled()) {
                    return node;
                }
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findFocusView(HelperQQService service) {
        return service.getRootInActiveWindow().findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
    }

    /**
     * 根據文本查找控件
     *
     * @param service
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo findViewByText(HelperQQService service, String text) {
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled()) {
                    return node;
                }
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findViewByTextMatch(HelperQQService service, String text) {
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled() && !TextUtils.isEmpty(node.getText()) && node.getText().toString().equals(text)) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * 根據文本查找控件
     *
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled() && !TextUtils.isEmpty(node.getText()) && node.getText().toString().equals(text)) {
                    return node;
                }
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findViewByTextNoMatch(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled()) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * 根據控件ID查找控件
     *
     * @param service
     * @param id
     * @return
     */
    @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR2 )
    public static AccessibilityNodeInfo findViewById(HelperQQService service, String id) {
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodes != null && nodes.size() > 0) {
            return nodes.get(0);
        }
        return null;
    }

    @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR2 )
    public static List<AccessibilityNodeInfo> findViewListById(HelperQQService service, String id) {
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        return nodes;
    }

    /**
     * 啓動應用
     *
     * @param context
     * @param appName
     */
    public static void launcherApp(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.startActivity(intent);
    }

    public static void scrollViewDown(AccessibilityNodeInfo listView) {
        if (listView != null) {
            listView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
    }

    public static void scrollViewUp(AccessibilityNodeInfo listView) {
        if (listView != null) {
            listView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        }
    }

    public static int randomNum(int factor) {
        return random.nextInt(factor);
    }

    public static void pressScrollDown() {
        ShellUtils.execCommand("input swipe 361 1084 361 310", true);
    }

    public static void pressSmallScrollDown() {
        ShellUtils.execCommand("input swipe 361 742 361 310", true);
    }

    public static void pressScrollUp() {
        ShellUtils.execCommand("input swipe 361 310 361 1084", true);
    }

    public static void swipeUp(String str) {
        ShellUtils.execCommand("input swipe " + str, true);
    }

    public static void swipeDown(String str) {
        ShellUtils.execCommand("input swipe " + str, true);
    }

    public static Random getRandom() {
        return random;
    }

    public synchronized static void setProperty(String key, Object value) {
        String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME;
        FileUtils.createDir(cachePath);
        String cacheFile = cachePath + File.separator + key;
        FileUtils.createFile(cacheFile);
        FileUtils.writeObjectToFile(value, cacheFile);
    }

    public synchronized static Object getProperty(String key) {
        String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + key;
        return FileUtils.readObjectToFile(cachePath);
    }

    public synchronized static void saveObject(Object value, String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME;
        FileUtils.createDir(cachePath);
        String cacheFile = cachePath + File.separator + name;
        FileUtils.createFile(cacheFile);
        FileUtils.writeObjectToFile(value, cacheFile);
    }

    public synchronized static void removeObject(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        String cacheFile = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + name;
        if (FileUtils.fileIsExists(cacheFile)) {
            FileUtils.deleteFile(cacheFile);
        }
    }

    public synchronized static Object getObject(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + name;
        return FileUtils.readObjectToFile(cachePath);
    }

    public static void saveImageToPhoto(final Context context, final File imageFile) {
       /* try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), null);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile.getPath())));
            imageFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/


        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), null);

//        MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + imageFile.getName()}, null, null);
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile.getPath())));
//            connection=new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
//                @Override
//                public void onMediaScannerConnected() {
//                    connection.scanFile(imageFile.getPath(),imageFile.getName());
//                }
//
//                @Override
//                public void onScanCompleted(String s, Uri uri) {
//                    connection.disconnect();
//                }
//            });
//            connection.connect();
//            imageFile.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveImagesToPhoto(Context context, List<File> imageFiles) {
       /* try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), null);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile.getPath())));
            imageFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                LogUtils.logInfo("   file  name  =" + imageFile);
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            imageFile.getAbsolutePath(), imageFile.getName(), null);
//            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + imageFile.getName()}, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile.getPath())));
                imageFile.delete();
            }
        }
    }

    public static void delLoclImage(Context context, File imageFile) {
        if (imageFile.exists()) {
            imageFile.delete();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile.getPath())));
    }

    public static void delImageToPhoto() {
        //删除系统相册
        String cameraImagePath = "/sdcard/DCIM/Camera/";
        String picturesImagePath = "/sdcard/Pictures/";
        String qqphotoPath = "/sdcard/QQTask/QQPhoto/";
        File file = new File(cameraImagePath);
        file.listFiles(delImgeFilter);
        file = new File(picturesImagePath);
        file.listFiles(delImgeFilter);
        file = new File(qqphotoPath);
        file.listFiles(delImgeFilter);
    }

    private static FileFilter delImgeFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isFile()) {
                pathname.delete();
            }
            return false;
        }
    };

    public static String getFileNameByUrl(String fileUrl) {
        int index = fileUrl.lastIndexOf(File.separator);
        return fileUrl.substring((index + 1));
    }

    public static String getFileSuffix(String fileUrl) {
        int index = fileUrl.lastIndexOf(".");
        return fileUrl.substring(index);
    }

    public static AccessibilityNodeInfo findViewByDesc(HelperQQService service, String param) {
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo rootWindow = service.getRootInActiveWindow();
        if (rootWindow == null) {
            return null;
        }

        // 通过文本找到当前的节点
        return findChildNodeByDesc(rootWindow, param);
    }

    public static AccessibilityNodeInfo findChildNodeByDesc(AccessibilityNodeInfo node, String desc) {
        if (node == null) {
            return null;
        }

        int childCount = node.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (!TextUtils.isEmpty(child.getContentDescription()) && child.getContentDescription().equals(desc) && child.isEnabled()) {
                    return child;
                } else {
                    AccessibilityNodeInfo info = findChildNodeByDesc(child, desc);
                    if (info != null) {
                        return info;
                    }
                }
            }
        } else {
            if (!TextUtils.isEmpty(node.getContentDescription()) && node.getContentDescription().equals(desc) && node.isEnabled()) {
                return node;
            }
        }
        return null;
    }

    /**
     * 在子控件中查找文本控件
     *
     * @param node
     * @return
     */
    public static String findChildNodeText(AccessibilityNodeInfo node) {
        int childCount = node.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (child != null) {
                    if (!TextUtils.isEmpty(child.getText())) {
                        return child.getText().toString();
                    } else {
                        String result = findChildNodeText(child);
                        if (!TextUtils.isEmpty(result)) {
                            return result;
                        }
                    }
                }
            }
        } else {
            if (!TextUtils.isEmpty(node.getText())) {
                return node.getText().toString();
            }
        }
        return null;
    }

    /**
     * 退出应用
     *
     * @param appName
     */
    public static void exitApp(String appName) {
        String[] commonds = new String[]{
                "am force-stop " + appName,
                "am kill " + appName,
                "busybox pkill -9 " + appName
        };
        ShellUtils.execCommand(commonds, true, false);
        sleep(3000L);
    }

    public static boolean wxIsRun() {
        boolean isRun = false;
        ShellUtils.CommandResult result = ShellUtils.execCommand("ps | grep com.tencent.mobileqq", true);
        if (result.result == 0 && !TextUtils.isEmpty(result.successMsg)) {
            isRun = true;
        }
        return isRun;
    }

/*    public static void backDir(String dir) {
        String[] commands;
        String path = dir + "_shared";
        if (FileUtils.fileIsExists(path)) {
            commands = new String[]{
                    "rm -rf " + path,
                    "rm -rf /data/data/com.tencent.mm/app_cache",
                    "rm -rf /data/data/com.tencent.mm/app_dex",
                    "rm -rf /data/data/com.tencent.mm/app_lib",
                    "rm -rf /data/data/com.tencent.mm/lib",
                    "busybox tar cf " + dir + " /data/data/com.tencent.mm/"
            };
        }else{
            commands = new String[]{
                    "rm -rf /data/data/com.tencent.mm/app_cache",
                    "rm -rf /data/data/com.tencent.mm/app_dex",
                    "rm -rf /data/data/com.tencent.mm/app_lib",
                    "rm -rf /data/data/com.tencent.mm/lib",
                    "busybox tar cf " + dir + " /data/data/com.tencent.mm/"
            };
        }

        ShellUtils.execCommand(commands,true,false);
        sleep(10 * 1000L);
    }*/

/*    public static void reduction(String dir) {
        String[] commands;
        String path = dir + "_shared";
        if (FileUtils.fileIsExists(path)) {
            commands = new String[]{
                    "rm -rf /data/data/com.tencent.mm*//*",
                    "rm -rf /mnt/sdcard/Tencent/MicroMsg/",
                    "busybox tar xf " + dir,
                    "busybox tar xf " + path,
                    "chmod -R 777 /data/data/com.tencent.mm*//*"
            };
        }else{
            commands = new String[]{
                    "rm -rf /data/data/com.tencent.mm*//*",
                    "rm -rf /mnt/sdcard/Tencent/MicroMsg/",
                    "busybox tar xf " + dir,
                    "chmod -R 777 /data/data/com.tencent.mm*//*"
            };
        }
        ShellUtils.execCommand(commands,true,false);
        sleep(10 * 1000L);
    }*/

    private static String GetRandomString(int length, boolean useNum, boolean useLow, boolean useUpp, boolean useSpe) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        String str = "";
        if (useNum == true) {
            str += "0123456789";
        }
        if (useLow == true) {
            str += "abcdefghijklmnopqrstuvwxyz";
        }
        if (useUpp == true) {
            str += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (useSpe == true) {
            str += "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        }
        for (int i = 0; i < length; i++) {
            char value = str.charAt(r.nextInt(str.length() - 1));
            sb.append(value);
        }
        return sb.toString();
    }

    public static String makePwd() {
        return GetRandomString(8, true, true, true, false);
    }

    public static void unLockScreen(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
    }

    public static void lightScrenn(Context context) {
        PowerManager pwoerMangaer = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pwoerMangaer.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "camera");
        mWakeLock.setReferenceCounted(false);
        mWakeLock.acquire();
    }

    public static void showDialog(Context context, String msg, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(msg);
        dialog.setPositiveButton("确定", clickListener);
        dialog.setNegativeButton("取消", null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static List<AccessibilityNodeInfo> findViewListByType(HelperQQService service, String widget) {
        List<AccessibilityNodeInfo> result = new ArrayList<AccessibilityNodeInfo>();
        // 取得当前激活窗体的根节点
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return null;
        }
        getChildNode(nodeInfo, widget, result);
        return result;
    }

    /**
     * 在子控件中查找指定類型的控件
     *
     * @param node
     * @param widget
     * @return
     */
    public static void getChildNode(AccessibilityNodeInfo node, String widget, List<AccessibilityNodeInfo> result) {
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child.getClassName().equals(widget)) {
                result.add(child);
            } else {
                getChildNode(child, widget, result);
            }
        }
    }

    public static String makeIMEI() {
        String phoneimei = "";
        phoneimei = "868191" + GetRandomString(9, true, false, false, false);
        return phoneimei;
    }

    public static String makeMAC() {
        String phonemac = "";
        phonemac = "f4:8b:" + GetRandomString(2, true, false, false, false) + ":" + GetRandomString(2, true, false, false, false) + ":" + GetRandomString(2, true, false, false, false) + ":" + GetRandomString(2, true, false, false, false);
        return phonemac;

    }

    public static String makeSID() {
        String phonesid = "";
        phonesid = "460029" + GetRandomString(9, true, false, false, false);
        return phonesid;
    }
/*
    public synchronized static void savePwd(Object value, String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME;
        FileUtils.createDir(cachePath);
        String cacheFile = cachePath + File.separator + name;
        FileUtils.createFile(cacheFile);
        FileUtils.writeObjectToFile(value, cacheFile);
    }*/

    private static ProgressDialog progressDialog;

    public static void showProgressBar(Context context, String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        }
        progressDialog.show();
    }

    public static AccessibilityNodeInfo findListBtn(HelperQQService service, String msg) {
        List<AccessibilityNodeInfo> lists = Utils.findViewListById(service, "com.tencent.mobileqq:id/dialogTitle");//com.tencent.mobileqq:id/dialogTitle
        for (AccessibilityNodeInfo entry : lists) {
            if (entry != null && !TextUtils.isEmpty(entry.getText()) && entry.getText().toString().equals(msg)) {
                return entry;
            } else {
                if (entry != null) {
                    entry.recycle();
                }
            }
        }
        return null;
    }

    public static boolean obatinMsgDialogText(HelperQQService context, String msg) {
//        LogUtils.logInfo("  识别文字 ");
        boolean isTrue = false;
        Utils.sleep(2000L);
        Utils.findViewListById(context, msg);

        AccessibilityNodeInfo dialog = Utils.findViewByText(context, msg);
        if (dialog != null) {
            LogUtils.logInfo("  识别文字    ********");
            dialog.recycle();
            isTrue = true;
        }
        return isTrue;
    }

    public static void closeProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

/*    public static void reportAddFriendData(HeplerQQService service, TaskEntry taskEntry, int type) {
        String nickName = "";
        AccessibilityNodeInfo nickNameTx = Utils.findViewById(service, "com.tencent.mm:id/hu");
        if (nickNameTx != null && !TextUtils.isEmpty(nickNameTx.getText())) {
            nickName = nickNameTx.getText().toString();
        }
        HttpHandler.requestAddFriend(taskEntry.getWx_sign(), type + "", nickName, new HttpTask.HttpCallback() {
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
    }*/

    /*public static String obatinMarkName() {
        int index = getRandom().nextInt((markName.length - 1));
        return markName[index];
    }*/

    public static void setAdbEnable(Context context) {
        boolean enableAdb = (Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0);
        if (!enableAdb) {
            Settings.Secure.putInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 1);
            LogUtils.logInfo("设置调试模式");
        } else {
            LogUtils.logInfo("调试模式已打开");
        }
    }

    public static boolean isNumber(String tagert) {
        boolean isTrue = false;
        if (TextUtils.isEmpty(tagert)) {
            return isTrue;
        }
        if (tagert.matches("^[0-9]*$")) {
            isTrue = true;
        }
        return isTrue;
    }

/*    public static void otherLogin(HeplerQQService service, TaskEntry taskEntry, int index) {
        List<AccessibilityNodeInfo> editors = Utils.findViewListByType(service, EditText.class.getName());
        if (editors != null && editors.size() > 1) {
            AccessibilityNodeInfo firstPwd = editors.get(index);
            AccessibilityNodeInfo secondPwd = editors.get(index + 1);
            Utils.inputText(service, firstPwd, taskEntry.getWx_id());
            Utils.sleep(3 * 1000L);
            Utils.inputText(service, secondPwd, taskEntry.getWx_pass());
            Utils.sleep(3 * 1000L);
            Utils.clickCompone(Utils.findViewByText(service, Button.class.getName(), "登录"));
        }
    }*/
/*
    */

    /**
     * 验证系统是否还原成功
     *
     * @param //taskEntry
     * @return
     */
  /*  public static boolean validateIsModify(HelperQQService service, TaskEntry taskEntry) {
        boolean isTrue = false;
        String deviceId = Utils.getDeviceId(service);
        String sid = Utils.getSId(service);
        if (taskEntry.getImei().equals(deviceId) && taskEntry.getSid().equals(sid)) {
            isTrue = true;
        }
        return isTrue;
    }*/
    public static AccessibilityNodeInfo findAccountView(HelperQQService service, String account) {
        AccessibilityNodeInfo wxId;
        if ((wxId = Utils.findViewByText(service, account)) == null) {
            if (Utils.isNumber(account)) {
                StringBuilder sb = new StringBuilder(account);
                sb.insert(3, " ");
                sb.insert(8, " ");
                wxId = Utils.findViewByText(service, sb.toString());
            }
        }
        return wxId;
    }

    public static String findAppName(HelperQQService service) {
        return service.getApplicationInfo().loadLabel(service.getPackageManager()).toString();
    }

    /**
     * 当前辅助服务是否添加进辅助服务启动列表中
     *
     * @return boolean
     */
    public static boolean isCurrentSupportService() {
        boolean isTrue = false;
        ShellUtils.CommandResult serviceResult = ShellUtils.execCommand("settings get secure enabled_accessibility_services", true);
        LogUtils.logInfo("当前辅助服务 = " + serviceResult.successMsg);
        if (serviceResult.successMsg.contains("com.cc.task.helperx/.service.HelperQQService")) {
            isTrue = true;
        }
        return isTrue;
    }

    /**
     * 当前辅助服务是否停止
     *
     * @return boolean
     */
    public static boolean isSupportServiceStop() {
        boolean isTrue = false;
        int accessibityEnablsd = 0;
        ShellUtils.CommandResult openResult = ShellUtils.execCommand("settings get secure accessibility_enabled", true);
        if (!openResult.successMsg.equals("1")) {
            isTrue = true;
        }

//        String service = context.getPackageName() + "/" + HelperQQService.class.getCanonicalName();
//        LogUtils.logInfo("  service   " + service);
//        try {
//            accessibityEnablsd = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//        TextUtils.SimpleStringSplitter simpleStringSplitter =
//                new TextUtils.SimpleStringSplitter(':');
//
//        if (accessibityEnablsd == 1) {
//            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
//                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//            if (settingValue != null) {
//                simpleStringSplitter.setString(settingValue);
//                while (simpleStringSplitter.hasNext()) {
//                    String accessibityService = simpleStringSplitter.next();
//                    if (accessibityService.equalsIgnoreCase(service)) {
//                        isTrue = true;
//                    }
//                }
//            }
//        }
        return isTrue;
    }

    /**
     * 设置当前辅助服务
     */
    public static void setCurrntSupportService() {
        ShellUtils.execCommand("settings put secure enabled_accessibility_services com.cc.task.helperx/.service.HelperQQService", true);
    }

    /**
     * 停止/启动当前辅助服务
     *
     * @param startOrStop 0:停止/1:启动
     */
    public static void startAndStopSupportService(int startOrStop) {
        ShellUtils.execCommand("settings put secure accessibility_enabled " + startOrStop, true);
    }

    /**
     * 启动服务
     */
    public static void startTaskService(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TaskControllerService.class);
        context.startService(intent);
    }

    public static boolean startMsgView(HelperQQService service, String groupId, String version) {
        boolean istrue = false;
        LogUtils.logInfo("  跳转 打开聊天界面  ");
        String group_msg_url = "mqqwpa://im/chat?chat_type=group&uin=" + groupId + "&version=" + version;
        // adb shell am start -a com.tencent.mobileqq/.activity.SplashActivity -et mqqwpa://im/chat?chat_type=group&uin=185179045&version=1
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(group_msg_url));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            service.startActivity(intent);
        } catch (Exception e) {
            LogUtils.logInfo(" e.getMessage()  =  " + e.getMessage());
            e.getMessage();
        }

        Utils.sleep(4000L);
        AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
        if (groupNotice != null) {
            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
            Utils.sleep(3000);
        }
        Utils.sleep(2000);
        AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
        if (groupChat != null) {
            istrue = true;
        }
        return istrue;
    }

    /**
     * 跳入群成员聊天页面
     *
     * @return
     */
    public static void startGroupMembersChatView(HelperQQService service, String memberId,int uintype, String memberName, String groupId, String troop_uin) {
        LogUtils.logInfo("  troop_code  = " + groupId + "  troop_uin = " + troop_uin);
        Intent localIntent = null;
        localIntent = new Intent();
        localIntent.setAction("com.tencent.mobileqq.action.MAINACTIVITY");
//        localIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        localIntent.addCategory(Intent.CATEGORY_DEFAULT);
//        localIntent.setAction(Intent.ACTION_MAIN);
        localIntent.putExtra("uin", memberId);// 成员id
        localIntent.putExtra("uintype", uintype);
        localIntent.putExtra("apollo_actionid_key", 0);
        localIntent.putExtra("bool2", false);
        localIntent.putExtra("apollo_pkgid_key", 0);
        localIntent.putExtra("chat_subType", 0);
        localIntent.putExtra("isFromMainTab", false);
        localIntent.putExtra("open_chatfragment", true);
        localIntent.putExtra("PREVIOUS_WINDOW", "com.tencent.mobileqq.activity.TroopMemberCarActivity");
        localIntent.putExtra("PREVIOUS_UIN", memberId);// 成员id

        localIntent.putExtra("uinname", memberName);// 昵称
        localIntent.putExtra("troop_code", groupId); // 群号
        localIntent.putExtra("troop_uin", troop_uin);
        localIntent.putExtra("cSpecialFlag", 0);

//        localIntent.putExtra("isGameRoom", false);
        localIntent.putExtra("at_member_source", 500);
        service.startActivity(localIntent);
    }

    private static List<Activity> getActivitiesByApplication(Application application) {
        List<Activity> list = new ArrayList<>();
        try {
            Class<Application> applicationClass = Application.class;
            Field mLoadedApkField = applicationClass.getDeclaredField("mLoadedApk");
            mLoadedApkField.setAccessible(true);
            Object mLoadedApk = mLoadedApkField.get(application);
            Class<?> mLoadedApkClass = mLoadedApk.getClass();
            Field mActivityThreadField = mLoadedApkClass.getDeclaredField("mActivityThread");
            mActivityThreadField.setAccessible(true);
            Object mActivityThread = mActivityThreadField.get(mLoadedApk);
            Class<?> mActivityThreadClass = mActivityThread.getClass();
            Field mActivitiesField = mActivityThreadClass.getDeclaredField("mActivities");
            mActivitiesField.setAccessible(true);
            Object mActivities = mActivitiesField.get(mActivityThread);
            // 注意这里一定写成Map，低版本这里用的是HashMap，高版本用的是ArrayMap
            if (mActivities instanceof Map) {
                @SuppressWarnings( "unchecked" )
                Map<Object, Object> arrayMap = (Map<Object, Object>) mActivities;
                for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                    Object value = entry.getValue();
                    Class<?> activityClientRecordClass = value.getClass();
                    Field activityField = activityClientRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Object o = activityField.get(value);
                    list.add((Activity) o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /**
     * 判断服务是否后台运行
     *
     * @param context     Context
     * @param serviceName 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRun(Context context, String serviceName) {
        boolean isRun = false;
        if (context == null || TextUtils.isEmpty(serviceName)) {
            return isRun;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo serviceInfo : serviceList) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    public static void startFuzhuService() {
//        if (!Utils.isCurrentSupportService()) {
//            //设置当前辅助服务
//            Utils.setCurrntSupportService();
//            if (Utils.isSupportServiceStop(context)) {
//                Utils.startAndStopSupportService(Constants.SUPPORT_SERVICE_START);
//            }
//        } else {
//            //辅助服务是否停止
//            if (Utils.isSupportServiceStop(context)) {
//                Utils.startAndStopSupportService(Constants.SUPPORT_SERVICE_START);
//            }
//        }

        if (!Utils.isCurrentSupportService()) {
            //设置当前辅助服务
            Utils.sleep(3000L);
            Utils.setCurrntSupportService();
            Utils.sleep(3000L);
            if (Utils.isSupportServiceStop()) {
                //启动辅助服务
                Utils.sleep(3000L);
                Utils.startAndStopSupportService(Constants.SUPPORT_SERVICE_START);
                ShellUtils.CommandResult serviceResult = ShellUtils.execCommand("settings get secure enabled_accessibility_services", true);
                LogUtils.logInfo(" 当前辅助服务 = " + serviceResult.successMsg);
            } else {
                Utils.startAndStopSupportService(Constants.SUPPORT_SERVICE_STOP);
                Utils.sleep(3000L);
                Utils.startAndStopSupportService(Constants.SUPPORT_SERVICE_START);
                ShellUtils.CommandResult serviceResult = ShellUtils.execCommand("settings get secure enabled_accessibility_services", true);
                LogUtils.logInfo("当前辅助服务 = " + serviceResult.successMsg);
            }
        } else {
            //辅助服务是否停止
            if (Utils.isSupportServiceStop()) {
                //启动辅助服务\
                Utils.startAndStopSupportService(Constants.SUPPORT_SERVICE_STOP);
                Utils.sleep(3000L);
                Utils.startAndStopSupportService(Constants.SUPPORT_SERVICE_START);
                ShellUtils.CommandResult serviceResult = ShellUtils.execCommand("settings get secure enabled_accessibility_services", true);
                LogUtils.logInfo("当前辅助服务 = " + serviceResult.successMsg);
            }
        }
    }

    /**
     * 广播重启手机
     */
    public static void reboot() {
        String[] commands = new String[]{
                "am broadcast -a android.intent.action.REBOOT --ei nowait 1 --ei interval 1 --ei window 0"
        };
        ShellUtils.execCommand(commands, true, false);
    }

    public static void rebootSystem(HelperQQService service) {
        Utils.launcherApp(service, Constants.XPOSED_PACKAGE);
        Utils.sleep(6 * 1000L);
        AccessibilityNodeInfo negionBtn = Utils.findViewByDesc(service, "打开导航抽屉");
        if (negionBtn != null) {
            Utils.clickCompone(negionBtn);
            Utils.sleep(3000L);
            AccessibilityNodeInfo modelBtn = Utils.findViewByTextMatch(service, "模块");
            if (modelBtn != null) {
                Utils.clickCompone(modelBtn);
                Utils.sleep(3000L);
                String appName = Utils.findAppName(service);
                AccessibilityNodeInfo appNameBtn = Utils.findViewByTextMatch(service, appName);
                if (appNameBtn != null) {
                    AccessibilityNodeInfo checkBox = Utils.getChildNode(appNameBtn.getParent(), CheckBox.class.getName());
                    if (checkBox != null) {
                        if (checkBox.isChecked()) {
                            Utils.clickComponeNoRecycle(checkBox);
                            Utils.sleep(2000L);
                            if (Utils.findViewByText(service,"试图发出通知")!=null){

                                Utils.clickCompone(Utils.findViewByType(service,CheckBox.class.getName()));
                                Utils.sleep(2000L);

                                Utils.clickCompone(Utils.findViewByText(service,Button.class.getName(),"允许"));
                                Utils.sleep(2000);
                            }
                            Utils.clickCompone(checkBox);
                        } else {
                            Utils.clickCompone(checkBox);
                            if (Utils.findViewByText(service,"试图发出通知")!=null){
                                Utils.clickCompone(Utils.findViewByType(service,CheckBox.class.getName()));
                                Utils.sleep(2000L);
                                Utils.clickCompone(Utils.findViewByText(service,Button.class.getName(),"允许"));
                                Utils.sleep(2000);
                            }
                        }
                    }
                    //  Utils.clickCompone(appNameBtn);
                }
            }
        }
        Utils.sleep(2000L);
        Utils.clickCompone(Utils.findViewByDesc(service, "打开导航抽屉"));
        Utils.sleep(2000L);
        Utils.clickCompone(Utils.findViewByTextMatch(service, "框架"));
        Utils.sleep(2000L);
        Utils.clickCompone(Utils.findViewByDesc(service, "更多选项"));
        Utils.sleep(2000L);
        if (Utils.findViewByTextMatch(service, "重启设备") != null) {
            Utils.clickCompone(Utils.findViewByTextMatch(service, "重启设备"));
        } else {
            Utils.clickCompone(Utils.findViewByTextMatch(service, "正常重启"));
        }
        Utils.sleep(2000L);
        if (Utils.findViewByTextMatch(service, "重启设备") != null) {
            Utils.clickCompone(Utils.findViewByTextMatch(service, "重启设备"));
        } else {
            Utils.clickCompone(Utils.findViewByTextMatch(service, "正常重启"));
        }

    }

    public static void openScreen(Context context) {
        Utils.unLockScreen(context);
        Utils.lightScrenn(context);
    }

    /**
     * 删除原有数据
     */
    public static void clearData() {
        //删除原有数据
        ShellUtils.execCommand(new String[]{
                "rm -rf /data/data/com.tencent.mobileqq/*",
//                "rm -rf /sdcard/imqq/*",
                "rm -rf sdcard/tencent/*"
        }, true);
    }

    /***
     * 还原
     * @param dataPath
     * @param sdImQQPath
     * @param sdCvQQPath
     */
    public static void reductionData(String dataPath, String sdImQQPath, String sdCvQQPath) {
        //还原
        ShellUtils.execCommand(new String[]{
                "busybox tar xf " + dataPath,
                "chmod -R 777 /data/data/com.tencent.mobileqq/*",
//                "busybox tar xf " + sdImQQPath,
                "busybox tar xf " + sdCvQQPath
        }, true);

    }

    public static void openFlyModel(HelperQQService service) {
        String model = Build.MODEL;
        if (model.contains("SCL")) {
            ShellUtils.execCommand("am start -n com.android.settings/.HWSettings", true);
            Utils.sleep(8000L);
//            AccessibilityNodeInfo moreBtn = findMoreBtn(service);
            AccessibilityNodeInfo moreBtn = findViewByText(service, "更多");
            if (moreBtn != null) {
                Utils.clickCompone(moreBtn);
                Utils.sleep(3000L);
                AccessibilityNodeInfo airModeBtn = Utils.findViewById(service, "android:id/switchWidget");
                Utils.clickComponeNoRecycle(airModeBtn);
                Utils.sleep(3000L);
                Utils.clickCompone(airModeBtn);
                Utils.sleep(3000L);
                Utils.pressBack(service);
                Utils.sleep(3000L);
                Utils.pressBack(service);
                Utils.sleep(3000L);
                Utils.exitApp("com.android.settings");
            }
        } else if (model.contains("MI")) {
            ShellUtils.execCommand("am start -n com.android.settings/.MiuiSettings", true);
            Utils.sleep(3000L);
            AccessibilityNodeInfo airModeBtn = Utils.findViewById(service, "com.android.settings:id/slidingButton");
            Utils.clickComponeNoRecycle(airModeBtn);
            Utils.sleep(3000L);
            Utils.clickCompone(airModeBtn);
            Utils.sleep(7000L);
            Utils.pressBack(service);
            Utils.sleep(3000L);
            Utils.exitApp("com.android.settings");
        } else {
            ShellUtils.execCommand("am start -n com.android.settings/.Settings", true);
            Utils.sleep(4000L);
            AccessibilityNodeInfo moreBtn = findMoreBtn(service);
            if (moreBtn != null) {
                Utils.clickCompone(moreBtn);
                Utils.sleep(3000L);
                AccessibilityNodeInfo airModeBtn = Utils.findViewById(service, "android:id/switchWidget");
                Utils.clickComponeNoRecycle(airModeBtn);
                Utils.sleep(5000L);
                AccessibilityNodeInfo okBtn = Utils.findViewByText(service, Button.class.getName(), "确定");
                if (okBtn != null) {
                    Utils.clickCompone(okBtn);
                    Utils.sleep(3000L);
                }
                Utils.clickCompone(airModeBtn);
                Utils.sleep(3000L);
                Utils.pressBack(service);
                Utils.sleep(3000L);
                Utils.pressBack(service);
                Utils.sleep(3000L);
                Utils.exitApp("com.android.settings");
            }
        }
    }

    private static AccessibilityNodeInfo findMoreBtn(HelperQQService service) {
        List<AccessibilityNodeInfo> lists = Utils.findViewListById(service, "com.android.settings:id/title");
        for (AccessibilityNodeInfo entry : lists) {
            if (entry != null && !TextUtils.isEmpty(entry.getText()) && entry.getText().toString().equals("更多")) {
                return entry;
            } else {
                if (entry != null) {
                    entry.recycle();
                }
            }
        }
        return null;
    }

    /**
     * 验证系统是否还原成功
     *
     * @param taskEntry
     * @return
     */
    public static boolean validateIsModify(HelperQQService service, TaskEntry taskEntry) {
        boolean isTrue = false;
        String deviceId = Utils.getDeviceId(service);
        String sid = Utils.getSId(service);
        LogUtils.logInfo("本地信息:IMEI = " + deviceId + "  SID = " + sid);
        LogUtils.logInfo("后台信息:IMEI = " + taskEntry.getImei() + "  SID = " + taskEntry.getSid());
        if (taskEntry.getImei().equals(deviceId) && taskEntry.getSid().equals(sid)) {
            isTrue = true;
        }
        return isTrue;
    }

    /***
     * 备份
     * @param backups_data
     * @param backups_imqq
     * @param backups_cvqq
     */
    public static void backupsData(String backups_data, String backups_imqq, String backups_cvqq) {
        //备份
        ShellUtils.execCommand(new String[]{
                "busybox tar cf " + backups_data + " data/data/com.tencent.mobileqq",
                "rm -rf data/data/com.tencent.mobileqq/app_lib",
                "rm -rf data/data/com.tencent.mobileqq/app_webview",
                "rm -rf sdcard/tencent/imsdklogs",
                "rm -rf sdcard/tencent/imsdkpiccache",
                "rm -rf sdcard/tencent/imsdkvideocache",
                "rm -rf sdcard/tencent/QQ_Favorite",
                "rm -rf sdcard/tencent/QQ_Images",
                "rm -rf sdcard/tencent/QQfile_recv",
                "rm -rf sdcard/tencent/qzone",
                "busybox tar cf " + backups_cvqq + " sdcard/tencent"
        }, true);
    }

    /****
     * 截取字段
     * @param string
     * @return
     */
    public static String[] splitString(String string) {
        if (!TextUtils.isEmpty(string)) {
            String[] strs = string.split("@@@");
            return strs;
        }
        return null;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /***
     * 字符串转义
     * @param
     * @return
     */
//    public static String sqliteEscape(String keyWord) {
//        keyWord = keyWord.replace("/", "//");
//        keyWord = keyWord.replace("'", "''");
//        keyWord = keyWord.replace("[", "/[");
//        keyWord = keyWord.replace("]", "/]");
//        keyWord = keyWord.replace("%", "/%");
//        keyWord = keyWord.replace("&", "/&");
//        keyWord = keyWord.replace("_", "/_");
//        keyWord = keyWord.replace("(", "/(");
//        keyWord = keyWord.replace(")", "/)");
//        return keyWord;
//    }

    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    // URLEncoder 编码
    public static Object encoderCode(Object string) {
        String enUft = null;
        try {
            enUft = URLEncoder.encode(String.valueOf(string), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enUft;
    }

    public static String decoderCode(String string) {
        java.net.URLDecoder urlDecoder = new java.net.URLDecoder();
        String str = null;
        try {
            str = urlDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    //将 s 进行 BASE64 编码
    public static String getBASE64(String s) {
        if (s == null) return null;
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
    }

    //将 BASE64 编码的字符串 s 进行解码
    public static String getFromBASE64(String s) {
        if (s == null) return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
}
