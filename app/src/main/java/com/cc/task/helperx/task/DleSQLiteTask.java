package com.cc.task.helperx.task;

import android.os.Handler;

import com.cc.task.helperx.utils.Utils;

import org.litepal.crud.DataSupport;


/**
 * Created by fangying on 2017/11/24.
 */

public class DleSQLiteTask {

    private Handler handler;

    public DleSQLiteTask(Handler handler) {
        this.handler = handler;
    }

    public void deleteSQLite(String getWx_sign){
       // DataSupport.deleteAll(GroupMembersInfo.class);
       // DataSupport.deleteAll(" sqlite_sequence "," name = ? ","groupmembersinfo");
       // DataSupport.deleteAll(GroupInfo.class);
       // DataSupport.deleteAll(" sqlite_sequence "," name = ? ","groupinfo");
//        DataSupport.deleteAll(GroupMembersInfo.class);
//        DataSupport.deleteAll(" sqlite_sequence "," name = ? ","groupmembersinfo");
        DataSupport.deleteAll("groupinfo", " wxsign = ? ", getWx_sign);
        DataSupport.deleteAll("groupmembersinfo", " wxsign = ? ", getWx_sign);
        Utils.sleep(3000L);
        handler.sendEmptyMessage(1);
    }
}
