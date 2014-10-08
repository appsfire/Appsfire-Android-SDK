package com.appsfire.mediationexampleapp;

import java.util.Arrays;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsfire.adUnitJAR.sdk.*;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.*;
import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;
import com.appsfire.adUnitJAR.mediation.AFMedBannerView;
import com.appsfire.adUnitJAR.mediation.AFMedBannerViewListener;
import com.appsfire.adUnitJAR.mediation.AFMedInfo;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitial;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialListener;
import com.appsfire.adapter.adcolony.AFAdColonyInterstitialCustomEvent;
import com.appsfire.adapter.chartboost.AFChartBoostInterstitialCustomEvent;
import com.appsfire.adapter.smartadserver.AFSASBannerViewCustomEvent;
import com.appsfire.adapter.smartadserver.AFSASInterstitialCustomEvent;

/**
 * Sample activity
 */

public class MainActivity extends Activity implements AFMedInterstitialListener, AFMedBannerViewListener {
	// Tag for logging messages
	public static final String CLASS_TAG = "AFMediationExampleApp";
	
	// true for debug mode, false for production mode (set to false when distributing your app)
	private static final Boolean IS_AFADSDK_DEBUG = false;
		
	// App key
	private static final String YOUR_API_KEY = "YOUR_API_KEY_HERE";
	
	// App secret
	private static final String YOUR_API_SECRET = "aeb1128780d3669b545cf7beb3ea0386";
	
	// Create instance of the Appsfire Ad SDK
	private static AFAdSDK adSdk = AFSDKFactory.getAFAdSDK().
								        setFeatures(Arrays.asList(AFSDKFeature.AFSDKFeatureMonetization)).
										setAPIKey(YOUR_API_KEY).
										setAPISecret(YOUR_API_SECRET).
										setEventsDelegate(new AdUnitSampleAppEventsDelegate()).
										setDebugModeEnabled(IS_AFADSDK_DEBUG).
										setVerboseLoggingEnabled(true);
		
	// Main activity
	private static Activity m_activity = null;
	
	// Status indicator
	private static TextView m_status = null;
	
	// Last set status text
	private static String m_lastStatusText = null;
	
	// Name of network currently serving ad
	private static String m_currentNetworkName = null;
	
	// Interstitial mediator
	private AFMedInterstitial m_interstitial = null;
	
	// True if currently loading or presenting an ad
	private boolean m_busy = false;
	
	// Banner view mediator
	private AFMedBannerView m_bannerView = null;
	
	// Create activity
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final String interstitialsJson = "[ { id: 5, name: \"Appsfire\", class: \"com.appsfire.adUnitJAR.mediation.AFAppsfireInterstitialCustomEvent\", parameters: { type: \"sushi\" } }," +
							             "  { id: 3, name: \"AdMob\", class: \"com.appsfire.adapter.admob.AFAdMobInterstitialCustomEvent\", parameters: { adUnitId: \"ca-app-pub-9559125671393639/2392684707\" } }," +
							             "  { id: 2, name: \"MoPub\", class: \"com.appsfire.adapter.mopub.AFMoPubInterstitialCustomEvent\", parameters: { adUnitId: \"d69e68b5ddfe49fc983a047906ee0de5\" } }," +
							             "  { id: 4, name: \"Double Click\", class: \"com.appsfire.adapter.doubleclick.AFDFPInterstitialCustomEvent\", parameters: { adUnitId: \"/6253334/dfp_example_ad/interstitial\" } }," +
							             "  { id: 0, name: \"Smart Ad Server\", class: \"com.appsfire.adapter.smartadserver.AFSASInterstitialCustomEvent\", parameters: { siteId: \"35176\", pageId: \"(news_activity)\", formatId: \"12167\" } }," +
							             "  { id: 1, name: \"Facebook\", class: \"com.appsfire.adapter.facebook.AFFacebookInterstitialCustomEvent\", parameters: { placementId: \"219190781458893_817994434911855\" } }," +
		 								 "  { id: 30, name: \"AdColony\", class: \"com.appsfire.adapter.adcolony.AFAdColonyInterstitialCustomEvent\", parameters: { appId: \"app67af8dbe452747228e\", adZoneId: \"vzfbe1c2864c344d02b8\" } }," +
	             						 "  { id: 31, name: \"ChartBoost\", class: \"com.appsfire.adapter.chartboost.AFChartBoostInterstitialCustomEvent\", parameters: { appId: \"53fc67991873da0660043e74\", appSignature: \"a5427009f228f8cef0eb7113ae6ec5519f3d9a0e\" } }," +
	             						 "  { id: 450, name: \"InMobi\", class: \"com.appsfire.adapter.inmobi.AFInMobiInterstitialCustomEvent\", parameters: { appId: \"dfae8ce7677d4b8b9da830c0fd361593\" } }," +
	             						 "  { id: 45, name: \"Xaxis\", class: \"com.appsfire.adapter.xaxis.AFXaxisInterstitialCustomEvent\", parameters: { domainName: \"delivery.uat.247realmedia.com\", pageName: \"MSDK-internal-2\", adPosition: \"x25\" } } ]";
		final String bannerViewsJson = "[ { id: 0, name: \"Smart Ad Server\", class: \"com.appsfire.adapter.smartadserver.AFSASBannerViewCustomEvent\", parameters: { siteId: \"35176\", pageId: \"(news_activity)\", formatId: \"15140\" } }," + 
							            " { id: 3, name: \"AdMob\", class: \"com.appsfire.adapter.admob.AFAdMobBannerViewCustomEvent\", parameters: { adUnitId: \"ca-app-pub-9559125671393639/4459357107\" } }," +				
									    " { id: 45, name: \"Xaxis\", class: \"com.appsfire.adapter.xaxis.AFXaxisBannerViewCustomEvent\", parameters: { domainName: \"delivery.uat.247realmedia.com\", pageName: \"MSDK-Opera-MRAID2-Expand-StayCentered\", adPosition: \"x25\" } }," +
	             						" { id: 2, name: \"MoPub\", class: \"com.appsfire.adapter.mopub.AFMoPubBannerViewCustomEvent\", parameters: { adUnitId: \"693d0d19589f4ff48a82cd485c6043e2\" } }," +
	             						" { id: 450, name: \"InMobi\", class: \"com.appsfire.adapter.inmobi.AFInMobiBannerViewCustomEvent\", parameters: { appId: \"dfae8ce7677d4b8b9da830c0fd361593\" } }," +
										" { id: 4, name: \"Double Click\", class: \"com.appsfire.adapter.doubleclick.AFDFPBannerViewCustomEvent\", parameters: { adUnitId: \"/6253334/dfp_example_ad\" } }," +
										" { id: 1, name: \"Facebook\", class: \"com.appsfire.adapter.facebook.AFFacebookBannerViewCustomEvent\", parameters: { placementId: \"219190781458893_817994434911855\" } } ]";
		super.onCreate(savedInstanceState);
		m_activity = this;

