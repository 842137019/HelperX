package com.cc.task.helperx.task;

import android.os.Handler;
import android.os.Message;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.cc.task.helperx.entity.NewsEntry;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

/**
 * 瀏覽新聞
 */
public class BrowseNewsTask {

    private HelperQQService service;
    private Handler handler;

    private Handler currentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Object obj = msg.obj;
                if (obj != null && obj instanceof NewsEntry) {
                    NewsEntry newsEntry = (NewsEntry) obj;
                    traversalNews(newsEntry);
                }
            }
        }
    };

    public BrowseNewsTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    public void browseNews() {
        Utils.startGroupMembersChatView(service, "2909288299", 1008, "腾讯新闻", null, null);
        exeReadNews();
    }

    public void exeReadNews() {
        Utils.pressScrollDown();
        Utils.sleep(6000L);
        AccessibilityNodeInfo listView = Utils.findViewByType(service, AbsListView.class.getName());
        if (listView != null && listView.getChildCount() > 0) {
            AccessibilityNodeInfo lastChld = listView.getChild(listView.getChildCount() - 1);
            if (lastChld != null && lastChld.getChildCount() > 0) {
                AccessibilityNodeInfo chldnewLast = lastChld.getChild(lastChld.getChildCount() - 1);
                if (chldnewLast != null) {
                    NewsEntry newsEntry = new NewsEntry(chldnewLast, 0);
                    traversalNews(newsEntry);
                } else {
                    listView.recycle();
                    Utils.pressBack(service);
                    Utils.sleep(3 * 1000L);
                    handler.sendEmptyMessage(1);
                }
            } else {
                listView.recycle();
                Utils.pressBack(service);
                Utils.sleep(3 * 1000L);
                handler.sendEmptyMessage(1);
            }
        } else {
            Utils.pressBack(service);
            Utils.sleep(3 * 1000L);
            handler.sendEmptyMessage(1);
        }
    }

    private void traversalNews(NewsEntry newsEntry) {
        AccessibilityNodeInfo lastChld = newsEntry.getLastChld();
        int index = newsEntry.getIndex();
        LogUtils.i("index = " + index);
        if (index < lastChld.getChildCount()) {
            AccessibilityNodeInfo finalView = newsEntry.getLastChld().getChild(index);
            if (finalView != null && finalView.getClassName().equals(LinearLayout.class.getName()) && finalView.isClickable()) {
                LogUtils.i("finalView = " + finalView);
                Utils.clickCompone(finalView);
                Utils.sleep(5 * 1000L);
                if (Utils.isTragetActivity(Constants.PAGE_NEWS_CONTENT_PAGE)) {
                    AccessibilityNodeInfo goonBtn = Utils.findViewByTextMatch(service, "继续");
                    if (goonBtn != null) {
                        Utils.clickCompone(goonBtn);
                        Utils.sleep(3 * 1000L);
                    }
                    new ScrollNewThread(newsEntry).start();
                } else {
                    newsEntry.setIndex(index + 1);
                    traversalNews(newsEntry);
                }
            } else {
                newsEntry.setIndex(index + 1);
                traversalNews(newsEntry);
            }
        } else {
            Utils.sleep(3 * 1000L);
            Utils.pressBack(service);
            Utils.sleep(3 * 1000L);
            handler.sendEmptyMessage(1);
        }
    }

    private class ScrollNewThread extends Thread {

        private NewsEntry newsEntry;

        public ScrollNewThread(NewsEntry newsEntry) {
            this.newsEntry = newsEntry;
        }

        @Override
        public void run() {
            Utils.pressScrollDown();
            Utils.sleep(4 * 1000L);
            Utils.pressScrollDown();
            Utils.sleep(4 * 1000L);
            Utils.pressScrollDown();
            Utils.sleep(4 * 1000L);
            Utils.pressBack(service);
            Utils.sleep(3 * 1000L);

            newsEntry.setIndex(newsEntry.getIndex() + 1);
            Message msg = currentHandler.obtainMessage();
            msg.obj = newsEntry;
            msg.what = 1;
            msg.sendToTarget();
        }
    }
}
