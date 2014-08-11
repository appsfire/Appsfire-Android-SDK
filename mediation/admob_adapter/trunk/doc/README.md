# Appsfire AdUnit and AdMob Mediation

Appsfire's AdUnit integration with AdMob is quite easy. AdMob provides a simple way with custom events to plug their SDK with our AdUnit.

## Getting Started with Appsfire
The Appsfire Android SDK is the cornerstone of the Appsfire network.

It provides the functionalities for monetizing your mobile application: it facilitates inserting native mobile ads into you application using native APIs. You can choose one of our ad units (Sushi, Uramaki).

It also helps you engage with your users by sending push and in-app notifications.

- Please visit our [website](http://appsfire.com) to learn more about our ad units and products.<br />
- Please visit our [online documentation](http://docs.appsfire.com/sdk/android/integration-reference/Introduction) to learn how to integrate our SDK into your app.<br />
- Check out the full [API specification](http://docs.appsfire.com/sdk/android/api-reference/) to have a detailed understanding of our SDK.

## Installation

In order to get started, please be sure you've done the following:

1. Registered on [Appsfire website](http://www.appsfire.com/) and accepted our Terms Of Use
2. Registered your app on our [Dashboard](http://dashboard.appsfire.com/) and generated an SDK key for your app
3. Grabbed our latest version of the SDK, either using CocoaPods, or downloading the SDK from our [Dashboard](http://dashboard.appsfire.com/app/doc)

## Quick Start
1. First you need to install the Appsfire SDK in order to use the Appsfire AdUnit. If you are not familiar with it, you can take a look at the MoPub Mediation Demo project bundled in this package.
2. Add admob_adapter/trunk/src as external sources to your Eclipse project that makes use of Admob (for instance aliased as src_adapter)
3. On the AdMob web interface, create a new network in the Monetize section. A popup will be presented to you where you can pick a network, instead click on Custom event and enter com.appsfire.mediation.AdmobInterstitialAdapter then put {"sdkKey": "INSERT_YOUR_API_KEY", "isDebug": "1"} in the Parameter section. To turn off debug mode when you release your app, use "0" instead of "1" for isDebug.

1. First you need to **install the Appsfire SDK** in order to use the Appsfire AdUnit. If you are not familiar with it, you can take a look at the AdMob Mediation Demo project bundled in this package.

2. Add admob_adapter/trunk/src as external sources to your Eclipse project that makes use of Admob (for instance aliased as src_adapter)

3. On the AdMob web interface, **create a new network** in the Monetize section. A popup will be presented to you where you can pick a network, instead click on *Custom event* and enter `com.appsfire.mediation.AdmobInterstitialAdapter` in the *Class name* field. In the *Label* field enter something that will allow to easily indentify the Appsfire AdUnit.

Then put `{"sdkKey": "INSERT_YOUR_APPSFIRE_API_KEY", "isDebug": "1"}` in the Parameter section.
To turn off debug mode when you release your app, use "0" instead of "1" for isDebug.

4. You should be good to go after these steps. To make sure, you can **run** the AdMob Mediation Demo project with you AdMob interstitial Unit Id and see a test interstitial.

**Note**: Please make sure to only allocate one interstitial at a time, otherwise you can end up having less events reported to AdMob.

## Release Notes

**1.0**  
Initial release.
