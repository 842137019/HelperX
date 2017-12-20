package com.cc.task.helperx.utils;

/**
 * Created by fangying on 2017/11/29.
 */

public class KeyWordUtils {

    private static String[] keywords = new String[]{"贷款", "抵押", "成人", "学生", "加群", "车贷",
            "贷", "款", "兼职", "日结", "金融", "管理", "诚信", "信用", "借", "业务", "广告"};

    public static boolean ContainKeyWord(String s) {
        boolean isexist = false;
        for (String str : keywords) {
            if (s.contains(str)) {
                isexist = true;
            }
        }
        return isexist;
    }
}
