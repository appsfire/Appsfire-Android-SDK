package com.appsfire.adapter.mopub;

import java.util.Map;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener;

/**
 * MoPub interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the MoPub SDK 2.3 for Android
 */

public class AFMoPubInterstitialCustomEvent implements AFMedInterstitialCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFMoPubInterstitialCustomEvent";

	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;

	// Current MoPub interstitial
	private MoPubInterstitial m_mopubInterstitial;
	
	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;

	@Override
	public void destroy() {
		Log.d (CLASS_TAG, "destroy");
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
		String adUnitId = null;
		
		// Reset
		if (m_mopubInterstitial != null)
			m_mopubInterstitial = null;
		m_listenerAlreadyNotified = false;
		
		// Get ad unit ID from payload
		if (parameters != null) {
			adUnitId = parameters.get("adUnitId");
			if (adUnitId == null)
				adUnitId = parameters.get("adunitId");
		}
		
		if (adUnitId != null) {
			final AFMoPubInterstitialCustomEvent adapter = this;
			
			m_mopubInterstitial = new MoPubInterstitial(activity, adUnitId);
			m_mopubInterstitial.setInterstitialAdListener(new InterstitialAdListener () {
				@Override
				public void onInterstitialLoaded (MoPubInterstitial interstitial) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter);
					}					
				}

				@Override
				public void onInterstitialFailed (MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						
						switch (errorCode) {
						case NO_FILL:
							m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
							break;
							
						case CANCELLED:
							m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingCanceledByDeveloper);
							break;
							
						default:
							m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeMediationRequestFailed);
							break;
						}
					}
				}

				@Override
				public void onInterstitialShown (MoPubInterstitial interstitial) {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillAppear(adapter);
						m_listener.interstitialCustomEventDidAppear(adapter);
					}					
				}

				@Override
				public void onInterstitialClicked (MoPubInterstitial interstitial) {
				}

				@Override
				public void onInterstitialDismissed (MoPubInterstitial interstitial) {
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillDisappear(adapter);
						m_listener.interstitialCustomEventDidDisappear(adapter);
					}					
				}
			});
			
			// Begin loading interstitial
			m_mopubInterstitial.load();
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
		if (m_mopubInterstitial != null && m_mopubInterstitial.isReady()) {
			m_mopubInterstitial.show();
	    }
	}
}
