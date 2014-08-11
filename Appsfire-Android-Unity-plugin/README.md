Appsire SDK Unity Plugin for Android
====================================
This document describes the installation and usage of the Unity plugin for the Appsfire Android SDK.

## Introduction
This documentation is a brief introduction to Appsfire Android SDK for Unity. We recommend you to take a look on the general documentation that you can find in your dashboard. Most of methods were implemented in Unity. If you have a problem during the integration, don't hesitate to contact our team at <a href="mailto:support@appsfire.com">support@appsfire.com</a>.

We recommend you to read the integration reference documentation that you find at this url <a href="http://docs.appsfire.com/" target="_blank">http://docs.appsfire.com/</a>.

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
3. Grabbed our latest version of the SDK and Unity plugin from our [Dashboard](http://dashboard.appsfire.com/app/doc)

First you need to add the Appsfire SDK files into your Unity project. Just copy and paste the files from appsfire_unity_plugin/Assets/Plugins to your Unity project's Assets/Plugins folder.

![appsfire sdk unity import step 1](./images/unity-plugin-import-manually.png)

## Implementation
There are 4 important files that you need to be aware of:

* **AppsfireSDKPrefab.prefab** : prefab to include in your project. It'll be used for the communication between our library and Unity.
* **AppsfireSDK.cs** : the base library
* **AppsfireAdSDK.cs** : the "monetization" library (e.g. displaying ads to do money).
* **AppsfireSDKEvents.cs** : subscribing and receiving sdk events.

For the following example, <u>we recommend you to take a look at AppsfireSDKDemo.cs</u> file which is in the demo project included in the zip (with the unitypackage).

## Step 1: Include the prefab
**Make sure that the AppsfireSDKPrefab.prefab is included in your project.** It's required to correctly initialize AppsfireSDK and get events.

## Step 2: Specify the API key
In your **Start()** method, you need to add a required initialization method where you put your **API KEY**. If you don't have it yet, please check the general documentation which explains how to get it thanks to your dashboard.

Once you have it, just add the following line:

	// af sdk - connect with your api key
	AppsfireSDK.ConnectWithAPIKey("demokey");

  // af sdk - enable debug mode
  AppsfireAdSDK.SetDebugModeEnabled(true);

## Step 3: Display Ads
It's easy to display ads during breakout sessions.
First, we recommend you to add this line in the Start() method. It allows our SDK to download ads and any eventual asset and be ready as soon as possible.

	// af ad sdk - prepare here, so ad will be available sooner!
	AppsfireAdSDK.Prepare();

Then, when you are ready to display an ad, you just have to check if any is available, and then request it!

	if (AppsfireAdSDK.IsThereAModalAdAvailable(AFAdSDKModalType.AFAdSDKModalTypeSushi) == AFAdSDKAdAvailability.AFAdSDKAdAvailabilityYes)
		AppsfireAdSDK.RequestModalAd(AFAdSDKModalType.AFAdSDKModalTypeSushi);

You can be alerted that an ad is available thanks to an event. So you can directly display the ad! or wait a breakout session to display it.

	void OnEnable()
	{
		AppsfireSDKEvents.afsdkadModalAdIsReadyForRequest += this.afsdkadModalAdIsReadyForRequest;
	}

	void OnDisable()
	{
		AppsfireSDKEvents.afsdkadModalAdIsReadyForRequest -= this.afsdkadModalAdIsReadyForRequest;
	}

	public void afsdkadModalAdIsReadyForRequest()
	{
		Debug.Log("Appsfire Ad SDK - Modal Ad Is Ready For Request");
		if (AppsfireAdSDK.IsThereAModalAdAvailable(AFAdSDKModalType.AFAdSDKModalTypeUraMaki) == AFAdSDKAdAvailability.AFAdSDKAdAvailabilityYes) {
			AppsfireAdSDK.RequestModalAd(AFAdSDKModalType.AFAdSDKModalTypeSushi);
		}
	}

## Step 5: Register to events
You can easily register and get events from any place of your code.

Here is an example if you want to know when an ad is dismissed.

	void OnEnable()
	{
		AppsfireSDKEvents.afsdkadModalAdDidDisappear += this.afsdkadModalAdDidDisappear;
	}

	void OnDisable()
	{
		AppsfireSDKEvents.afsdkadModalAdDidDisappear -= this.afsdkadModalAdDidDisappear;
	}

	// modal ad did disappear
	public void afsdkadModalAdDidDisappear()
	{
	  Debug.Log("Appsfire Ad SDK - Modal Ad Did Disappear");
	}

If you don't get any event, it's likely that the prefab isn't used in your project.
