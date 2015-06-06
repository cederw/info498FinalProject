package com.info498.bestgroup.tindar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Home extends Activity {
    TextView title;
    TextView tagline;
    TextView search;
    com.gc.materialdesign.views.ButtonRectangle findButton;
    com.gc.materialdesign.views.ButtonRectangle stopButton;
    com.gc.materialdesign.views.ProgressBarCircularIndeterminate spinner;
    AnimatorSet oa = new AnimatorSet();
    boolean animating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        title = (TextView) findViewById(R.id.title);
        tagline = (TextView) findViewById(R.id.tagline);
        search = (TextView) findViewById(R.id.search);
        findButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.findButton);
        stopButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.stopButton);
        spinner = (com.gc.materialdesign.views.ProgressBarCircularIndeterminate) findViewById(R.id.spinner);

        title.setTypeface(robotoFont);
        tagline.setTypeface(robotoFont);
        search.setTypeface(fontAwesome);

        findButton.setRippleSpeed(80f);
        stopButton.setRippleSpeed(80f);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findButton.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                animating = true;
                animateSearch();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
                animating = false;
            }
        });
    }

    public void animateSearch(){
        final float x = search.getX();
        final float y = search.getY();

        oa.playSequentially(ObjectAnimator.ofFloat(search, "x", 150), // anim 1
                ObjectAnimator.ofFloat(search, "y", 550), // anim 2
                ObjectAnimator.ofFloat(search, "x", x), // anim 3
                ObjectAnimator.ofFloat(search, "y", y)); // anim 4
        oa.setDuration(425);

        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                if(animating) {
                    oa.start();
                }else{
                    findButton.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        oa.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
