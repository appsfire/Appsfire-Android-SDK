package com.appsfire.afantexampleapp;

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
   
	// Put your app API key here !
	private static final String YOUR_API_KEY = "YOUR_API_KEY_HERE";
	
	// Create instance of the Appsfire Ad SDK
	private static AFAdSDK m_adSdk = AFSDKFactory.getAFAdSDK().
                                       setFeatures(Arrays.asList(AFSDKFeature.AFSDKFeatureMonetization)).
                                       setAPIKey(YOUR_API_KEY).
                                       setDebugModeEnabled(IS_AFADSDK_DEBUG);
   
   // Tag for logging
   private static String CLASS_TAG = "afantexampleapp";
   
	// Create activity
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set main layout as content
		setContentView(R.layout.activity_main);
      
		// Initialize SDK
		m_adSdk.setEventsDelegate(new AdUnitSampleAppEventsDelegate());
		m_adSdk.prepare(this);
      
		// Add click handler to "show sushi interstitial ad" button
		Button actionButton = (Button) findViewById(R.id.show_sushi_button);
		final Activity activity = this;
		actionButton.setOnClickListener (new Button.OnClickListener () {
         public void onClick(View v) {
            // Show sushi ad interstitial; see showSushiAd() below
            showSushiAd ();
         }
		});
	}

	// Start activity

	@Override
	protected void onStart() {
		super.onStart();
		m_adSdk.onStart (this);
	}

	// Stop activity
	
	@Override
	protected void onStop() {
		m_adSdk.onStop ();
		super.onStop();
	}
   
   // Request and show sushi ad interstitial
   
	void showSushiAd() {
		// Check if a modal ad of type Sushi is available
		if (m_adSdk.isAModalAdOfTypeAvailable(AFAdSDKModalType.AFAdSDKModalTypeSushi)) {
			try {
				// Request modal ad
            m_adSdk.requestModalAd(AFAdSDKModalType.AFAdSDKModalTypeSushi, this);
			} catch (AFAdAlreadyDisplayedException e) {
			}
		}
		else {
			// SDK still initializing or downloading ad units, or no connectivity available
         Toast toastInstance = Toast.makeText (this, "No modal ads available yet", Toast.LENGTH_LONG);
         toastInstance.show ();
		}
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
	}
}
