package co.conceptdev;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;

import com.microsoft.device.display.DisplayMask;
import android.graphics.Rect;
import java.util.List;

import android.util.DisplayMetrics;
/**
 * This class provides helper functions for detecting the device state on a Microsoft Surface Duo:
 * - isDeviceSurfaceDuo: true or false
 * - getDisplayMask: returns a Rect coordinates string plus statusbarheight
 * - getStatusBarHeight: separately query statusbarheight
 * 
 * The getDisplayMask method requires displaymask.jar which only works on Surface Duo. 
 */
public class ScreenHelperPlugin extends CordovaPlugin {
    /**
     * Called by Cordova
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "execute action: " + action);
        
        // Route the action
        if (action.equals("isDualScreenDevice")) {
            callbackContext.success(""+isDeviceSurfaceDuo());
            return true;
        }
        if (action.equals("getHinge")) {
            callbackContext.success(getDisplayMask());
            return true;
        }
        if (action.equals("getStatusBarHeight")) {
            callbackContext.success(""+getStatusBarHeight());
            return true;
        }

        // Action not found
        callbackContext.error("action "+action+" not recognized");
        return false;
    }

    static final String TAG = "ScreenHelperCordovaPlugin";

    /**
    * @return true if this is a Surface Duo device, false otherwise
    */
    private boolean isDeviceSurfaceDuo(){
        String feature = "com.microsoft.device.display.displaymask";
        Activity activity = this.cordova.getActivity();
        PackageManager pm = activity.getPackageManager();

        if (pm.hasSystemFeature(feature)) {
            Log.i(TAG, "System has feature: " + feature + " | isDeviceSurfaceDuo==true");
            return true;
        } else {
            Log.w(TAG, "System missing feature: " + feature + " | isDeviceSurfaceDuo==false");
            return false;
        }
    }

    /**
    * @return co-ordinates of the display mask area in dp (left, top, width, height, statusBarHeight)
    *         or 0,0,0,0 if there is no display mask (ie. app is not spanned)
    */
    private String getDisplayMask(){
        // if device is NOT a Surface Duo, return no display mask
        if (!isDeviceSurfaceDuo()) return "0,0,0,0,0";
        
        Activity activity = this.cordova.getActivity();

        DisplayMask displayMask = DisplayMask.fromResourcesRect(activity);
        int orientation = activity.getResources().getConfiguration().orientation;

        List<Rect> masks = displayMask.getBoundingRectsForRotation(orientation);
        Rect mask = new Rect();
        if(!masks.isEmpty()) {
            mask = masks.get(0);
        }

        int statusBarHeight = getStatusBarHeight();

        float density = getDisplayMetricsDensity();
        
        return String.format("%d,%d,%d,%d,%d", 
            Math.round(mask.left / density), 
            Math.round(mask.top / density), 
            Math.round((mask.right - mask.left) / density), 
            Math.round((mask.bottom - mask.top) / density), 
            Math.round(statusBarHeight / density));
    }

    /**
    * @return height of the status bar above the Cordova viewport (60px by default)
    *         warning: this is not the recommended way to determine insets
    */
    public int getStatusBarHeight() {
        Activity activity = this.cordova.getActivity();

        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /** 
     * @return screen pixel density (eg. 2.5 for Surface Duo) to convert px values to dp
     */
    private float getDisplayMetricsDensity() {
        Activity activity = this.cordova.getActivity();

        DisplayMetrics metrics = new DisplayMetrics();
        return activity.getResources().getDisplayMetrics().density;
      }
}
