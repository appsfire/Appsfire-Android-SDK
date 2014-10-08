package com.appsfire.adapter.inmobi;

import java.util.Map;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;

/**
 * InMobi interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the InMobi SDK 4.4.3 for Android
 */

public class AFInMobiInterstitialCustomEvent implements AFMedInterstitialCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFInMobiInterstitialCustomEvent";

	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;
	
	// Current InMobi interstitial
	private IMInterstitial m_inMobiInterstitial;
	
	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// true if ad has appeared yet
	private boolean m_adAppeared = false;
	
	// true if ad has disappeared yet
	private boolean m_adDisappeared = false;
	
	// true if SDK has been initialized yet
	static private boolean m_sdkInitialized;
	
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
		String appId = null;
		
		// Reset
		if (m_inMobiInterstitial != null)
			m_inMobiInterstitial = null;
		m_listenerAlreadyNotified = false;
		m_adAppeared = false;
		m_adDisappeared = false;
		
		// Get appId from payload
		if (parameters != null)
			appId = parameters.get ("appId");
		
		if (appId != null) {
			final AFInMobiInterstitialCustomEvent adapter = this;
			final Activity finalActivity = activity;
			final String finalAppId = appId;
			
			activity.runOnUiThread (new Runnable () {
				@Override
				public void run() {
					if (!m_sdkInitialized) {
						m_sdkInitialized = true;
						af_Log.d (CLASS_TAG, "initialize InMobi");
						InMobi.initialize(finalActivity, finalAppId);
					}
					
					af_Log.d (CLASS_TAG, "request InMobi interstitial");
					m_inMobiInterstitial = new IMInterstitial(finalActivity, finalAppId);
					m_inMobiInterstitial.setIMInterstitialListener(new IMInterstitialListener() {
						@Override
						public void onInterstitialLoaded(IMInterstitial interstitial) {
							af_Log.d (CLASS_TAG, "onInterstitialLoaded");
							if (m_listener != null && !m_listenerAlreadyNotified) {
								m_listenerAlreadyNotified = true;
								m_listener.didLoadAd (adapter);
							}					
						}

						@Override
						public void onInterstitialFailed(IMInterstitial interstitial, IMErrorCode errorCode) {
							af_Log.d (CLASS_TAG, "onInterstitialFailed with error: " + errorCode);
							if (m_listener != null && !m_listenerAlreadyNotified) {
								m_listenerAlreadyNotified = true;
								
								switch (errorCode) {
								case NO_FILL:
									m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
									break;
									
								default:
									m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeMediationRequestFailed);
									break;
								}
							}					
						}

						@Override
						public void onShowInterstitialScreen(IMInterstitial interstitial) {
							af_Log.d (CLASS_TAG, "onShowInterstitialScreen");
							if (m_listener != null && !m_adAppeared) {
								m_adAppeared = true;
								m_listener.interstitialCustomEventWillAppear(adapter);
								m_listener.interstitialCustomEventDidAppear(adapter);
							}					
						}
						
						@Override
						public void onDismissInterstitialScreen(IMInterstitial interstitial) {
							af_Log.d (CLASS_TAG, "onDismissInterstitialScreen");
							if (m_listener != null && !m_adDisappeared) {
								m_adDisappeared = true;
								m_listener.interstitialCustomEventWillDisappear(adapter);
								m_listener.interstitialCustomEventDidDisappear(adapter);
							}					
						}

						@Override
						public void onInterstitialInteraction(IMInterstitial interstitial, Map<String, String> args) {
							af_Log.d (CLASS_TAG, "onInterstitialInteraction");
						}

						@Override
						public void onLeaveApplication(IMInterstitial interstitial) {
							af_Log.d (CLASS_TAG, "onLeaveApplication");
							if (m_listener != null) {
								m_listener.interstitialCustomEventWillLeaveApplication(adapter);
							}
						}
				    }); 
					m_inMobiInterstitial.loadInterstitial();
				}				
			});			
		}
		else {
			// App ID (property) missing
			Log.d (CLASS_TAG, "missing appId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}
		}
	}

	@Override
	public void presentInterstitial() {
		af_Log.d (CLASS_TAG, "presentInterstitial");
		if (m_inMobiInterstitial != null && m_inMobiInterstitial.getState() == IMInterstitial.State.READY)
			m_inMobiInterstitial.show();
	}
}
