package com.example.pngsequenceexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageAnim extends View {
    private final String TAG = "ImageAnim";
    Bitmap background;
    Bitmap[] icons = new Bitmap[47];
    int planeFragmes = 0,iconsCount = 0;
    Handler handler;
    Runnable runnable;
    final int UPDATE_MILLIES = 30;

    public ImageAnim(Context context) {
        super(context);
        init(null,0);
    }

    public ImageAnim(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public ImageAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ImageAnim, defStyle, 0);
        String icon_path = a.getString(R.styleable.ImageAnim_icon_type);

        handler = new Handler();
        String folder_name = icon_path+"_";
        iconsCount = 0;
        for (int i = 0; i < 47; i++) {

            String sequnce = i < 10
                    ? "0000" : "000";

            addPngSequenceInArray(
                    getContext(),
                    47,
                    folder_name+sequnce
            );
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        addImageonCanvas();
    }

    private void addPngSequenceInArray(
            Context context,
            int totalIcons,
            String folder_name){
        try
        {

            Log.d(TAG,""+folder_name+iconsCount+".png");

            // get input stream
            InputStream ims = context
                    .getAssets()
                    .open(folder_name+iconsCount+".png");
            // load image as bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(ims);
            // set image to ImageView
            icons[iconsCount] = bitmap;
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
        iconsCount++;
        if(iconsCount >= totalIcons)
        {
            iconsCount = 0;
        }
    }


    Canvas canvas;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        addImageonCanvas();
        handler.postDelayed(runnable,UPDATE_MILLIES);
    }

    private void addImageonCanvas(){
        planeFragmes++;
        if(planeFragmes == 47)
            planeFragmes = 0;

        Bitmap iconBitmap = icons[planeFragmes];

//        Log.d(TAG,"ImageAnim : "+planeFragmes
//                +" | "+iconBitmap);

        if(iconBitmap != null && canvas != null)
        {
            canvas.drawBitmap(
                    iconBitmap,
                    0,
                    0,
                    null
            );
        }
    }
}
