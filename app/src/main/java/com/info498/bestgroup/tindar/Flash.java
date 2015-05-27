package com.info498.bestgroup.tindar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;


/*

Walter Ceder

 */
public class Flash extends BroadcastReceiver {
    public Flash() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            Camera cam;
            cam = Camera.open();
            Camera.Parameters params = cam.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            cam.setParameters(params);
            cam.startPreview();
            cam.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
            try {
                wait(1000);
            } catch (InterruptedException e) {
                //if it cant wait one second
                e.printStackTrace();
            }
            cam.stopPreview();
            cam.release();

        } else{
            //no flash
        }
    }
}
