package com.cc.task.helperx.entity;

import android.view.accessibility.AccessibilityNodeInfo;

import java.io.Serializable;

/**
 * Created by yzq on 2017/4/19.
 */

public class NewsEntry implements Serializable {

    private AccessibilityNodeInfo lastChld;
    private int index;

    public NewsEntry(AccessibilityNodeInfo lastChld, int index) {
        this.lastChld = lastChld;
        this.index = index;
    }

    public NewsEntry() {
    }

    public AccessibilityNodeInfo getLastChld() {
        return lastChld;
    }

    public void setLastChld(AccessibilityNodeInfo lastChld) {
        this.lastChld = lastChld;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
