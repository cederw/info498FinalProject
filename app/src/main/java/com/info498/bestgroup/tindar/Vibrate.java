package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


public class Vibrate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);

        final com.gc.materialdesign.views.ButtonRectangle vibrate = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.vibrateButton);


        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tindar.ConnectedThread connectedThread = ((Tindar)getApplication()).connectedThread;
                connectedThread.write("vibrate".getBytes());
            }
        });

        vibrate.setRippleSpeed(80f);
        vibrate.setY(268);

        vibrate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrate.setY(312);
                    vibrate.setBackgroundColor(Color.parseColor("#008E80"));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    vibrate.setY(300);
                    vibrate.setBackgroundColor(Color.parseColor("#009688"));
                }
                return true;
            }
        });
    }
}
