package com.appsfire.adapter.doubleclick;

import java.util.Map;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEventListener;
import com.appsfire.adUnitJAR.mediation.AFMedInfo;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.doubleclick.*;

/**
 * DoubleClick banner view implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 */

public class AFDFPBannerViewCustomEvent implements AFMedBannerViewCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFDFPInterstitialCustomEvent";

	// Adapter events listener
	private AFMedBannerViewCustomEventListener m_listener;

	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// Current banner ad view
	private PublisherAdView m_adView;
	
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
	public void setListener (AFMedBannerViewCustomEventListener listener) {
		m_listener = listener;
	}
	
	@Override
	public void requestBannerViewWithSize (Activity activity, Map<String,String> parameters) {
		af_Log.d (CLASS_TAG, "requestInterstitialWithCustomParameters: " + parameters);
		
		String adUnitId = null;
		
		// Reset
		if (m_adView != null)
			m_adView = null;
		m_listenerAlreadyNotified = false;
		
		// Get ad unit ID from payload
		if (parameters != null) {
			adUnitId = parameters.get("adUnitId");
			if (adUnitId == null)
				adUnitId = parameters.get("adunitId");
		}
		
		if (adUnitId != null) {
			final AFDFPBannerViewCustomEvent adapter = this;
			
			m_adView = new PublisherAdView(activity);
			m_adView.setAdUnitId(adUnitId);
			m_adView.setAdSizes(AdSize.BANNER);
			m_adView.setAdListener(new AdListener () {
				@Override
				public void onAdLoaded() {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter, m_adView);
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
						m_listener.bannerViewCustomEventBeginOverlayPresentation(adapter);
					}					
				}
				
				@Override
				public void onAdClosed() {
					if (m_listener != null) {
						m_listener.bannerViewCustomEventEndOverlayPresentation(adapter);
					}					
				}
				
				@Override
				public void onAdLeftApplication() {
					if (m_listener != null) {
						m_listener.bannerViewCustomEventWillLeaveApplication(adapter);
					}					
				}				
			});

		    // Initiate a generic request to load it with an ad
			
			AFMedInfo information = null;
			PublisherAdRequest adRequest;
		    
			if (m_listener != null)
				information = m_listener.bannerViewCustomEventInformation (this);

			if (information != null) {
				adRequest = new PublisherAdRequest.Builder().setBirthday(information.getBirthDate())
												   			.setGender(adNetworkGender (information))
												   			.setLocation(adNetworkLocation (information))
												   			.build();
			}
			else {
				adRequest = new PublisherAdRequest.Builder().build();
			}
			
			m_adView.loadAd(adRequest);
		}
		else {
			/* Invalid parameters */
			Log.d (CLASS_TAG, "missing adUnitId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}			
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
