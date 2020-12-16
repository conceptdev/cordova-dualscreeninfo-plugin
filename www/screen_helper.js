var ScreenHelper = function() {};

/** Useful for debugging */
ScreenHelper.prototype.getVersion = function () {
    return "0.0.2";
}

/** This is really only checking for Surface Duo feature string */
ScreenHelper.prototype.isDualScreenDevice = function(success, fail) {
    cordova.exec(success, fail, "ScreenHelperPlugin","isDualScreenDevice", []);
};

/** Better name for `isDualScreenDevice` 
 * @returns true if a Surface Duo, false otherwise
 */
ScreenHelper.prototype.isDeviceSurfaceDuo = function(success, fail) {
    cordova.exec(success, fail, "ScreenHelperPlugin","isDualScreenDevice", []);
};

/** Dimensions of the area 'masked' by the hinge, if present because the app is spanned
 * @returns Coordinates of masked area "x,y,width,height,statusbarheight", or "0,0,0,0,0" if the app isn't spanned or device is not a Surface Duo
 */
ScreenHelper.prototype.getHinge = function(success, fail) {
    cordova.exec(success, fail, "ScreenHelperPlugin","getHinge", []);
};

/** Height of the status bar at the top of the screen (to adjust for in dual-landscape mode)
 * @returns integer
 */
ScreenHelper.prototype.getStatusBarHeight = function(success, fail) {
    cordova.exec(success, fail, "ScreenHelperPlugin","getStatusBarHeight", []);
};

var screenHelper = new ScreenHelper();
module.exports = screenHelper;