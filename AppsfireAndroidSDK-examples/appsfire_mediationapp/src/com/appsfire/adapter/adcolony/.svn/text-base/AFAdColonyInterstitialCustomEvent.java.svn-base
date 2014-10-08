package com.appsfire.adapter.adcolony;

import java.util.Map;

import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.jirbo.adcolony.*;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.KeyEvent;

/**
 * AdColony interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the AdColony SDK 2.1.1 for Android
 */

public class AFAdColonyInterstitialCustomEvent implements AFMedInterstitialCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFAdColonyInterstitialCustomEvent";

	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;

	// App ID for AdColony
	static private String m_appId;
	
    // Adcolony event listener
	static private AdColonyAdAvailabilityListener m_adAvailabilityListener;
	
	// true if our ad zone is currently available
	static private boolean m_adZoneAvailable;
	
	// Zone ID for the AdColony interstitial
	private String m_zoneId;
		
	// Activity that requested the ad
	private Activity m_activity;
	
	// Video ad
	private AdColonyVideoAd m_interstitial;
		
	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	static public void configure (String appId) {
		m_appId = appId;
	}
	
	@Override
	public void destroy() {
		Log.d (CLASS_TAG, "destroy");
	}

	@Override
	public void pause() {
		Log.d (CLASS_TAG, "pause");
		if (m_activity != null)
			AdColony.pause(); 
	}

	@Override
	public void resume() {
		Log.d (CLASS_TAG, "resume");
		if (m_activity != null)
			AdColony.resume( m_activity ); 
	}
	
	@Override
	public boolean onBackPressed () {
		return false;
	}
	
	@Override
	public void onKeyDown (int keyCode, KeyEvent event) {		
	}
	
	@Override
	public void onKeyUp (int keyCode, KeyEvent event) {		
	}
	
	@Override
	public void setListener(AFMedInterstitialCustomEventListener listener) {
		m_listener = listener;		
	}
	
	@Override
	public void requestInterstitialWithCustomParameters(Activity activity, Map<String, String> parameters) {
		Log.d (CLASS_TAG, "requestInterstitialWithCustomParameters: " + parameters);		
		String adZoneId = null;
		
		// Reset
		m_listenerAlreadyNotified = false;
		
		// Get ad unit ID from payload
		if (parameters != null) {
			adZoneId = parameters.get("adZoneId");
			if (adZoneId == null)
				adZoneId = parameters.get("zoneId");
		}
		
		if (m_appId != null && adZoneId != null) {
			final AFAdColonyInterstitialCustomEvent adapter = this;
			final String[] m_zones = new String[] { adZoneId };
			String versionStr = "";
			
			m_zoneId = adZoneId;
			
			if (m_activity == null || m_activity != activity) {
				m_activity = activity;
				
		        // Get app version
		        try {
		           PackageInfo pkg = activity.getPackageManager().getPackageInfo (activity.getPackageName(), 0);
		           versionStr = pkg.versionName;
		        } catch (Exception e) {
		           Log.v (CLASS_TAG, "exception getting app version: " + e.toString());
		        }
		      
		        // Initialize AdColony
		        Log.v (CLASS_TAG, "initialize AdColony with app version " + versionStr);
			    AdColony.configure (activity, "version:" + versionStr + ",store:google", m_appId, m_zones);
		    
			    // Add zone availability listener
			    m_adAvailabilityListener = new AdColonyAdAvailabilityListener () {
			         @Override
			         public void onAdColonyAdAvailabilityChange (boolean available, String zone_id) {
		            	Log.v (CLASS_TAG, "video ad zone " + zone_id + " available: " + available + ", our zone = " + m_zoneId);		            
			            if (zone_id == m_zoneId) {
			            	m_adZoneAvailable = available;
			            	
			            	if (m_adZoneAvailable) {
								if (m_listener != null && !m_listenerAlreadyNotified) {
									m_listenerAlreadyNotified = true;
									m_listener.didLoadAd (adapter);
								}		            		
			            	}
			            }
			         }
			    };
			    AdColony.addAdAvailabilityListener (m_adAvailabilityListener);
			}
		    
        	m_interstitial = new AdColonyVideoAd(m_zoneId).withListener (new AdColonyAdListener () {
                @Override
                public void onAdColonyAdStarted (AdColonyAd ad) {
                   Log.v (CLASS_TAG, "adcolony video ad started");
					 if (m_listener != null) {
						m_listener.interstitialCustomEventWillAppear(adapter);
						m_listener.interstitialCustomEventDidAppear(adapter);
					 }		
                }
                
                @Override
                public void onAdColonyAdAttemptFinished (AdColonyAd ad) {
                   if (ad.shown()) {
                      Log.v (CLASS_TAG, "adcolony video ad finished (shown)");
						m_listener.interstitialCustomEventWillDisappear(adapter);
						m_listener.interstitialCustomEventDidDisappear(adapter);
                   }
                   else {
                      Log.v (CLASS_TAG, "adcolony video ad finished (not shown)");
      				m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
                   }
                }
            });
        	
        	if (m_adZoneAvailable && m_interstitial.isReady()) {
        		Log.v (CLASS_TAG, "adcolony video ad is immediately ready");
				if (m_listener != null && !m_listenerAlreadyNotified) {
					m_listenerAlreadyNotified = true;
					m_listener.didLoadAd (adapter);
				}        		
        	}
		}
		else {
			// Ad unit IDs missing
			Log.d (CLASS_TAG, "missing appId or adZoneId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}
		}
	}

	@Override
	public void presentInterstitial() {
		Log.d (CLASS_TAG, "presentInterstitial");
		
    	if (m_adZoneAvailable && m_adAvailabilityListener != null && m_activity != null && m_interstitial != null &&
    	    m_interstitial.isReady()) {
    		final AFAdColonyInterstitialCustomEvent adapter = this;
    		
            m_activity.runOnUiThread (new Runnable () {
                public void run() {                   
                   m_interstitial.show();
                }
            });
        }
	}
};
