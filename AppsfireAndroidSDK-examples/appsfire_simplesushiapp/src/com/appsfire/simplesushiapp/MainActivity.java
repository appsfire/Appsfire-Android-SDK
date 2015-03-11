package com.appsfire.simplesushiapp;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appsfire.adUnitJAR.sdk.*;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.*;
import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;
import com.appsfire.adUnitJAR.exceptions.*;

/**
 * Sample activity
 */

public class MainActivity extends Activity {
	// true for debug mode, false for production mode (set to false when distributing your app)
	private static final Boolean IS_AFADSDK_DEBUG = true;
		
	// App key
	private static final String YOUR_API_KEY = "YOUR_API_KEY_HERE";
	
	// Create instance of the Appsfire Ad SDK
	private static AFAdSDK adSdk = AFSDKFactory.getAFAdSDK().
								        setFeatures(Arrays.asList(AFSDKFeature.AFSDKFeatureMonetization)).
										setAPIKey(YOUR_API_KEY).
										setEventsDelegate(new AdUnitSampleAppEventsDelegate()).
										setDebugModeEnabled(IS_AFADSDK_DEBUG);
	
	// Tag for logging messages
	public static String CLASS_TAG = "AFSimpleSushiApp";
	
	// Create activity
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set main layout as content
		setContentView(R.layout.activity_main);
		
		// Initialize SDK
		adSdk.prepare(this);
		
		// Add click handler to "show sushi interstitial ad" button
		Button actionButton = (Button) findViewById(R.id.show_sushi_button);
		final Activity activity = this;
		actionButton.setOnClickListener (new Button.OnClickListener () {
		    public void onClick(View v) {
				// Check if a modal ad of type Sushi is available
				if (adSdk.isAModalAdOfTypeAvailable(AFAdSDKModalType.AFAdSDKModalTypeSushi)) {
					try {
						// Request modal ad
		        	    adSdk.requestModalAd(AFAdSDKModalType.AFAdSDKModalTypeSushi, activity);			                        
					} catch (AFAdAlreadyDisplayedException e) {									
					}
				}
				else {
					// SDK still initializing or downloading ad units, or no connectivity available
	                Toast toastInstance = Toast.makeText (activity, "No modal ads available yet", Toast.LENGTH_LONG);				                
	                toastInstance.show ();																
				}
		    	
		    }
		});
	}

	// Start activity

	@Override
	protected void onStart() {
		super.onStart();
		adSdk.onStart (this);
	}

	// Stop activity
	
	@Override
	protected void onStop() {
		adSdk.onStop ();		
		super.onStop();
	}
	
	/**
	 * Handle events dispatched by the ad SDK
	 */

	static class AdUnitSampleAppEventsDelegate implements AFAdSDKEventsDelegate {			
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
			Log.i (CLASS_TAG,"onAdsLoaded");
		}

		@Override
		public void onModalAdAvailable() {
			// A modal ad (sushi interstitial) is available
			Log.i (CLASS_TAG, "onModalAdAvailable");
		}
		
		@Override
		public void onInStreamAdAvailable() {
			// One or more in-stream (sashimi) ads are available
			Log.i (CLASS_TAG,"onInStreamAdAvailable");
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
		}

		@Override
		public void onLeaveApplication() {
			// Leaving application
			Log.i (CLASS_TAG,"onLeaveApplication");
		}
	}
}
