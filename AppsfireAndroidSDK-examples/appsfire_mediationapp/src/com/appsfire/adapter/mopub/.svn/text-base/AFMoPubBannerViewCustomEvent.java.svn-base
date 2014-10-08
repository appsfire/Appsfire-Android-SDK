package com.appsfire.adapter.mopub;

import java.util.Map;

import android.app.Activity;
import android.util.Log;

import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.BannerAdListener;

/**
 * MoPub banner view implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the MoPub SDK 2.3 for Android
 */

public class AFMoPubBannerViewCustomEvent implements AFMedBannerViewCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFMoPubBannerViewCustomEvent";

	// Adapter events listener
	private AFMedBannerViewCustomEventListener m_listener;

	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// Current banner ad view
	private MoPubView m_adView;
	
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
			final AFMoPubBannerViewCustomEvent adapter = this;
			
			m_adView = new MoPubView (activity);
			m_adView.setAdUnitId(adUnitId);
			m_adView.setBannerAdListener(new BannerAdListener () {
				@Override
				public void onBannerLoaded(MoPubView banner) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter, m_adView);
					}					
				}

				@Override
				public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
					}					
				}

				@Override
				public void onBannerClicked(MoPubView banner) {
				}

				@Override
				public void onBannerExpanded(MoPubView banner) {
					if (m_listener != null) {
						m_listener.bannerViewCustomEventBeginOverlayPresentation(adapter);
					}
				}

				@Override
				public void onBannerCollapsed(MoPubView banner) {
					m_listener.bannerViewCustomEventEndOverlayPresentation(adapter);
				}				
			});
		    
		    // Initiate a generic request to load it with an ad
		    m_adView.loadAd();
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
}
