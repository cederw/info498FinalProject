package com.info498.bestgroup.tindar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;


public class VibrateReceiver extends BroadcastReceiver {

    public VibrateReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        int vibrateTime = intent.getIntExtra("vibrateTime", 2);

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(vibrateTime * 1000);
    }
}