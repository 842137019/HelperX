package com.cc.task.helperx;

import android.content.Context;


import com.cc.task.helperx.http.CrashHandler;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.Utils;

import org.litepal.LitePalApplication;

import java.io.File;

/**
 *
 * Created by fangying on 2017/9/4.
 */

public class MyApplication extends LitePalApplication {

    public static boolean debugMode = false; // true 调试模式 false正常执行
     public static  Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Utils.initClipManager(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        cacheFile();
    }

    private void cacheFile() {
        String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME;
        FileUtils.createDir(cachePath);
        String cacheiamges = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + Constants.QQ_PHOTO;
        FileUtils.createDir(cacheiamges);
        String cacheInfo = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + Constants.QQ_INFO;
        FileUtils.createDir(cacheInfo);

        String new_data = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "new_data";
        String cv_qq = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "cv_qq";
        if (!FileUtils.fileIsExists(new_data)) {
            Utils.exitApp(Constants.APP_NAME);
            Utils.backupsData(new_data, null, cv_qq);
        }
    }
}
