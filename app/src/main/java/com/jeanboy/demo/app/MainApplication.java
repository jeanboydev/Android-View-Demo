package com.jeanboy.demo.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;

import com.jeanboy.demo.ui.activity.SplashActivity;

import java.util.Iterator;
import java.util.List;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public class MainApplication extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        if (isRemote()) {
            return;
        }
    }

    public boolean isRemote() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !getPackageName().equals(processAppName)) {
            return true;
        }
        return false;
    }

    protected String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.exit(0);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
