package com.cc.task.helperx.http;

import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yzq on 2017/3/14.
 */

public class HttpTask {

    public static HttpTask instance;

    private ExecutorService executors;

    private final int GET = 0;

    private final int POST = 1;

    private final int DOWNLOAD = 2;

    private HttpTask() {
        executors = Executors.newSingleThreadExecutor();
    }

    public static HttpTask getInstance() {
        if (instance == null) {
            instance = new HttpTask();
        }
        return instance;
    }

    public void download(String url, HttpCallback callback) {
        executors.submit(new Work(url, DOWNLOAD, callback));
    }

    public void download(String url, Map<String, String> param, HttpCallback callback) {
        executors.submit(new Work(url, param, DOWNLOAD, callback));
    }

    public void download(String url, String savePath, Map<String, String> param, HttpCallback callback) {
        executors.submit(new Work(url, param, savePath, DOWNLOAD, callback));
    }

    public void post(String url, Map<String, String> param, HttpCallback callback) {
        executors.submit(new Work(url, param, POST, callback));
    }

    public void get(String url, Map<String, String> param, HttpCallback callback) {

        executors.submit(new Work(url, param, GET, callback));
    }

    public interface HttpCallback {

        void onSuccess(String data);

        void onFailure(String errMsg);

        void onFinished();
    }

    private class Work implements Runnable {

        private String url;
        private Map<String, String> param;
        private HttpCallback callback;
        private int type;
        private String savePath;

        public Work(String url, Map<String, String> param, String savePath, int type, HttpCallback callback) {
            this.url = url;
            this.param = param;
            this.callback = callback;
            this.type = type;
            this.savePath = savePath;
        }

        public Work(String url, Map<String, String> param, int type, HttpCallback callback) {
            this.url = url;
            this.param = param;
            this.callback = callback;
            this.type = type;
        }

        public Work(String url, int type, HttpCallback callback) {
            this.url = url;
            this.callback = callback;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                String reult = "";
                switch (type) {
                    case GET:
                        reult = HttpEngine.get(url, param);
                        break;
                    case POST:
                        reult = HttpEngine.post(url, param);
                        break;
                    case DOWNLOAD:
                        if (TextUtils.isEmpty(savePath)) {
                            reult = HttpEngine.download(url, param);
                        } else {
                            reult = HttpEngine.download(url, savePath, param);
                        }
                        break;
                }
//                LogUtils.logInfo("  result =" + reult);
                if (callback != null && !TextUtils.isEmpty(reult)) {
                    callback.onSuccess(reult);
                } else {
                    callback.onFailure("请求失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onFailure(e.getMessage());
                }
            } finally {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        }
    }

}
