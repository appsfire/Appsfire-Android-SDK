package com.appsfire.cordova;

import java.util.Arrays;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.appsfire.adUnitJAR.sdk.AFAdSDK;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKModalType;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFSDKFeature;
import com.appsfire.adUnitJAR.sdk.AFAdSDKEventsDelegate;
import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;
import com.appsfire.adUnitJAR.exceptions.AFAdAlreadyDisplayedException;

// Cordova plugin class

public class CDVAppsfireSDK extends CordovaPlugin implements AFAdSDKEventsDelegate  {
   // Actions
   public static final String ACTION_GET_AFSDK_VERSION_INFO = "sdk_getAFSDKVersionInfo";
   public static final String ACTION_CONNECT_WITH_API_KEY = "sdk_connectWithApiKey";
   public static final String ACTION_SDK_INITIALIZED = "sdk_isInitialized";
   public static final String ACTION_ADSDK_INITIALIZED = "adsdk_isInitialized";
   public static final String ACTION_ARE_ADS_LOADED = "adsdk_areAdsLoaded";
   public static final String ACTION_SET_DEBUG_MODE_ENABLED = "adsdk_setDebugModeEnabled";
   public static final String ACTION_PREPARE = "adsdk_prepare";
   public static final String ACTION_IS_THERE_A_MODAL_AD_AVAILABLE = "adsdk_isThereAModalAdAvailable";
   public static final String ACTION_REQUEST_MODAL_AD = "adsdk_requestModalAd";
   public static final String ACTION_CANCEL_PENDING_AD_MODAL_REQUEST = "adsdk_cancelPendingAdModalRequest";
   public static final String ACTION_IS_MODAL_AD_DISPLAYED = "adsdk_isModalAdDisplayed";
   
	// Tag for logging
	public static final String CLASS_TAG = "CDVAppsfireSDK";
   
	// Appsfire SDK instance
	private AFAdSDK adSdk;
   
   // Initialize
   @Override
   public void initialize (CordovaInterface cordova, CordovaWebView webView) {
      super.initialize (cordova, webView);
      Log.d (CLASS_TAG, "initialize");
      
      if (adSdk == null) {
         adSdk = AFSDKFactory.getAFAdSDK();
         adSdk.setFeatures(Arrays.asList(AFSDKFeature.AFSDKFeatureMonetization));
         adSdk.setEventsDelegate(this);
      }
   }
   
