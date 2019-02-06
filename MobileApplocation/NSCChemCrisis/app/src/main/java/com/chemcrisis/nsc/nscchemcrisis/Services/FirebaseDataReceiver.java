package com.chemcrisis.nsc.nscchemcrisis.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";
    private static String content;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras().get("content") != null) {
//            for (String key : intent.getExtras().keySet()) {
//                Object value = intent.getExtras().get(key);
//                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
//            }

            content = intent.getExtras().get("content").toString();
            Log.i("CONTENT", content + "");
        }
    }

    public static String getContent(){
        return content;
    }

    public static void setContent(){
        content = null;
    }
}
