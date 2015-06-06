package com.info498.bestgroup.tindar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/*

Walter Ceder

 */
public class Picture extends BroadcastReceiver {
    private Camera cam;
    public Picture() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)&&context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){


            cam = null;
            try {
              //  cam.release();
                cam = Camera.open(); // attempt to get a Camera instance
            }
            catch (Exception e){
                Log.i("CAM", "No camera");
            }

            cam.startPreview();
            cam.takePicture(null,null,mPicture);
            Log.i("CAM","Start?");
            cam.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
            cam.takePicture(null,null,mPicture);
            cam.stopPreview();
            Log.i("CAM","Stop?");
            cam.release();

//            try {
//                wait(1000);
//            } catch (InterruptedException e) {
//                //if it cant wait one second
//                e.printStackTrace();
//            }
//
//            cam.release();

        } else{
            //no flash
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] data, Camera camera){
            File pictureFile = getOutputMediaFile();

            if(pictureFile == null){
                Log.d("TEST", "Error creating media file, check storage permissions");
                return;
            }

            try{
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            }catch(FileNotFoundException e){
                Log.d("TEST","File not found: "+e.getMessage());
            } catch (IOException e){
                Log.d("TEST","Error accessing file: "+e.getMessage());
            }
        }
    };

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");

        return mediaFile;
    }
}