		// Set main layout as content
		setContentView(R.layout.activity_main);
		
		// Initialize SDK
		adSdk.prepare(this);

		// Create user information
		AFMedInfo information = new AFMedInfo ()
									.setBirthDate(new Date ())
									.setGender(AFMedInfo.AFMedInfoGender.AFMedInfoGenderMale)
									.setLocation(new AFMedInfo.AFMedInfoLocation().setLatitude(100.0).setLongitude(100.0));
				
		// Configure adapters
		AFAdColonyInterstitialCustomEvent.configure("app67af8dbe452747228e");  // appId
		AFChartBoostInterstitialCustomEvent.configure("53fc67991873da0660043e74", "a5427009f228f8cef0eb7113ae6ec5519f3d9a0e");	// appID, appSignature
		AFSASInterstitialCustomEvent.configure("35176"); // siteId
		AFSASBannerViewCustomEvent.configure("35176"); // siteId
		
		// Create interstitials mediator
		Log.d (CLASS_TAG, "create interstitial");
		m_interstitial = new AFMedInterstitial (this, "a389e3e4-4da6-af86-d181-94b783b06918");
		m_interstitial.setListener(this);
		m_interstitial.setInformation(information);
//		m_interstitial.setCustomNetworksData(interstitialsJson);
		
		// Create banner view mediator
		m_bannerView = new AFMedBannerView (this, "e593f3fd-6fff-d16d-fe1d-2c0140d2132c");
		m_bannerView.setListener(this);
		m_bannerView.setInformation(information);
//		m_bannerView.setCustomNetworksData(bannerViewsJson);
		
		m_status = (TextView) findViewById(R.id.mediation_status);
		
		// Add click handler to "show interstitial ad" button
		Button interstitialActionButton = (Button) findViewById(R.id.show_interstitial_button);
		interstitialActionButton.setOnClickListener (new Button.OnClickListener () {
		    public void onClick(View v) {
				// Load interstitial ad
		    	if (!m_busy) {
		    		m_busy = true;
			    	setStatus("Requesting interstitial...");
			    	m_interstitial.loadAd ();
		    	}
		    }
		});
		
		// Add click handler to "show banner ad" button
		Button bannerActionButton = (Button) findViewById(R.id.show_banner_button);
		bannerActionButton.setOnClickListener (new Button.OnClickListener () {
		    public void onClick(View v) {
				// Load banner ad
		    	if (!m_busy) {
		    		m_busy = true;
			    	setStatus("Requesting banner...");
		    		m_bannerView.loadAd();
		    	}
		    }
		});		
				 
   	    // Add banner ad mediator view to layout
	   	FrameLayout.LayoutParams params;
	   	params = new FrameLayout.LayoutParams (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
		LinearLayout bannerAdLayout = (LinearLayout) findViewById(R.id.banner_ad); 		
		bannerAdLayout.addView(m_bannerView, 0, params);
	}

	// Destroy activity
	
