package com.appsfire.adapter.doubleclick;

import java.util.Map;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.KeyEvent;

import com.appsfire.adUnitJAR.mediation.AFMedInfo;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.*;

/**
 * DoubleClick interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 */

public class AFDFPInterstitialCustomEvent implements AFMedInterstitialCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFDFPInterstitialCustomEvent";

	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;

	// Current interstitial
	private PublisherInterstitialAd  m_dfpInterstitial;
	
	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	@Override
	public void destroy() {
		af_Log.d (CLASS_TAG, "destroy");
	}

	@Override
	public void pause() {
		af_Log.d (CLASS_TAG, "pause");
	}

	@Override
	public void resume() {
		af_Log.d (CLASS_TAG, "resume");
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
		af_Log.d (CLASS_TAG, "requestInterstitialWithCustomParameters: " + parameters);		
		String adUnitId = null;
		
		// Reset
		if (m_dfpInterstitial != null)
			m_dfpInterstitial = null;
		m_listenerAlreadyNotified = false;
		
		// Get ad unit ID from payload
		if (parameters != null) {
			adUnitId = parameters.get("adUnitId");
			if (adUnitId == null)
				adUnitId = parameters.get("adunitId");
		}
		
		if (adUnitId != null) {
			final AFDFPInterstitialCustomEvent adapter = this;
			
			m_dfpInterstitial = new PublisherInterstitialAd(activity);
			m_dfpInterstitial.setAdUnitId(adUnitId);
			m_dfpInterstitial.setAdListener(new AdListener () {
				@Override
				public void onAdLoaded() {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter);
					}					
				}
				
				@Override
				public void onAdFailedToLoad(int errorCode) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
					}					
				}
				
				@Override
				public void onAdOpened() {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillAppear(adapter);
						m_listener.interstitialCustomEventDidAppear(adapter);
					}					
				}
				
				@Override
				public void onAdClosed() {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillDisappear(adapter);
						m_listener.interstitialCustomEventDidDisappear(adapter);
					}					
				}
				
				@Override
				public void onAdLeftApplication() {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillLeaveApplication(adapter);
					}					
				}
			});

		    // Create ad request.
			AFMedInfo information = null;
			PublisherAdRequest adRequest;
		    
			if (m_listener != null)
				information = m_listener.interstitialCustomEventInformation (this);

			if (information != null) {
				adRequest = new PublisherAdRequest.Builder().setBirthday(information.getBirthDate())
												   			.setGender(adNetworkGender (information))
												   			.setLocation(adNetworkLocation (information))
												   			.build();
			}
			else {
				adRequest = new PublisherAdRequest.Builder().build();
			}

		    // Begin loading the interstitial
		    m_dfpInterstitial.loadAd(adRequest);
		}
		else {
			// Ad unit ID missing
			Log.d (CLASS_TAG, "missing adUnitId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}
		}
	}

	@Override
	public void presentInterstitial() {
		af_Log.d (CLASS_TAG, "presentInterstitial");
		
	    if (m_dfpInterstitial != null && m_dfpInterstitial.isLoaded()) {
	    	m_dfpInterstitial.show();
	    }
	}
	
	public int adNetworkGender (AFMedInfo information) {
		switch (information.getGender()) {
		case AFMedInfoGenderMale:
			return PublisherAdRequest.GENDER_MALE;
		
		case AFMedInfoGenderFemale:
			return PublisherAdRequest.GENDER_FEMALE;
		
		default:
			return PublisherAdRequest.GENDER_UNKNOWN;
		}
	}
	
	public Location adNetworkLocation (AFMedInfo information) {
		Location location = new Location ("appsfire");
		
		location.setLongitude(information.getLocation().getLongitude());
		location.setLatitude(information.getLocation().getLatitude());
		
		return location;
	}
}
