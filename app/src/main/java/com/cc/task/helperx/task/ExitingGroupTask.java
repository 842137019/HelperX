package com.cc.task.helperx.task;

import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import java.util.List;

/**
 * 退群
 * Created by fangying on 2017/12/6.
 */

public class ExitingGroupTask {

    private HelperQQService service;
    private Handler handler;

    public ExitingGroupTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    int index = 0;

    public void exitingGroup() {

        AccessibilityNodeInfo contacts = Utils.findViewByTextMatch(service, "联系人");
        if (contacts == null) {
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(contacts);
        Utils.sleep(2000);

        Utils.clickCompone(Utils.findViewByTextMatch(service, "群"));
        Utils.sleep(2000);

        List<AccessibilityNodeInfo> listText = Utils.findViewListById(service, "com.tencent.mobileqq:id/text1");
        if (listText != null && listText.size() > 0) {
            LogUtils.i("群数量:" + listText.size());
            if (index < listText.size()) {
                isfindStringObj(listText.get(0));

                AccessibilityNodeInfo rightImage = Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnRightImage");
                if (rightImage != null) {
                    Utils.clickComponeByXY(rightImage);
                    Utils.sleep(2000L);

                    if (Utils.findViewByTextMatch(service, "退出该群") != null) {
                        Utils.clickCompone(Utils.findViewByTextMatch(service, "退出该群"));
                        Utils.sleep(2000L);
                        if (Utils.findViewByText(service,"你将退出群")!=null){
                            Utils.clickCompone(Utils.findViewByTextMatch(service,"退出"));
                            Utils.sleep(5000L);
                            exitingGroup();
                        }
                    }
                }
            }
        }else {
            LogUtils.i("退群执行完成");
            handler.sendEmptyMessage(1);
        }
    }

    private void isfindStringObj(AccessibilityNodeInfo group) {
        String troopUin = null;
        if (group != null) {
            Utils.clickCompone(group);
            Utils.sleep(2000);
            LogUtils.i("群聊天页面");
            AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
            if (groupNotice != null) {
                Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                Utils.sleep(1500);
            }

            // com.tencent.mobileqq:id/dialogText
            AccessibilityNodeInfo info = Utils.findViewByText(service, "该群因涉及违反相关条例，已被永久封停，不能使用。系统将会自动解散该群。");
            if (info != null) {
                // com.tencent.mobileqq:id/dialogRightBtn
                Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                Utils.sleep(2000L);
            }
        }
    }
}
