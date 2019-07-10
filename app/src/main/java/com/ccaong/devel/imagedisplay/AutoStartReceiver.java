package com.ccaong.devel.imagedisplay;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    //开机后，系统发送的广播

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction()) || Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
            Intent it = new Intent(context.getApplicationContext(), MainActivity.class);
            it.setAction("android.intent.action.MAIN");
            it.addCategory("android.intent.category.LAUNCHER");
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(it);
            PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, it, 0);
            try {
                pendingIntent.send();

            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }
}
