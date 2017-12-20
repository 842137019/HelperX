package com.cc.task.helperx.utils;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.task.TimeTask;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookUtils {

    public static void HookAndChange(ClassLoader classLoader) {
//        LogUtils.i("HookAndChange");
//        Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
//        if (obj != null) {
//            LogUtils.i("obj = " + obj);
//            TaskEntry newTask = (TaskEntry) obj;
////            LogUtils.i("LastLocation = " + newTask.getGpsloction());
//        }
        XposedHelpers.findAndHookMethod("android.telephony.PhoneStateListener", classLoader,
                "onCellLocationChanged", CellLocation.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(null);
                    }
                });

        XposedBridge.hookAllMethods(TelephonyManager.class,
                "getCellLocation", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(null);
                    }
                });

        XposedBridge.hookAllMethods(TelephonyManager.class,
                "getNeighboringCellInfo", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(null);
                    }
                });

        XposedBridge.hookAllMethods(LocationManager.class, "getProviders", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("gps");
                param.setResult(arrayList);
            }
        });

        XposedBridge.hookAllMethods(LocationManager.class, "getBestProvider", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("gps");
            }
        });


        XposedBridge.hookAllMethods(LocationManager.class, "getLastLocation", new XC_MethodHook() {
            @SuppressLint("NewApi")
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
                if (obj != null) {
                    TaskEntry newTask = (TaskEntry) obj;
                    LogUtils.i("LastLocation = " + newTask.getGpsloction());
                    String[] gpsloction = newTask.getGpsloction().split(",");
                    if (gpsloction.length > 1) {
                        Location l = new Location(LocationManager.GPS_PROVIDER);
                        l.setLatitude(Double.valueOf(gpsloction[0]));
                        l.setLongitude(Double.valueOf(gpsloction[1]));
                        l.setAccuracy(100f);
                        l.setTime(System.currentTimeMillis());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            l.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        }
                        param.setResult(l);
                    }
                }
            }
        });

        XposedBridge.hookAllMethods(LocationManager.class, "getLastKnownLocation", new XC_MethodHook() {
            @SuppressLint("NewApi")
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
                if (obj != null) {
                    TaskEntry newTask = (TaskEntry) obj;
                    String[] gpsloction = newTask.getGpsloction().split(",");
                    LogUtils.i(" LastKnownLocation = " + newTask.getGpsloction() + " [0] = " + gpsloction[0] + " [1] = " + gpsloction[1]);
                    if (gpsloction.length > 1) {
                        Location l = new Location(LocationManager.GPS_PROVIDER);
                        l.setLatitude(Double.valueOf(gpsloction[0]));
                        l.setLongitude(Double.valueOf(gpsloction[1]));
                        l.setAccuracy(100f);
                        l.setTime(System.currentTimeMillis());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            l.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        }
                        param.setResult(l);
                    }
                }
            }
        });

        XposedBridge.hookAllMethods(LocationManager.class, "requestLocationUpdates", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object tagert = null;
                for (int i = 0; i < param.args.length; i++) {
                    tagert = param.args[i];
                    if (tagert instanceof LocationListener) {
                        break;
                    }
                }
                if (tagert != null) {
                    LocationListener locationListener = (LocationListener) tagert;
                    updateLocation(locationListener);
                    locationThread.setLocationListener(locationListener);
                    timer.schedule(locationThread, 800L, 500L);
                }
            }
        });

    }

    private static TimeManager timer = new TimeManager();

    private static LocationThread locationThread = new LocationThread();

    private static class LocationThread extends TimeTask {

        public void setLocationListener(LocationListener locationListener) {
            this.locationListener = locationListener;
        }

        private LocationListener locationListener;

        public LocationThread() {
        }

        public void run() {
            updateLocation(locationListener);
        }
    }

    private static void updateLocation(LocationListener locationListener) {
        Object obj = Utils.getObject(TaskEntry.class.getSimpleName());
        if (obj != null && locationListener != null) {
            TaskEntry newTask = (TaskEntry) obj;
            String[] gpsloction = newTask.getGpsloction().split(",");
            if (gpsloction.length > 1) {
                double latitude = Double.valueOf(gpsloction[0]);
                double longtitude = Double.valueOf(gpsloction[1]);
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(latitude);
                location.setLongitude(longtitude);
                location.setAccuracy(10.00f);
                location.setTime(System.currentTimeMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }
                XposedHelpers.callMethod(locationListener, "onLocationChanged", location);
            }
        }
    }

    /**
     * 发送消息，不经过聊天界面，直接发送
     *
     * @param recvUsername
     * @param msg
     * @return
     * @throws Exception
     */
    public static void sendMessage(ClassLoader clsLoader, String recvUsername, String msg) throws Exception {
        Constructor<?> c = clsLoader.loadClass("com.tencent.mm.modelmulti.i").getDeclaredConstructor(String.class, String.class, int.class);
        c.setAccessible(true);
        Object i = c.newInstance(recvUsername, msg, 1);
        Object obj = XposedHelpers.callStaticMethod(clsLoader.loadClass("com.tencent.mm.model.ak"), "vy");
        XposedHelpers.callMethod(obj, "a", i, 0);
    }
}
