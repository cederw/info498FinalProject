package com.info498.bestgroup.tindar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.widgets.ColorSelector;

import java.util.HashSet;


public class Doodling extends Activity {

    //private DrawingManager mDrawingManager = null;
    Slider strokeWidthSlider;
    ButtonRectangle clearButton;
    ButtonRectangle colorButton;
    ButtonRectangle redoButton;
    ButtonRectangle undoButton;
    ButtonFloat sendButton;
    RelativeLayout sliderGroup;
    LinearLayout undoRedoButtons;

    DrawView drawView;
    Context thisActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodling);

        drawView = (DrawView) findViewById(R.id.canvas);

        strokeWidthSlider = (Slider) findViewById(R.id.strokeWidthSlider);
        clearButton = (ButtonRectangle) findViewById(R.id.clearButton);
        colorButton = (ButtonRectangle) findViewById(R.id.colorButton);
        redoButton = (ButtonRectangle) findViewById(R.id.redoButton);
        undoButton = (ButtonRectangle) findViewById(R.id.undoButton);
        undoRedoButtons = (LinearLayout) findViewById(R.id.undoRedoButtons);
        sliderGroup = (RelativeLayout) findViewById(R.id.sliderGroup);
        sendButton = (ButtonFloat) findViewById(R.id.sendButton);

        HashSet<View> views = new HashSet<View>();
        views.add(sliderGroup);
        views.add(clearButton);
        views.add(colorButton);
        views.add(sendButton);
        views.add(undoRedoButtons);

        drawView.views = views;
        drawView.redoButton = redoButton;
        drawView.undoButton = undoButton;

        strokeWidthSlider.setValue(20);
        colorButton.setBackgroundColor(Color.parseColor("#AED581"));
        redoButton.setEnabled(false);
        undoButton.setEnabled(false);

        strokeWidthSlider.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                drawView.setWidth(i);
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorSelector colorSelector = new ColorSelector(thisActivity, drawView.curColor, new ColorSelector.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int i) {
                        colorButton.setBackgroundColor(i);
                        drawView.setColor(i);
                    }
                });
                colorSelector.show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clearDrawing();
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tindar.ConnectedThread connectedThread = ((Tindar) getApplication()).connectedThread;
                if (connectedThread != null) {
                    String temp = drawView.send();
                    Log.i("Doodle",temp );

//                    int index = 0;
//                        while(index<temp.length()){
//                            String temp2;
//                            if(index+100>=temp.length()){
//                                temp2 = temp.substring(index,temp.length());
//                            } else{
//                                temp2 = temp.substring(index,index+100);
//                            }
//                            String id = "DOODLEIDLENGTH";
//                            if(temp2.length()>=100){
//                                id = id+"00";
//                            } else {
//                                id = id+id.length();
//                            }
//                            connectedThread.write((id+temp2).getBytes());
//                            index +=100;
//                        }
                    if(!temp.contains("=")){
                        temp = temp+"===";
                    }
                    connectedThread.write(("doodle "+temp ).getBytes());



                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth connection lost", Toast.LENGTH_LONG).show();
                }

            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.undo();
            }
        });

        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.redo();
            }
        });

    }
}