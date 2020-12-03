# Surface Duo Screen Helper for Cordova plugin

## How To Use

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

    window.ScreenHelper.isDualScreenDevice(
        function(result) { document.getElementById('dualscreen').innerText = 'isSurfaceDuo: ' + result; },
        function(error)  { document.getElementById('dualscreen').innerText = 'isSurfaceDuo: error ' + error; }
    );
}

function onResize() {
    window.ScreenHelper.getHinge(
        function(result) { document.getElementById('hinge').innerText = 'hinge: ' + result; },
        function(error)  { document.getElementById('hinge').innerText = 'hinge: error ' + error; }
    );
}
```