	@Override
	protected void onDestroy() {
		if (m_interstitial != null) {
			m_interstitial.destroy();
			m_interstitial = null;
		}
		
		m_activity = null;
		
		super.onDestroy ();
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
	
    @Override
    public void onBackPressed () {
       if (m_interstitial != null && m_interstitial.onBackPressed ())
          return;
       else
          super.onBackPressed ();
    }
    
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
    	if (m_interstitial != null)
    		m_interstitial.onKeyDown(keyCode, event);
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event) {
    	if (m_interstitial != null)
    		m_interstitial.onKeyUp(keyCode, event);
    	return super.onKeyUp(keyCode, event);
    }
    
	/**
	 * Set status text
	 * 
	 * @param status new status text
	 */
	private static void setStatus (final String status) {
		if (m_activity != null && m_status != null) {
			m_lastStatusText = status;
			m_activity.runOnUiThread(new Runnable () {
				@Override
				public void run() {
					if (m_currentNetworkName != null)
						m_status.setText (status + "\n(" + m_currentNetworkName + ")");
					else
						m_status.setText (status);					
				}
			});
		}
	}

	/**
	 * Handle events dispatched by the interstitial mediator (AFMedInterstitialListener implementation)
	 */
	
	@Override
	public void interstitialDidChangeNetwork (AFMedInterstitial interstitial, String networkName) {	
		m_currentNetworkName = networkName;
		setStatus (m_lastStatusText);
	}
	
	@Override
	public void interstitialDidLoadAd(AFMedInterstitial interstitial) {
		Log.d (CLASS_TAG, "interstitialDidLoadAd");
		
		// Ad loaded
		setStatus("Ad loaded, presenting...");
	}

	@Override
	public void interstitialDidFailToLoadAd(AFMedInterstitial interstitial, AFAdSDKError errCode) {
		Log.d (CLASS_TAG, "interstitialDidFailToLoadAd with error:" + errCode);
		setStatus("Ad failed to load");
		m_busy = false;
	}

	@Override
	public void interstitialWillAppear(AFMedInterstitial interstitial) {
		Log.d (CLASS_TAG, "interstitialWillAppear");
		setStatus("Ad will appear");
	}

	@Override
	public void interstitialDidAppear(AFMedInterstitial interstitial) {
		Log.d (CLASS_TAG, "interstitialDidAppear");
		setStatus("Ad appeared");
	}

	@Override
	public void interstitialWillDisappear(AFMedInterstitial interstitial) {
		Log.d (CLASS_TAG, "interstitialWillDisappear");
		setStatus("Ad will disappear");
	}
	
	@Override
	public void interstitialDidDisappear(AFMedInterstitial interstitial) {
		Log.d (CLASS_TAG, "interstitialDidDisappear");
		setStatus("Ad disappeared");
		m_busy = false;
	}
	
	@Override
	public void interstitialDidExpire(AFMedInterstitial interstitial) {
		Log.d (CLASS_TAG, "interstitialDidExpire");
		setStatus("Ad expired");
		m_busy = false;
	}
	
	/**
	 * Handle events dispatched by the banner views mediator (AFMedBannerViewListener implementation)
	 */
	
	@Override
	public void bannerViewDidChangeNetwork (AFMedBannerView bannerView, String networkName) {
		m_currentNetworkName = networkName;
		setStatus (m_lastStatusText);
	}

	@Override
	public void bannerViewDidLoadAd (AFMedBannerView bannerView) {
		Log.d (CLASS_TAG, "bannerViewDidLoadAd");    	
		setStatus("Banner ad loaded, display");
		m_busy = false;
	}
	
	@Override
	public void bannerViewDidFailToLoadAd (AFMedBannerView bannerView, AFAdSDKError error) {
		Log.d (CLASS_TAG, "bannerViewDidFailToLoadAd");				
		setStatus("Ad failed to load");
		m_busy = false;
	}

	@Override
	public void bannerViewBeginOverlayPresentation (AFMedBannerView bannerView) {
		Log.d (CLASS_TAG, "bannerViewBeginOverlayPresentation");
		setStatus("Begin overlay");
	}

	@Override
	public void bannerViewEndOverlayPresentation (AFMedBannerView bannerView) {
		Log.d (CLASS_TAG, "bannerViewEndOverlayPresentation");				
		setStatus("End overlay");
	}

	/**
	 * Handle events dispatched by the ad SDK
	 */

	static class AdUnitSampleAppEventsDelegate implements AFAdSDKEventsDelegate {			
		@Override
		public void onEngageSDKInitialized() {
		}

		@Override
		public void onAdUnitInitialized() {
			// Ad unit initialized
			setStatus("Initialized, loading ads...");
		}

		
		@Override
		public void onAdsLoaded() {
			// Ads metadata downloaded
			setStatus("Ads loaded");
		}

		@Override
		public void onModalAdAvailable() {
		}
		
		@Override
		public void onInStreamAdAvailable() {
		}
			
		@Override
		public void onModalAdPreDisplay() {
		}

		@Override
		public void onModalAdDisplayed() {
		}

		@Override
		public void onModalAdFailedToDisplay(AFAdSDKError errCode) {
		}

		@Override
		public void onModalAdPreDismiss() {
		}

		@Override
		public void onModalAdDismissed() {
		}
	}
}
