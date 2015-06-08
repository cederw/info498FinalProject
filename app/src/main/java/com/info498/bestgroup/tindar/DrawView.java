package com.info498.bestgroup.tindar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.gc.materialdesign.views.ButtonRectangle;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Created by Alex on 5/31/2015.
 */
public class DrawView  extends View {
    public HashSet<View> views;
    public ArrayList<Stroke> strokes;
    public ArrayList<Stroke> undoneStrokes;

    ButtonRectangle undoButton;
    ButtonRectangle redoButton;

    public Paint mPaint;
    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;

    int curColor;
    int curWidth;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        strokes = new ArrayList<Stroke>();
        undoneStrokes = new ArrayList<Stroke>();

        setDrawingCacheEnabled(true);

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        setColor(Color.parseColor("#AED581"));
        setWidth(20);
    }
    public void clearDrawing() {
        setDrawingCacheEnabled(false);

        strokes.clear();
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
        onSizeChanged(width, height, width, height);
        invalidate();

        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStrokes(strokes);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    public void undo(){
        setDrawingCacheEnabled(false);

        undoneStrokes.add(strokes.remove(strokes.size() - 1));

        if(strokes.size() == 0){
            undoButton.setEnabled(false);
        }
        redoButton.setEnabled(true);

        onSizeChanged(width, height, width, height);
        invalidate();

        setDrawingCacheEnabled(true);
        drawStrokes(strokes);
        invalidate();
    }

    public void redo(){
        setDrawingCacheEnabled(false);

        strokes.add(undoneStrokes.remove(undoneStrokes.size() - 1));

        if(undoneStrokes.size() == 0){
            redoButton.setEnabled(false);
        }
        undoButton.setEnabled(true);

        onSizeChanged(width, height, width, height);

        setDrawingCacheEnabled(true);
        drawStrokes(strokes);
        invalidate();
    }

    public void drawStrokes(ArrayList<Stroke> strokes){
        for(Stroke s : strokes) {
            mPaint.setStrokeWidth(s.strokeWidth);
            mPaint.setColor(s.color);
            mCanvas.drawPath(s.mPath, mPaint);
        }
        mPaint.setColor(curColor);
        mPaint.setStrokeWidth(curWidth);
    }

    public void setColor(int color){
        mPaint.setColor(color);
        curColor = color;
    }

    public void setWidth(int width){
        mPaint.setStrokeWidth(width);
        curWidth = width;
    }

    public byte[] send(){
        invalidate();
        Bitmap whatTheUserDrewBitmap = getDrawingCache();

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        whatTheUserDrewBitmap =
                ThumbnailUtils.extractThumbnail(whatTheUserDrewBitmap, width, height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        whatTheUserDrewBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);

        byte[] yourByteArray;
        yourByteArray = baos.toByteArray();

        return yourByteArray;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    public void hideViews(){
        for(View v : views){
            v.setVisibility(View.INVISIBLE);
        }
    }

    public void showViews(){
        for(View v : views){
            v.setVisibility(View.VISIBLE);
        }
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);

        mCanvas.drawPath(mPath, mPaint);

        Stroke mStroke = new Stroke(mPath, curColor, curWidth);

        strokes.add(mStroke);
        undoButton.setEnabled(true);

        undoneStrokes.clear();
        redoButton.setEnabled(false);

        mPath = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hideViews();
                touch_start(x, y);
                touch_move(x, y + 4f);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                showViews();
                touch_up();
                mCanvas.save();
                invalidate();
                break;
        }
        return true;
    }
}

