package com.appsfire.admobmediationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.*;

/**
 * Sample activity
 */

public class MainActivity extends Activity {
	// Tag for logging messages
	public static final String CLASS_TAG = "AFadmobmediationdemo";
	
	// Admob ad unit ID in demo app
	private static final String ADMOB_ADUNIT_ID = "ca-app-pub-9559125671393639/8568170309";
	
	// Admob interstitial
	private InterstitialAd mInterstitial;
	
	// Create activity
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set main layout as content
		setContentView(R.layout.activity_main);
		
		// Create the interstitial.
		mInterstitial = new InterstitialAd(this);
		mInterstitial.setAdUnitId(ADMOB_ADUNIT_ID);
		mInterstitial.setAdListener(new AdListener () {
			@Override
			public void onAdClosed() {
				AdRequest adRequest = new AdRequest.Builder().build();
				mInterstitial.loadAd(adRequest);
			}
		});

	    // Create ad request.
	    AdRequest adRequest = new AdRequest.Builder().build();
	    
	    // Begin loading interstitial.
	    mInterstitial.loadAd(adRequest);
	    
		// Add click handler to "show Admob Interstitial ad" button
		Button actionButton = (Button) findViewById(R.id.show_admob_button);
		final Activity activity = this;
		Log.d (CLASS_TAG, "actionButton = " + actionButton);
		actionButton.setOnClickListener (new Button.OnClickListener () {
		    public void onClick(View v) {
		    	 if (mInterstitial.isLoaded()) {
		    		 Log.d (CLASS_TAG, "Show interstitial...");
		    		 mInterstitial.show();
		 	      }
				else {
					// Interstitial is still loading
	                Toast toastInstance = Toast.makeText (activity, "The Admob interstitial is still loading...", Toast.LENGTH_LONG);				                
	                toastInstance.show ();																
				}
		    	
		    }
		});			   			
	}
}
