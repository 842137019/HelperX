package com.cc.task.helperx;

import android.app.Activity;
import android.os.Bundle;

import com.cc.task.helperx.utils.EventBusUtils;
import com.cc.task.helperx.utils.Utils;

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBusUtils.getInstance().register(this);
        Utils.openScreen(this);
        new Thread() {
            @Override
            public void run() {
                Utils.startFuzhuService();
                Utils.startTaskService(MainActivity.this);
            }
        }.start();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
