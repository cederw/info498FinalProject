package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joanzapata.android.iconify.Iconify;

import org.w3c.dom.Text;


public class Connected extends Activity {
    TextView connectionText;
    com.gc.materialdesign.views.ButtonRectangle vibrateButton;
    com.gc.materialdesign.views.ButtonRectangle flashlightButton;
    com.gc.materialdesign.views.ButtonRectangle doodleButton;
    com.gc.materialdesign.views.ButtonRectangle optionalButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        connectionText = (TextView) findViewById(R.id.connectionText);
        vibrateButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.vibrateButton);
        flashlightButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.flashlightButton);
        doodleButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.doodleButton);

        vibrateButton.setRippleSpeed(80f);
        flashlightButton.setRippleSpeed(80f);
        doodleButton.setRippleSpeed(80f);


        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
        Typeface robotoFontItalic = Typeface.createFromAsset(getAssets(),"fonts/Roboto-MediumItalic.ttf");
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        connectionText.setTypeface(robotoFontItalic);

        vibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connected.this, FlashUI.class));
            }
        });

        flashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connected.this, FlashUI.class));
            }
        });
    }
}
