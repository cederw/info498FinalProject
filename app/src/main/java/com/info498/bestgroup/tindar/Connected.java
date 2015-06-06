package com.info498.bestgroup.tindar;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joanzapata.android.iconify.Iconify;

import org.w3c.dom.Text;


public class Connected extends ActionBarActivity {
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
        optionalButton2 = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.optionalButton2);

        vibrateButton.setRippleSpeed(80f);
        flashlightButton.setRippleSpeed(80f);
        doodleButton.setRippleSpeed(80f);
        optionalButton2.setRippleSpeed(80f);


        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
        Typeface robotoFontItalic = Typeface.createFromAsset(getAssets(),"fonts/Roboto-MediumItalic.ttf");
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        connectionText.setTypeface(robotoFontItalic);

        vibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vibrateIntent = new Intent(getApplicationContext(), Vibrate.class);
                startActivity(vibrateIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connected, menu);
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
