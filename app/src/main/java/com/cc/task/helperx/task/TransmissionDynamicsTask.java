package com.cc.task.helperx.task;

import android.content.ContentValues;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.ScrollView;

import com.cc.task.helperx.entity.GroupInfo;
import com.cc.task.helperx.entity.MessageEntity;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 发送动态
 * Created by fangying on 2017/12/12.
 */

public class TransmissionDynamicsTask {

    private HelperQQService service;
    private Handler handler;
    private boolean isphototxt = true;
    private int dex = 0;
    public TransmissionDynamicsTask(HelperQQService service, Handler handler) {
        this.handler = handler;
        this.service = service;
    }

    private void checkDynamic() {
        AccessibilityNodeInfo dynamicInfo = Utils.findViewByTextMatch(service, "动态");
        if (dynamicInfo != null) {
            Utils.clickCompone(dynamicInfo);
            Utils.sleep(1500L);

            Utils.clickCompone(Utils.findViewByTextMatch(service, "好友动态"));
            Utils.sleep(2000L);

            Utils.clickCompone(Utils.findViewByDesc(service, "写说说等按钮"));
            Utils.sleep(2000L);

            AccessibilityNodeInfo closeInfo = Utils.findViewByDesc(service, "关闭");
            if (closeInfo != null) {
                switch (dex) {
                    case 1:// 说说
                        Utils.clickCompone(Utils.findViewByText(service, "说说"));
                        Utils.sleep(2000L);
                        AccessibilityNodeInfo nodeInfo = Utils.findViewByType(service, EditText.class.getName());
                        if (nodeInfo != null) {
                            Utils.inputText(service, nodeInfo, "今天星期五啦");
                            Utils.sleep(3000L);

                            if (isphototxt) {//如果存在图片  添加图片
                                AccessibilityNodeInfo imgNodeInfo = Utils.findViewByDesc(service, "添加图片");
                                if (imgNodeInfo != null) {
                                    Utils.clickCompone(imgNodeInfo);
                                    Utils.sleep(2000L);

                                    if (Utils.findViewByType(service, ScrollView.class.getName()) != null) {
                                        // 下载图片

                                        // 选择图片来源
                                        Utils.clickCompone(Utils.findViewByTextMatch(service, "从手机选择"));
                                        Utils.sleep(2000L);

                                        Utils.clickCompone(Utils.findViewByTextMatch(service, "相册"));
                                        Utils.sleep(1500L);

                                        AccessibilityNodeInfo camera = Utils.findViewByText(service, "Camera");
                                        AccessibilityNodeInfo photo = Utils.findViewByText(service, "QQPhoto");
                                        if (camera != null) {
                                            Utils.clickComponeByXY(camera);
                                            Utils.sleep(2000);
                                            // 选择图片
                                        } else if (photo != null) {
                                            Utils.clickComponeByXY(photo);
                                            Utils.sleep(2000);
                                            // 选择图片
                                        } else {
                                            Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                                            Utils.sleep(2000);

                                        }
                                    }
                                }
                            }

                            AccessibilityNodeInfo publishInfo = Utils.findViewByTextMatch(service, "发表");
                            if (publishInfo != null && publishInfo.isEnabled()) {
                                Utils.clickCompone(publishInfo);
                                Utils.sleep(2000L);
                            }
                        }
                        break;

                    case 2:// 相册

                        break;
                }
            }
        }
    }

    private void checkPhoto(List<String> datas) {
        AccessibilityNodeInfo imges = Utils.findViewById(service, "com.tencent.mobileqq:id/photo_list_gv");
        if (imges != null) {
            LogUtils.logInfo("size = " + imges.getChildCount() + "    ,   ");
            List<AccessibilityNodeInfo> checkes = Utils.findViewListByType(service, "android.widget.CheckBox");
            if (checkes != null && checkes.size() > 0) {
                LogUtils.logInfo("CheckBox size = " + checkes.size() + "    ,   ");
                if (checkes.size() == 1) {
                    Utils.clickCompone(checkes.get(0));
                    Utils.sleep(1500);
                } else {
                    int length = checkes.size() - 2;
                    for (int i = 0; i < length; i++) {
                        Utils.clickCompone(checkes.get(i));
                        Utils.sleep(1000);
                    }
                }
                Utils.clickCompone(Utils.findViewByText(service, "发送"));
                Utils.sleep(1500);
            }
                if (Utils.findViewByTextMatch(service, "无法上传") != null) {
                    Utils.findViewByText(service, "我知道了");
                    Utils.sleep(1500L);

                    Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                    Utils.sleep(1500L);

                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                } else {
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                }
        }
    }
}
