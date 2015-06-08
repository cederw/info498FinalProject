package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class FlashUI extends Activity {

    private int sliderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_ui);

        final com.gc.materialdesign.views.ButtonRectangle flashButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.flashlightButton);
        RelativeLayout sliderGroup = (RelativeLayout) findViewById(R.id.sliderGroup);


        flashButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i <= sliderCount; i++) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Tindar.ConnectedThread connectedThread = ((Tindar) getApplication()).connectedThread;
                                    if (connectedThread != null) {
                                        connectedThread.write("flash".getBytes());
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Bluetooth connection lost", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            i * 1000);
                }
            }
        });
    }
}