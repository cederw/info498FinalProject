package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Slider;


public class FlashUI extends Activity {

    private int sliderCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_ui);

        final ButtonRectangle flashButton = (ButtonRectangle) findViewById(R.id.flashlightButton);
        Slider slider = (Slider) findViewById(R.id.flashlightSlider);
        slider.setValue(3);

        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");

        TextView text = (TextView) findViewById(R.id.flashText);
        text.setTypeface(robotoFont);


        slider.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                sliderCount = i;
            }
        });

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(275); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        flashButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashButton.startAnimation(animation);
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