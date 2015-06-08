package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Slider;


public class Vibrate extends Activity {

    private int sliderCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);
        Slider slider = (Slider) findViewById(R.id.vibrateSlider);

        slider.setValue(2);

        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");

        TextView text = (TextView) findViewById(R.id.vibrateText);
        text.setTypeface(robotoFont);

        slider.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                sliderCount = i;
            }
        });

        final ButtonRectangle vibrateButton = (ButtonRectangle) findViewById(R.id.vibrateButton);


        vibrateButton.setOnClickListener(new View.OnClickListener() {
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
        vibrateButton.setRippleSpeed(80f);
    }
}