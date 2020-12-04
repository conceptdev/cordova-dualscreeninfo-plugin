# Surface Duo Screen Helper for Cordova plugin

This Cordova plugin can be used to enhance HTML+JS layouts for the dual-screen Surface Duo.

## How to use

```dotnetcli
cordova plugin add https://github.com/conceptdev/cordova-dualscreeninfo-plugin/
```

Add placeholders in HTML:

```html
<div id="dualscreen"><p>...</p></div>
<div id="hinge"><p>...</p></div>
```

Wire up events to check status. Note that the hinge is not detected on a single screen - the dimensions returned will be `0,0,0,0`.

```javascript
document.addEventListener('deviceready', onDeviceReady, false);
window.addEventListener('resize', onResize, true);

function onDeviceReady() {
    // Cordova is now initialized. Have fun!
    // ...

    // returns true or false
    // can be checked in onDeviceReady as the value will never change
    window.ScreenHelper.isDualScreenDevice(
        function(result) { document.getElementById('dualscreen').innerText = 'isSurfaceDuo: ' + result; },
        function(error)  { document.getElementById('dualscreen').innerText = 'isSurfaceDuo: error ' + error; }
    );
}

function onResize() {
    // returns coordinates of hinge area (left, top, width, height, statusbarheight)
    // or 0,0,0,0,? if the app is not spanned
    // should be checked in onResize because the hinge will only be present when spanned
    // and the position will change with device orientation
    // use the statusbarheight in double-landscape (tall) mode to shift the hinge up to account for the status bar
    window.ScreenHelper.getHinge(
        function(result) { document.getElementById('hinge').innerText = 'hinge: ' + result; },
        function(error)  { document.getElementById('hinge').innerText = 'hinge: error ' + error; }
    );
}
```

## Resources

- [Demo Cordova apps](https://github.com/conceptdev/cordova-samples) that use this plugin
- [Surface Duo developer documentation](https://docs.microsoft.com/dual-screen/)
- [Surface Duo developer blog](https://devblogs.microsoft.com/surface-duo/)
