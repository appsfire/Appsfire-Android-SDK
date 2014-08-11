#Appsire SDK Cordova Plugin
This document describes the installation and usage of the Cordova plugin for the Appsfire SDK.

##Requirements
- Appsfire SDK >= 2.0.0
- Cordova CLI >= 3.0

## Installation

### Using the local folder
```
cordova plugin add PATH/appsfire_cordova_plugin
cordova prepare
```

## API Reference

#### Initialization of the SDK

*Method:*  
`sdk_connectWithApiKey()`

*Usage:*  
```js
var apiKey = 'YOUR_API_KEY';
var af = new AppsfireSDK();

af.sdk_connectWithApiKey(
    apiKey,
    function() {
        console.log('Connected to the API.');
    },
    function() {
        console.log('Not connected to the API.');
    });
```

#### Checking if the SDK is initialized
*Method:*  
`af.sdk_isInitialized(callback)`

*Usage:*  
```js
af.sdk_isInitialized(function(isInitialized) {
    console.log('Is SDK initialized: ' + isInitialized);
});
```

#### Appsfire SDK version
*Method:*  
`sdk_getAFSDKVersionInfo(callback)`

*Usage:*  
```js
af.sdk_getAFSDKVersionInfo(function(version) {
    console.log('Appsfire SDK Version: ' + version);
});
```

#### Preload ads
*Method:*  
`adsdk_prepare()`

*Method*  
```js
af.adsdk_prepare();
```

#### Cheking if the ad library is initialized
*Method:*  
`adsdk_isInitialized(callback)`

*Usage:*  
```js
af.adsdk_isInitialized(function(isInitialized) {
    console.log('Is Ad SDK initialized: ' + isInitialized);
});
```

#### Cheking if ads have been loaded from the web service
*Method:*  
`adsdk_areAdsLoaded(callback)`

*Usage:*  
```js
af.adsdk_areAdsLoaded(function(areAdsLoaded){
    console.log('Are Ads loaded: ' + areAdsLoaded);
});
```

#### Test your implementation via “debug” mode
*Method:*  
`adsdk_setDebugModeEnabled(debugModeEnabled, successCallback, failureCallback)` (Default is `false`)

*Arguments:*  
`debugModeEnabled`: Boolean to activate/deactivate debug mode. It's useful if you want to test your implementation because it will generate test ads. **Don't forget to de-activate in production!** (Default is `false`)

*Usage:*  
```js
af.adsdk_setDebugModeEnabled(
    false,
    function(){
        console.log('Debug mode set to off.');
    },
    function(){
        console.log('Failed to set debug mode.');
    });
```

#### Request a modal ad
*Method:*  
`adsdk_requestModalAd(modalType, shouldUseTimer)`

*Arguments:*  
-`modalType`: String which allows you to choose between the `sushi` and `uramaki` formats. (Default is `sushi`).
-`shouldUseTimer`: Boolean to activate/deactivate the timer before showing the ad. (Default is `false`).

*Usage:*  
```js
af.ad.adsdk_requestModalAd('sushi', false);
```

#### Check if there is a modal ad to display
*Method:*  
`adsdk_isThereAModalAdAvailable(modalType, callback)`

*Arguments:*  
-`modalType`: String which correspond to the ad unit format you want to check, should be `sushi` or `uramaki` ad formats. (Default is `sushi`).

*Usage:*  
```js
af.adsdk_isThereAModalAdAvailable('uramaki', function(isThereAModalAdAvailable){
    console.log('Is there a modal Ad available: ' + isThereAModalAdAvailable);
});
```

#### Cancel a pending request
*Method:*  
`adsdk_cancelPendingAdModalRequest()`

*Usage:*  
```js
af.adsdk_cancelPendingAdModalRequest();
```

#### Check if a modal ad is currently displayed
*Method:*  
`adsdk_isModalAdDisplayed(callback)`

*Usage:*  
```js
af.adsdk_isModalAdDisplayed(function(isModalAdDisplayed){
    console.log('Is modal Ad displayed: ' + isModalAdDisplayed);
});
```
