package com.cc.task.helperx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/27 0027.
 */

public class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DESLASH_DATE_FORMAT ="yyyy/MM/dd HH:mm:ss";


    /**
     *  格式化时间,自定义标签
     * @param timeInMillis millisecond
     * @param pattern  格式化时间
     * @return
     */
    public static String format(long timeInMillis, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timeInMillis));
    }


}
