package com.cc.task.helperx.task;

import android.text.TextUtils;

import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.ShellUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileFilter;

public class DelBackTask {

    public void delbackTask(){
        String macId = FileUtils.readStringToFile(Constants.MAC_ID_FILE);
        if (!TextUtils.isEmpty(macId)) {
            HttpHandler.requestDelbackInfo(macId,new HttpTask.HttpCallback(){
                @Override
                public void onSuccess(String data) {
                    if(!TextUtils.isEmpty(data)){
                        try {
                            JSONArray datas = new JSONArray(data);
                            exeDelback(datas);
                        } catch (JSONException e) {
                            LogUtils.logError(e.getMessage(),e);
                        }
                    }
                }

                @Override
                public void onFailure(String errMsg) {

                }

                @Override
                public void onFinished() {
                }
            });
        }
    }

    private void exeDelback(JSONArray datas){
        if(datas.length() > 0){
            exeDelbackInvalidate(datas);
        }else if(datas.length() == 0){
            delAllback();
        }
    }

    private void exeDelbackInvalidate(final JSONArray datas){
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                boolean isTrue = true;
                String fileName = pathname.getName();
                if(fileName.length() >= 32){
                    for(int index = 0; index < datas.length() ; index ++){
                        String wxSign = datas.optString(index);
                        if(fileName.contains(wxSign)){
                            isTrue = false;
                            LogUtils.logInfo("保存的文件:" + fileName);
                            break;
                        }
                    }
                }
                return isTrue;
            }
        };
        File file = new File("/sdcard/"+Constants.CACHE_PATH_NAME);
        File[] files = file.listFiles(fileFilter);
        StringBuilder commonds = new StringBuilder();
        for(File delFile :files){
            String fileName = delFile.getName();
            if(fileName.length() >= 32){
                String commond = "rm -rf "+delFile.getPath();
                commonds.append(commond);
                commonds.append(",");
            }
        }
        commonds.append("rm -rf /sdcard/"+Constants.CACHE_PATH_NAME+"/bak_date");
        String commond = commonds.toString();
        if(!TextUtils.isEmpty(commond)){
            ShellUtils.execCommand(commond.split(","),false);
        }
    }

    private void delAllback(){
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                boolean isTrue = false;
                String fileName = pathname.getName();
                if(fileName.length() >= 32){
                    LogUtils.logInfo("删除文件:"+fileName);
                    isTrue = true;
                }
                return isTrue;
            }
        };
        File file = new File("/sdcard/"+Constants.CACHE_PATH_NAME);
        File[] files = file.listFiles(fileFilter);
        StringBuilder commonds = new StringBuilder();
        for(File delFile :files){
            String fileName = delFile.getName();
            if(fileName.length() >= 32){
                String commond = "rm -rf "+delFile.getPath();
                commonds.append(commond);
                commonds.append(",");
            }
        }
        commonds.append("rm -rf /sdcard/"+Constants.CACHE_PATH_NAME+"/bak_date");
        String commond = commonds.toString();
        if(!TextUtils.isEmpty(commond)){
            ShellUtils.execCommand(commond.split(","),false);
        }
    }
}
