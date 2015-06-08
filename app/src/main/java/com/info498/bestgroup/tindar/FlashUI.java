package com.info498.bestgroup.tindar;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;


public class FlashUI extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_ui);

        final com.gc.materialdesign.views.ButtonRectangle flashOnce = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.flashOnce);
        final com.gc.materialdesign.views.ButtonRectangle flashThrice = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.flashThrice);
        final com.gc.materialdesign.views.ButtonRectangle flashTence = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.flashTence);

        flashOnce.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tindar.ConnectedThread connectedThread = ((Tindar)getApplication()).connectedThread;
                if (connectedThread.isAlive()) {
                    byte[] word = "flash".getBytes();
                    connectedThread.write(word);
                }
            }
        });

        flashThrice.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i<4; i++) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Tindar.ConnectedThread connectedThread = ((Tindar) getApplication()).connectedThread;
                                    if (connectedThread.isAlive()) {
                                        byte[] word = "flash".getBytes();
                                        connectedThread.write(word);
                                    }
                                }
                            },
                            i * 1000);
                }
            }
        });

        flashTence.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tindar.ConnectedThread connectedThread = ((Tindar) getApplication()).connectedThread;
                for (int i = 1; i < 11; i++) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Tindar.ConnectedThread connectedThread = ((Tindar) getApplication()).connectedThread;
                                    if (connectedThread.isAlive()) {
                                        byte[] word = "flash".getBytes();
                                        connectedThread.write(word);
                                    }
                                }
                            },
                            i * 1000);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}