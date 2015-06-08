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
    }
<<<<<<< HEAD

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vibrate, menu);
        return true;
    }
}
=======
}
>>>>>>> 78f3e91957923e7a78e3f0bf7018c86cbf9b3d05
