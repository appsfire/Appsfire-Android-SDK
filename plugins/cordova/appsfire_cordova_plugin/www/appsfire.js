var argscheck = require('cordova/argscheck'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec');

var AppsfireSDK = function() {
    this.serviceName = "AppsfireSDK";
};

// Base SDK

AppsfireSDK.prototype.sdk_connectWithApiKey = function(apiKey, successCallback, failureCallback) {
    if (typeof(apiKey) != 'string') apiKey = '';
    exec(successCallback, failureCallback, this.serviceName, 'sdk_connectWithApiKey', [apiKey]);
};

AppsfireSDK.prototype.sdk_setFeatures = function(features) {
    if (typeof(features) != 'object') features = ['AFSDKFeatureEngage', 'AFSDKFeatureMonetization'];
    exec(null, null, this.serviceName, 'sdk_setFeatures', [features]);
};

AppsfireSDK.prototype.sdk_isInitialized = function(successCallback) {
    return exec(successCallback, null, this.serviceName, 'sdk_isInitialized', []);
};

AppsfireSDK.prototype.sdk_presentPanel = function(contenType, styleType, successCallback, failureCallback) {
    if (typeof(contenType) != 'string') contentype = 'notifications';
    if (typeof(styleType) != 'string') styleType = 'default';
    exec(successCallback, failureCallback, this.serviceName, 'sdk_presentPanel', [contenType, styleType]);
};

AppsfireSDK.prototype.sdk_dismissPanel = function() {
    exec(null, null, this.serviceName, 'sdk_dismissPanel', []);
};

AppsfireSDK.prototype.sdk_isDisplayed = function(successCallback) {
    exec(successCallback, null, this.serviceName, 'sdk_isDisplayed', []);
};

AppsfireSDK.prototype.sdk_registerPushToken = function(pushToken, successCallback, failureCallback) {
    if (typeof(pushToken) != 'string') pushToken = 0;
    exec(successCallback, failureCallback, this.serviceName, 'sdk_registerPushToken', [pushToken]);
};

AppsfireSDK.prototype.sdk_handleBadgeCountLocally = function(locally, successCallback, failureCallback) {
    if (typeof(locally) != 'boolean') locally = 0;
    exec(successCallback, failureCallback, this.serviceName, 'sdk_handleBadgeCountLocally', [locally]);
};

AppsfireSDK.prototype.sdk_handleBadgeCountLocallyAndRemotely = function(locallyAndRemotely, successCallback, failureCallback) {
    if (typeof(locallyAndRemotely) != 'boolean') locallyAndRemotely = 0;
    exec(successCallback, failureCallback, this.serviceName, 'sdk_handleBadgeCountLocallyAndRemotely', [locallyAndRemotely]);
};

AppsfireSDK.prototype.sdk_openSDKNotification = function(notificationId, successCallback, failureCallback) {
    if (typeof(notificationId) != 'number') notificationId = 0;
    exec(successCallback, failureCallback, this.serviceName, 'sdk_openSDKNotification', [notificationId]);
};

AppsfireSDK.prototype.sdk_customizeColors = function(backgroundColor, textColor) {
    if (typeof(backgroundColor) != 'object') backgroundColor = '{}';
    if (typeof(textColor) != 'object') textColor = '{}';
    exec(null, null, this.serviceName, 'sdk_customizeColors', [backgroundColor, textColor]);
};

AppsfireSDK.prototype.sdk_setCustomKeyValues = function(customKeyValues, successCallback, failureCallback) {
    if (typeof(customKeyValues) != 'object') customKeyValues = '{}';
    exec(successCallback, failureCallback, this.serviceName, 'sdk_setCustomKeyValues', [customKeyValues]);
};

AppsfireSDK.prototype.sdk_setUserEmail = function(email, isModifiable, successCallback, failureCallback) {
    if (typeof(email) != 'string') email = '';
    if (typeof(isModifiable) != 'boolean') isModifiable = true;
    exec(successCallback, failureCallback, this.serviceName, 'sdk_setUserEmail', [email, isModifiable]);
};

AppsfireSDK.prototype.sdk_showFeedbackButton = function(showButton, successCallback, failureCallback) {
    if (typeof(showButton) != 'boolean') showButton = 'true';
    exec(successCallback, failureCallback, this.serviceName, 'sdk_showFeedbackButton', [showButton]);
};

AppsfireSDK.prototype.sdk_getAFSDKVersionInfo = function(callback) {
    exec(callback, null, this.serviceName, 'sdk_getAFSDKVersionInfo', []);
};

AppsfireSDK.prototype.sdk_numberOfPendingNotifications = function(callback) {
    exec(callback, null, this.serviceName, 'sdk_numberOfPendingNotifications', []);
};

AppsfireSDK.prototype.sdk_getSessionID = function(callback) {
    exec(callback, null, this.serviceName, 'sdk_getSessionID', []);
};

AppsfireSDK.prototype.sdk_resetCache = function() {
    exec(null, null, this.serviceName, 'sdk_resetCache', []);
};

// Ad SDK

AppsfireSDK.prototype.adsdk_prepare = function() {
    return exec(null, null, this.serviceName, 'adsdk_prepare', []);
};

AppsfireSDK.prototype.adsdk_isInitialized = function(successCallback) {
    return exec(successCallback, null, this.serviceName, 'adsdk_isInitialized', []);
};

AppsfireSDK.prototype.adsdk_areAdsLoaded = function(successCallback) {
    return exec(successCallback, null, this.serviceName, 'adsdk_areAdsLoaded', []);
};

AppsfireSDK.prototype.adsdk_setUseInAppDownloadWhenPossible = function(userInAppDownloadWhenPossible, successCallback, failureCallback) {
    if (typeof(userInAppDownloadWhenPossible) != 'boolean') userInAppDownloadWhenPossible = 'true';
    exec(successCallback, failureCallback, this.serviceName, 'adsdk_setUseInAppDownloadWhenPossible', [userInAppDownloadWhenPossible]);
};

AppsfireSDK.prototype.adsdk_setDebugModeEnabled = function(debugModeEnabled, successCallback, failureCallback) {
    if (typeof(debugModeEnabled) != 'boolean') debugModeEnabled = 'true';
    exec(successCallback, failureCallback, this.serviceName, 'adsdk_setDebugModeEnabled', [debugModeEnabled]);
};

AppsfireSDK.prototype.adsdk_requestModalAd = function(modalType, shouldUseTimer) {
    if (typeof(modalType) != 'string') modalType = 'sushi';
    if (typeof(shouldUseTimer) != 'boolean') shouldUseTimer = 'false';
    exec(null, null, this.serviceName, 'adsdk_requestModalAd', [modalType, shouldUseTimer]);
};

AppsfireSDK.prototype.adsdk_isThereAModalAdAvailable = function(callback, modalType) {
    if (typeof(modalType) != 'string') modalType = 'sushi';
    exec(callback, null, this.serviceName, 'adsdk_isThereAModalAdAvailable', [modalType]);
};

AppsfireSDK.prototype.adsdk_cancelPendingAdModalRequest = function() {
    exec(null, null, this.serviceName, 'adsdk_cancelPendingAdModalRequest', []);
};

AppsfireSDK.prototype.adsdk_isModalAdDisplayed = function(callback) {
    exec(callback, null, this.serviceName, 'adsdk_isModalAdDisplayed', []);
};

module.exports = AppsfireSDK;