   // Execute action requested by Javascript
   @Override
   public boolean execute (String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
      Log.d (CLASS_TAG, "action \"" + action + "\" with args: " + args);
      
      try {
         if (action.equals (ACTION_GET_AFSDK_VERSION_INFO)) {
            String version = AFSDKFactory.getAFAdSDK().getSDKVersion ();
            
            // Return SDK version
            callbackContext.success (version);
            return true;
         }
         
         if (action.equals (ACTION_CONNECT_WITH_API_KEY)) {
            String apiKey = args.getString (0);
            
            // Log in with API key
            
            Log.d (CLASS_TAG, "set API key");
            if (adSdk != null) {
               adSdk.setAPIKey (apiKey);
            }
            
            callbackContext.success();
            return true;
         }
         
         if (action.equals (ACTION_SDK_INITIALIZED)) {
            Log.d (CLASS_TAG, "check if SDK is initialized");
            
            callbackContext.success((adSdk != null && adSdk.isInitialized()) ? "true" : "false");
            return true;
         }
         
         if (action.equals (ACTION_ADSDK_INITIALIZED)) {
            Log.d (CLASS_TAG, "check if ad SDK is initialized");
            
            callbackContext.success((adSdk != null && adSdk.isInitialized()) ? "true" : "false");
            return true;
         }
         
         if (action.equals (ACTION_ARE_ADS_LOADED)) {
            Log.d (CLASS_TAG, "check if ads are loaded");
            
            callbackContext.success((adSdk != null && adSdk.areAdsLoaded()) ? "true" : "false");
            return true;
         }
         
         if (action.equals (ACTION_SET_DEBUG_MODE_ENABLED)) {
            boolean debugModeEnabled = args.getBoolean (0);
            
            Log.d (CLASS_TAG, "set release/debug mode");
            if (adSdk != null) {
               adSdk.setDebugModeEnabled (debugModeEnabled);
            }
            
            callbackContext.success();
            return true;
         }
         
         if (action.equals (ACTION_PREPARE)) {
            // Predownload
            Log.d (CLASS_TAG, "prepare");
            try {
               adSdk.prepare (this.cordova.getActivity());
            } catch (Exception e) {
               Log.d (CLASS_TAG, "prepare: exception: " + e.toString());
            }
            Log.d (CLASS_TAG, "prepared");
            callbackContext.success();
            return true;
         }
         
         if (action.equals (ACTION_IS_THERE_A_MODAL_AD_AVAILABLE)) {
            String adType = args.getString (0);
            
            Log.d (CLASS_TAG, "check modal ad availability");
            if (adType.equalsIgnoreCase ("sushi")) {
               boolean bAvailable = adSdk.isAModalAdOfTypeAvailable(AFAdSDKModalType.AFAdSDKModalTypeSushi);
               
               // Report sushi availability
               callbackContext.success(bAvailable ? "true" : "false");
               return true;
            }
         }
         
         if (action.equals (ACTION_REQUEST_MODAL_AD)) {
            String adType = args.getString (0);
            
            Log.d (CLASS_TAG, "request modal ad");
            if (adType.equalsIgnoreCase ("sushi")) {
               // Display sushi
               if (adSdk.isAModalAdOfTypeAvailable(AFAdSDKModalType.AFAdSDKModalTypeSushi)) {
                  try {
                     adSdk.requestModalAd(AFAdSDKModalType.AFAdSDKModalTypeSushi, this.cordova.getActivity());
                     callbackContext.success();
                     return true;
                  } catch (AFAdAlreadyDisplayedException e) {
                  }
               }
            }
         }
         
         if (action.equals (ACTION_CANCEL_PENDING_AD_MODAL_REQUEST)) {
            callbackContext.success();
            return true;
         }
         
         if (action.equals (ACTION_IS_MODAL_AD_DISPLAYED)) {
            Log.d (CLASS_TAG, "check if a modal ad is displayed");
            
            callbackContext.success((adSdk != null && adSdk.isModalAdDisplayingNow()) ? "true" : "false");
            return true;
         }
      } catch (Exception e) {
         Log.d (CLASS_TAG, "exception in execute(): " + e.toString());
      }
      
      return false;
   }
   
   /*
    * AFAdSDKEventsDelegate implementation
    */
   
	@Override
	public void onEngageSDKInitialized() {
		// SDK initialized
		Log.i (CLASS_TAG, "onEngageSDKInitialized");
	}
   
	@Override
	public void onAdUnitInitialized() {
		// Ad unit initialized
		Log.i (CLASS_TAG, "onAdUnitInitialized");
	}
   
	
	@Override
	public void onAdsLoaded() {
		// Ads metadata downloaded
		Log.i (CLASS_TAG, "onAdsLoaded");
	}
   
	@Override
	public void onModalAdAvailable() {
		// A modal ad (sushi interstitial) is available
		Log.i (CLASS_TAG, "onModalAdAvailable");
	}
	
	@Override
	public void onInStreamAdAvailable() {
		// One or more in-stream (sashimi) ads are available
		Log.i (CLASS_TAG, "onInStreamAdAvailable");
	}
   
	@Override
	public void onModalAdPreDisplay() {
		// A modal ad is about to display
		Log.i (CLASS_TAG, "onModalAdPreDisplay");
	}
   
	@Override
	public void onModalAdDisplayed() {
		// A modal ad is displayed
		Log.i (CLASS_TAG, "onModalAdDisplayed");
	}
   
	@Override
	public void onModalAdFailedToDisplay(AFAdSDKError errCode) {
		// A modal ad failed to display
		Log.i (CLASS_TAG, "onModalAdFailedToDisplay");
	}
   
	@Override
	public void onModalAdPreDismiss() {
		// A modal ad is about to close
		Log.i (CLASS_TAG, "onModalAdPreDismiss");
	}
   
	@Override
	public void onModalAdDismissed() {
		// A modal ad has closed
		Log.i (CLASS_TAG, "onModalAdDismissed");
	}
}
