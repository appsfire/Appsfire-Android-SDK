package com.appsfire.adapter.facebook;

import java.util.Map;

import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.facebook.ads.*;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Facebook interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the Facebook SDK 3.17.1 for Android 
 */

public class AFFacebookInterstitialCustomEvent implements AFMedInterstitialCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFFacebookInterstitialCustomEvent";

	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;

	// Current interstitial
	private InterstitialAd m_interstitial;
	
	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	@Override
	public void destroy() {
		Log.d (CLASS_TAG, "destroy");
		
		if (m_interstitial != null) {
			m_interstitial.destroy();
			m_interstitial = null;
		}
	}

	@Override
	public void pause() {
		Log.d (CLASS_TAG, "pause");
	}

	@Override
	public void resume() {
		Log.d (CLASS_TAG, "resume");
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
		String placementId = null;
		
		// Reset
		if (m_interstitial != null) {
			m_interstitial.destroy();
			m_interstitial = null;
		}
		m_listenerAlreadyNotified = false;
		
		// Get placementId ID from payload
		if (parameters != null)
			placementId = parameters.get("placementId");
		
		if (placementId != null) {
			final AFFacebookInterstitialCustomEvent adapter = this;
			
			m_interstitial = new InterstitialAd(activity, placementId);
			m_interstitial.setAdListener(new InterstitialAdListener () {
				@Override
				public void onAdLoaded(Ad ad) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter);
					}					
				}

				@Override
				public void onError(Ad ad, AdError error) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
					}					
				}

				@Override
				public void onInterstitialDisplayed(Ad ad) {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillAppear(adapter);
						m_listener.interstitialCustomEventDidAppear(adapter);
					}					
				}

				@Override
				public void onInterstitialDismissed(Ad ad) {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillDisappear(adapter);
						m_listener.interstitialCustomEventDidDisappear(adapter);
					}					
				}			
				
				@Override
				public void onAdClicked(Ad ad) {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillLeaveApplication(adapter);
					}					
				}
			});
			
		    // Begin loading interstitial
			m_interstitial.loadAd();		    
		}
		else {
			// Ad unit ID missing
			Log.d (CLASS_TAG, "missing placementId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}
		}
	}

	@Override
	public void presentInterstitial() {
		Log.d (CLASS_TAG, "presentInterstitial");
		
		if (m_interstitial != null) {
			m_interstitial.show();
		}
	}
};
