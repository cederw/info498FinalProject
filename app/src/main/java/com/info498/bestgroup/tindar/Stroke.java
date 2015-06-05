package com.info498.bestgroup.tindar;

import android.graphics.Path;

/**
 * Created by Alex on 6/3/2015.
 */
public class Stroke {
    Path mPath;
    int color;
    int strokeWidth;
    public Stroke(Path mPath, int color, int strokeWidth){
        this.mPath = mPath;
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
}
