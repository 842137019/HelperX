package com.cc.task.helperx.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzq on 2017/1/16.
 */

public class FileUtils {

    public static void createFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.logError(e.getLocalizedMessage(), e);
            }
        }
    }

    public static List<File> createFiles(List<String> filePathes) {
        List<File> fliles = new ArrayList<>();
        for (String filePath : filePathes) {
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    fliles.add(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.logError(e.getLocalizedMessage(), e);
                }
            }
        }
        LogUtils.logInfo("   file.size =" + fliles.size());
        return fliles;
    }

    public static boolean deleteFiles(List<String> filePaths) {
        boolean isTrue = false;
        for (String path : filePaths) {
            isTrue = deleteFile(path);
        }
        return isTrue;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public static void createDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void writeObjectToFile(Object object, String filePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object readObjectToFile(String filePath) {
        if (!fileIsExists(filePath)) {
            return null;
        }

        FileInputStream fos = null;
        ObjectInputStream oos = null;
        try {
            fos = new FileInputStream(filePath);
            oos = new ObjectInputStream(fos);
            return oos.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String readStringToFile(String filePath) {
        if (!fileIsExists(filePath)) {
            return null;
        }

        File file = new File(filePath);
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String content;
            while (!TextUtils.isEmpty(content = br.readLine())) {
                sb.append(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static boolean fileIsExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    public static void writeInfoToFile(String message, String filePath) {
        PrintWriter pw = null;
        OutputStreamWriter osw = null;
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            String parentPath = file.getParent();
            createDir(parentPath);
            createFile(filePath);

            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            pw = new PrintWriter(osw);
            pw.write(message);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void writeFileToSDCard(String s, String s1, String s2, boolean b, boolean b1) {

        String groupmembers = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + s1;
        if (!FileUtils.fileIsExists(groupmembers)) {
            FileUtils.createDir(groupmembers);
        }
        String filePath = groupmembers;
        File file = new File(filePath, s2);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(s.getBytes());
            raf.close();
        } catch (Exception e) {
            LogUtils.logInfo("  err  =  " + e.getMessage());
        }
    }


    public synchronized static void writeFileToSDCardUin(final String context, final String folder, final String fileName, final boolean append, final boolean autoLine) {
        String filePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + folder;
//        String groupmembers = "/sdcard" + File.separator;
        ;//+Constants.CACHE_PATH_NAME+ File.separator;
/*        if (!FileUtils.fileIsExists(groupmembers)) {
            FileUtils.createDir(groupmembers);
        }*/
        // String filePath = groupmembers;
        // File file = new File(filePath, fileName);
        //判断文件名是否为空
        if(!FileUtils.fileIsExists(filePath)){
            FileUtils.createDir(filePath);
        }
        File file;
        if (TextUtils.isEmpty(fileName)) {
            file = new File(filePath + "app_log.txt");
        } else {
            file = new File(filePath, fileName);
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                deleteFile(filePath);
                file.createNewFile();
            }
//            LogUtils.i("文件名字:" + file.getAbsolutePath() + "  类容:" + context);
            FileOutputStream out = null;
            RandomAccessFile raf = null;
            if (append) {
         /*       //如果为追加则在原来的基础上继续写文件
                raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                if (autoLine) {
                    raf.write("\n".getBytes());
                }
                raf.write(context.getBytes());
                raf.close();*/
                JSONException s;
                JSONObject a = new JSONObject("");
                FileWriter filerWriter = new FileWriter(file, append);
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                if (autoLine) {
                    bufWriter.newLine();
                }
                bufWriter.write(context);
                bufWriter.close();
                filerWriter.close();

            } else {
                //重写文件，覆盖掉原来的数据
                out = new FileOutputStream(file);
                out.write(context.getBytes());
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            // LogUtils.logInfo("  err  =  " + e.getMessage());
        }
    }


}
