package com.example.myapplication;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class PushUtils {
    private static final String TAG = PushUtils.class.getSimpleName();
    private static PowerManager.WakeLock mWakeLock;
    public static void acquireWakeLock(Context context) {
        Log.i(TAG, "acquireWakeLock : " + mWakeLock);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "bokjiro:tags");
        mWakeLock.acquire(10*60*1000L /*10 minutes*/);
        Log.i(TAG, "mWakeLock mWakeLock : " + mWakeLock);
    }

    public static void releaseWakeLock(){
        if(mWakeLock != null){
            mWakeLock.release();
            mWakeLock = null;
        }
        Log.i(TAG, "mWakeLock : "+mWakeLock);
    }


}
