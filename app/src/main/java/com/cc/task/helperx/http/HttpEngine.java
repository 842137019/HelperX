package com.cc.task.helperx.http;

import android.text.TextUtils;


import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by yzq on 2017/3/14.
 */

public class HttpEngine {

    public static String download(String url, Map<String, String> param) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (param != null && param.size() > 0) {
            url += assembledRequestParam(param);
        }

        FileOutputStream fos = null;
        InputStream is = null;
        File outFile = null;
        try {
            URL requestUrl = new URL(url);
            URLConnection connection = requestUrl.openConnection();
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE 7.0;Windows NT 5.2;Trident/4.0;");
            connection.setDoInput(true);
            connection.connect();

            String[] names = url.split("/");
//            String fileName = System.currentTimeMillis() + Utils.getFileSuffix(url);
            String fileName = names[names.length - 1];
            String parentPath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + Constants.QQ_PHOTO;
            if (!FileUtils.fileIsExists(parentPath)) {
                FileUtils.createDir(parentPath);
            }
            outFile = new File(parentPath, fileName);
            if (outFile.exists()) {
                outFile.delete();
            }
            outFile.createNewFile();

            fos = new FileOutputStream(outFile);
            is = connection.getInputStream();
            int length;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
        } catch (Exception e) {
            outFile.delete();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outFile.toString();
    }

    public static String download(String url, String savePath, Map<String, String> param) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (param != null && param.size() > 0) {
            url += assembledRequestParam(param);
        }

        FileOutputStream fos = null;
        InputStream is = null;
        File outFile = null;
        try {
            URL requestUrl = new URL(url);
            URLConnection connection = requestUrl.openConnection();
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setConnectTimeout(6 * 1000);
            connection.setReadTimeout(6 * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE 7.0;Windows NT 5.2;Trident/4.0;");
            connection.setDoInput(true);
            connection.connect();

            String parentPath = savePath.substring(0, savePath.lastIndexOf(File.separator));
            if (!FileUtils.fileIsExists(parentPath)) {
                FileUtils.createDir(parentPath);
            }

            outFile = new File(savePath);
            if (outFile.exists()) {
                outFile.delete();
            }
            outFile.createNewFile();

            fos = new FileOutputStream(outFile);
            is = connection.getInputStream();
            int length;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
        } catch (Exception e) {
            outFile.delete();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outFile.toString();
    }

    public static String get(String url, Map<String, String> params) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (params != null && params.size() > 0) {
            url += assembledRequestParam(params);
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
//            LogUtils.logError("   url = " + url);
            URL requestUrl = new URL(url);
//            LogUtils.logError("   requestUrl = " + requestUrl);
            URLConnection connection = requestUrl.openConnection();
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setConnectTimeout(6 * 1000);
            connection.setReadTimeout(6 * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE 7.0;Windows NT 5.2;Trident/4.0;");
            connection.setDoInput(true);
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content;
            while (!TextUtils.isEmpty(content = reader.readLine())) {
                sb.append(content);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String post(String url, Map<String, String> params) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String paramUrl = "";
        if (params != null && params.size() > 0) {
            paramUrl = assembledPostRequestParam(params);
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            URL requestUrl = new URL(url);
            URLConnection connection = requestUrl.openConnection();
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setConnectTimeout(6 * 1000);
            connection.setReadTimeout(6 * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE 7.0;Windows NT 5.2;Trident/4.0;");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            if (!TextUtils.isEmpty(paramUrl)) {
                writer = new PrintWriter(connection.getOutputStream());
                writer.write(paramUrl);
                writer.flush();
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content;
            while (!TextUtils.isEmpty(content = reader.readLine())) {
                sb.append(content);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private static String assembledPostRequestParam(Map<String, String> param) {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = param.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(key + "=" + param.get(key));
            sb.append("&");
        }
        String result = sb.substring(0, (sb.length() - 1));
        return result;
    }

    private static String assembledRequestParam(Map<String, String> param) {
        StringBuilder sb = new StringBuilder("?");
        Set<String> keys = param.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(key + "=" + param.get(key));
            sb.append("&");
        }
        String result = sb.substring(0, (sb.length() - 1));
        return result;
    }

}
