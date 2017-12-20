package com.cc.task.helperx.task;

import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.Utils;

import java.util.List;

/**
 * 關閉功能
 * Created by fangying on 2017/9/21.
 */

public class CloseFunctionTask {
    private HelperQQService service;
    private Handler handler;

    public CloseFunctionTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    public void closeFunction() {
        if( !Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)){
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        if (Utils.findViewByText(service, "动态")==null){
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(Utils.findViewByText(service, "动态"));
        Utils.sleep(2000);

        Utils.clickCompone(Utils.findViewByText(service, "更多"));
        Utils.sleep(2000);

        boolean isfindOpen = true;
        while (isfindOpen) {
            if (Utils.findViewByText(service, "已开启的功能") != null) {
                Utils.clickCompone(Utils.findViewByText(service, "已开启的功能"));
                Utils.sleep(1500);
                isfindOpen = false;
            } else {
                Utils.pressSmallScrollDown();
                Utils.sleep(2000);
            }
        }

        AccessibilityNodeInfo titleNamae = Utils.findViewByTextMatch(service, "在动态中显示");
        if (titleNamae != null) {
                forCloseFunction();
                Utils.clickComponeByXY(Utils.findViewByText(service,"更多"));
                Utils.sleep(1500L);
                Utils.clickComponeByXY(Utils.findViewByText(service,"动态"));
                Utils.sleep(1500L);
                FileUtils.writeFileToSDCard("\t \t关闭功能执行完成 "+ DateUtils.format(System.currentTimeMillis(),DateUtils.DEFAULT_DATE_FORMAT)+"]","RunTime","runTimeLog",true,false);
                handler.sendEmptyMessage(1);
        }
    }

    /**
     * 重复调用自己，关闭功能
     */
    private void forCloseFunction() {
        List<AccessibilityNodeInfo> txt = Utils.findViewListById(service,"com.tencent.mobileqq:id/letsTextView");
        if (txt!=null && txt.size()>0){
            Utils.clickComponeByXY(txt.get(0));
            Utils.sleep(2000L);
            Utils.clickCompone(Utils.findViewByText(service, "关闭"));
            Utils.sleep(2000);
            AccessibilityNodeInfo closeInfo = Utils.findViewByText(service, "仍然关闭");
            if (closeInfo != null) {
                Utils.clickCompone(closeInfo);
                Utils.sleep(1500);
                Utils.clickCompone(Utils.findViewByText(service, "返回"));
                Utils.sleep(1500);
            }
            txt.clear();
            forCloseFunction();
        }
    }
}
