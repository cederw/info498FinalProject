package com.info498.bestgroup.tindar;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.joanzapata.android.iconify.Iconify;

import org.w3c.dom.Text;


public class Connected extends ActionBarActivity {
    TextView connectionText;
    TextView vibrateIcon;
    TextView vibrateText;
    TextView flashlightIcon;
    TextView flashlightText;
    Button optionalButton1;
    Button optionalButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        connectionText = (TextView) findViewById(R.id.connectionText);
        vibrateIcon = (TextView) findViewById(R.id.vibrateIcon);
        vibrateText = (TextView) findViewById(R.id.vibrateText);
        flashlightIcon = (TextView) findViewById(R.id.flashlightIcon);
        flashlightText = (TextView) findViewById(R.id.flashlightText);
        optionalButton1 = (Button) findViewById(R.id.optionalButton1);
        optionalButton2 = (Button) findViewById(R.id.optionalButton2);

        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
        Typeface robotoFontItalic = Typeface.createFromAsset(getAssets(),"fonts/Roboto-BoldItalic.ttf");
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        connectionText.setTypeface(robotoFontItalic);
        vibrateText.setTypeface(robotoFont);
        flashlightText.setTypeface(robotoFont);
        optionalButton1.setTypeface(robotoFont);
        optionalButton2.setTypeface(robotoFont);
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