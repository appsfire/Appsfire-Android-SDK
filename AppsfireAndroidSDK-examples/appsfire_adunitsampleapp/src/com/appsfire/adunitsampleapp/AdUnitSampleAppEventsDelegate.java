package com.appsfire.adunitsampleapp;

import android.util.Log;

import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.adUnitJAR.sdk.AFAdSDKEventsDelegate;
import com.appsfire.adunitsampleapp.MainAdUnitActivity.AdSDKStatus;

/**
 * Handle events dispatched by the ad SDK
 */

public class AdUnitSampleAppEventsDelegate implements AFAdSDKEventsDelegate {
	// Tag for logging messages
	public static String CLASS_TAG = "AdUnitSampleAppEventsDelegate";
		
	@Override
	public void onEngageSDKInitialized() {
		// SDK initialized
		Log.i (CLASS_TAG, "onEngageSDKInitialized");
		((MainAdUnitActivity)App.getSDK().getContext("MainAdUnitActivity")).setDisplayedAdSdkStatus(AdSDKStatus.engageSDKInitialized, true);
	}

	@Override
	public void onAdUnitInitialized() {
		// Ad unit initialized
		Log.i (CLASS_TAG, "onAdUnitInitialized");
		((MainAdUnitActivity)App.getSDK().getContext("MainAdUnitActivity")).setDisplayedAdSdkStatus(AdSDKStatus.adSDKInitialized, true);
	}

	
	@Override
	public void onAdsLoaded() {
		// Ads metadata downloaded
		Log.i (CLASS_TAG,"onAdsLoaded");
		((MainAdUnitActivity)App.getSDK().getContext("MainAdUnitActivity")).setDisplayedAdSdkStatus(AdSDKStatus.adsLoaded, true);
	}

	@Override
	public void onModalAdAvailable() {
		// A modal ad (sushi interstitial) is available
		Log.i (CLASS_TAG, "onModalAdAvailable");
		((MainAdUnitActivity)App.getSDK().getContext("MainAdUnitActivity")).setDisplayedAdSdkStatus(AdSDKStatus.modalAdAvailable, true);
	}
	
	@Override
	public void onInStreamAdAvailable() {
		// One or more in-stream (sashimi) ads are available
		Log.i (CLASS_TAG,"onInStreamAdAvailable");
		((MainAdUnitActivity)App.getSDK().getContext("MainAdUnitActivity")).setDisplayedAdSdkStatus(AdSDKStatus.inStreamAdAvailable, true);
	}
		
	@Override
	public void onModalAdPreDisplay() {
		// A modal ad is about to display
		Log.i (CLASS_TAG,"onModalAdPreDisplay");
	}

	@Override
	public void onModalAdDisplayed() {
		// A modal ad is displayed
		Log.i (CLASS_TAG,"onModalAdDisplayed");
	}

	@Override
	public void onModalAdFailedToDisplay(AFAdSDKError errCode) {
		// A modal ad failed to display
		Log.i (CLASS_TAG,"onModalAdFailedToDisplay");
	}

	@Override
	public void onModalAdPreDismiss() {
		// A modal ad is about to close
		Log.i (CLASS_TAG,"onModalAdPreDismiss");
	}

	@Override
	public void onModalAdDismissed() {
		// A modal ad has closed
		Log.i (CLASS_TAG,"onModalAdDismissed");
		((MainAdUnitActivity)App.getSDK().getContext("MainAdUnitActivity")).setDisplayedAdSdkStatus(AdSDKStatus.requestedAnAd, false); // color back to red
	}

	@Override
	public void onLeaveApplication() {
		// Leaving application
		Log.i (CLASS_TAG,"onLeaveApplication");
	}
}
