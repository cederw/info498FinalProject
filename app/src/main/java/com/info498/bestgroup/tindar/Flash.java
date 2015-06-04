package com.info498.bestgroup.tindar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;


/*

Walter Ceder

 */
public class Flash extends BroadcastReceiver {
    private static Camera cam;
    public Flash() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)&&context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){

            if(cam !=null){
                cam.stopPreview();
                cam.release();
            }
            cam = null;
            try {
              //  cam.release();
                cam = Camera.open(); // attempt to get a Camera instance
            }
            catch (Exception e){
                Log.i("CAM", "No camera");
            }

            Camera.Parameters params = cam.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(params);
            cam.startPreview();
            Log.i("CAM","Start?");
            cam.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            cam.stopPreview();
                            Log.i("CAM","Stop?");
                            cam.release();
                        }
                    },
                    300);
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
}
