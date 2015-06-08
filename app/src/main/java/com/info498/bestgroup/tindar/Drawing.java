package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Drawing extends Activity {

    CanvasView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        Intent intent = getIntent();

        String encodedString = intent.getStringExtra("bitmap");
        pic = new CanvasView(this, encodedString);
        setContentView(pic);
    }




}
