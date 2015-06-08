package com.info498.bestgroup.tindar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class Vibrate extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);

        final com.gc.materialdesign.views.ButtonRectangle vibrate = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.vibrateButton);


        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        });

        vibrate.setRippleSpeed(80f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vibrate, menu);
        return true;
    }
}
