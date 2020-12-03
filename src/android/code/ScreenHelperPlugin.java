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
/**
 * This class provides helper functions for detecting the device state on a Microsoft Surface Duo
 */
public class ScreenHelperPlugin extends CordovaPlugin {
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
            Log.i(TAG, "System has feature: " + feature);
            return true;
        } else {
            Log.w(TAG, "System missing feature: " + feature);
            return false;
        }
    }

    /**
    * @return co-ordinates of the display mask area in pixels (left, top, width, height, statusBarHeight)
    *         or 0,0,0,0 if there is no display mask (ie. app is not spanned)
    */
    private String getDisplayMask(){
        Activity activity = this.cordova.getActivity();

        DisplayMask displayMask = DisplayMask.fromResourcesRect(activity);
        int orientation = activity.getResources().getConfiguration().orientation;

        List<Rect> masks = displayMask.getBoundingRectsForRotation(orientation);
        Rect mask = new Rect();
        if(!masks.isEmpty()) {
            mask = masks.get(0);
        }

        int statusBarHeight = getStatusBarHeight();

        return String.format("%d,%d,%d,%d,%d", mask.left, mask.top, mask.right - mask.left, mask.bottom - mask.top, statusBarHeight);
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
}
