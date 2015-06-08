package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class Vibrate extends Activity {

    private int sliderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);
        final RelativeLayout sliderGroup = (RelativeLayout) findViewById(R.id.sliderGroup);

        final com.gc.materialdesign.views.ButtonRectangle vibrate = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.vibrateButton);


        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tindar.ConnectedThread connectedThread = ((Tindar) getApplication()).connectedThread;
                if (connectedThread != null) {
                    connectedThread.write("vibrate".getBytes());
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth connection lost", Toast.LENGTH_LONG).show();
                }
            }
        });
        vibrate.setRippleSpeed(80f);
    }
}