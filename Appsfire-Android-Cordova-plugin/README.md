Appsire SDK Cordova Plugin (Android)
====================================
This document describes the installation and usage of the Cordova plugin for the Appsfire Android SDK.

## Requirements
- Appsfire SDK >= 2.3.1
- Cordova CLI >= 3.0

## Introduction

This documentation is a brief introduction to Appsfire Android SDK for Cordova. We recommend you to take a look on the general documentation that you can find in your dashboard. Most of methods were implemented in Unity. If you have a problem during the integration, don't hesitate to contact our team at <a href="mailto:support@appsfire.com">support@appsfire.com</a>.

We recommend you to read the integration reference documentation that you find at this [URL](http://docs.appsfire.com/).

## Getting Started with Appsfire
The Appsfire SDK is the cornerstone of the Appsfire network.

It provides the functionalities for monetizing your mobile application: it facilitates inserting native mobile ads into you application using native APIs.
You can choose one of our ad units (Sushi, Uramaki).

It also helps you engage with your users by sending push and in-app notifications.

- Please visit our [website](http://appsfire.com) to learn more about our ad units and products.<br />
- Please visit our [iOS online documentation](http://docs.appsfire.com/sdk/ios/integration-reference/Introduction) to learn how to integrate our SDK into your iOS app.<br />
- Check out the full [iOS API specification](http://docs.appsfire.com/sdk/ios/api-reference/) to have a detailed understanding of our iOS SDK.

## Installation

In order to get started, please be sure you've done the following:

1. Registered on [Appsfire website](http://www.appsfire.com/) and accepted our Terms Of Use
2. Registered your app on our [Dashboard](http://dashboard.appsfire.com/) and generated an SDK key for your app
3. Grabbed our latest version of the SDK and Cordova plugin from our [Dashboard](http://dashboard.appsfire.com/app/doc)

### Using the local folder
```
cordova plugin add PATH/AppsfireSDKPlugin
cordova prepare
```

## API Reference

### Monetization
#### Check if the ad library is initialized
*Method:*  
`adsdk_prepare()`

*Method*  
```js
af.adsdk_prepare();
```

#### Enabling the features you want

*Method:*
`af.sdk_setFeatures(features)`

*Arguments:*
- `features`: Array of strings representing the features your want.
- Possible values are:
  - `AFSDKFeatureEngage`
  - `AFSDKFeatureMonetization`
  - `AFSDKFeatureTrack`

*Usage:*
```js
// Set the features you need.
// You need to call this before `sdk_connectWithApiKey`
af.sdk_setFeatures(['AFSDKFeatureEngage', 'AFSDKFeatureMonetization']);
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
#### Using the in-app overlay
*Method:*  
`adsdk_setUseInAppDownloadWhenPossible(userInAppDownloadWhenPossible, successCallback, failureCallback)`

*Arguments:*  
`userInAppDownloadWhenPossible` : Boolean to use the In-App Download native overlay (as opposed to clicking out to the App Store). (Default is `true`)

*Usage:*  
```js
af.adsdk_setUseInAppDownloadWhenPossible(
    true,
    function(){
        console.log('Using In App Download.');
    },
    function(){
        console.log('Failed to set In App Download usage.');
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


###Engagement (Notification Wall & Feedback Form)
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
#### Presenting the panel for Notifications / Feedback
*Method:*  
`sdk_presentPanel(contentType, styleType, successCallback, failureCallback)`

*Arguments:*  
`contentType`:  
- `notifications`: Displays the Notification Wall.  
- `feedback`: Displays the feedback form only. The notification form won’t be visible.

`styleType`:  
- `default`: The window will be centered and will not be full-screen. Your app remains visible in the background.  
- `fullscreen`: The window will be full screen. Please note that calling the full-screen style isn’t available on iPad and will force the use of the default style.

*Usage:*  
```js
af.sdk_presentPanel(
    'notifications',
    'fullscreen',
    function() {
        console.log('Presented');
    },
    function() {
        console.log('No Presented');
    });
```
#### Closing the Notification Wall / Feedback
*Method:*  
`sdk_dismissPanel()`

*Usage:*  
```js
af.sdk_dismissPanel();
```
#### Check if Appsfire SDK is visible
*Method:*  
`sdk_isDisplayed()`

*Usage:*  
```js
af.sdk_isDisplayed();
```
#### Register the push token for APNS (Apple Push Notification Service)
*Method:*  
`sdk_registerPushToken(pushToken, successCallBack, failureCallBack)`

*Arguments:*  
`pushToken`: The string of the push token received from Apple API.

*Usage:*  
```js
af.sdk_registerPushToken(
    'push_token',
    function() {
        console.log('Push token sent.');
    },
    function() {
        console.log('Push token not sent.');
    });
```

#### Local badge handling
*Method:*  
`sdk_handleBadgeCountLocally(locally, successCallback, failureCallback)`

*Arguments:*  
`locally` : Boolean whether to handle push locally. (Default is `true`)  

*Usage:*  
```js
af.sdk_handleBadgeCountLocally(
    true,
    function() {
        console.log('Handling badge count locally.');
    },
    function() {
        console.log('Failed Handling badge count locally.');
    });
```

#### Local and Remote badge handling
*Method:*  
`sdk_handleBadgeCountLocallyAndRemotely(locallyAndRemotely, successCallback, failureCallback)`

*Arguments:*  
`locallyAndRemotely` : Boolean whether to handle push locally and remotely.  (Default is `false`)

*Usage:*  
```js
af.sdk_handleBadgeCountLocallyAndRemotely(
    true,
    function() {
        console.log('Handling badge count locally and remotely.');
    },
    function() {
        console.log('Failed Handling badge count locally and remotely.');
    });
```

#### Open a specific Notification
*Method:*  
`sdk_openSDKNotification(notificationId, successCallback, failureCallback)`

*Arguments:*  
`notificationId`: Identifier of the notification you want to display.

*Usage:*  
```js
af.sdk_openSDKNotification(
    'the_notification_id',
    function() {
        console.log('open notification request sent.');
    },
    function() {
        console.log('failed to open notification.');
    });
```

#### Color customization
*Method:*  
`sdk_customizeColors(backgroundColor, textColor)`

*Arguments:*  
- `backgroundColor` : Dictionary representation of the color applied to the background.
- `textColor` : Dictionary representation of the color applied to the text.

The color is represented by a dictionnary which have this form:
```js
var whiteColor = {
    'r': '255', // value between 0 and 255
    'g': '255', // value between 0 and 255
    'b': '255', // value between 0 and 255
    'a': '1.0'  // value between 0 and 1
}
```

*Usage:*  
```js
af.sdk_customizeColors({
    'r': '247',
    'g': '148',
    'b': '18',
    'a': '1.0'
}, {
    'r': '255',
    'g': '255',
    'b': '255',
    'a': '1.0'
});
```

#### Sending substitution data to the SDK
*Method:*  
`sdk_setCustomKeyValues(customKeyValues, successCallback, failureCallback)`

*Arguments:*  
`customKeyValues` : Dictionary representation of the custom values sent to the API.

*Usage:*  
```js
af.sdk_setCustomKeyValues({
        "FIRSTNAME": "John",
        "LASTNAME": "Doe",
        "AGE": "42"
    },
    function() {
        console.log('Custom key values set.');
    },
    function() {
        console.log('Fail to set custom key values.');
    });
```

#### Setting the user’s email
###### If you already know the user's email address you can automatically insert it into the email field in the feedback form so the user doesn't have to type it
*Method:*  
`sdk_setUserEmail(email, isModifiable, successCallback, failureCallback)`

*Arguments:*  
- `email`: Email of the user which will be used in the feedback form.  
- `isModifiable`: Boolean which will make the email field of the feedback form visible or not.  

*Usage:*  
```js
af.sdk_setUserEmail(
    'john.doe@lost.io',
    true,
    function() {
        console.log('Email set.');
    },
    function() {
        console.log('Failed to set email.');
    });
```

#### Removing the Feedback button
*Method:*  
`sdk_showFeedbackButton(showButton, successCallback, failureCallback)`

*Arguments:*  
`showButton` : Boolean to show or hide the button. (Default is `true`)

*Usage:*  
```js
af.sdk_showFeedbackButton(
    false,
    function(){
        console.log('Feedback button hidden.');
    },
    function(){
        console.log('Faiuled to hide feedback button.');
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

#### Number of notifications
*Method:*  
`sdk_numberOfPendingNotifications(callback)`

*Usage:*  
```js
af.sdk_numberOfPendingNotifications(function(count) {
    console.log('Pending notifications count: ' + count);
});
```

#### Session Identifier
*Method:*
`sdk_getSessionID(callback)`

*Usage:*  
```js
af.sdk_getSessionID(function(sessionId) {
    console.log('Session Identifier: ' + sessionId);
});
```

#### Resetting the cache **Do not use lightly!**
*Method:*  
`sdk_resetCache()`

*Usage:*  
```js
af.sdk_resetCache();
```
