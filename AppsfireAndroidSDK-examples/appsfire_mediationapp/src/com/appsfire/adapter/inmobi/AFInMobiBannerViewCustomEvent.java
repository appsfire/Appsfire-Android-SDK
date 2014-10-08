package com.appsfire.adapter.inmobi;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;

/**
 * InMobi banner view implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the InMobi SDK 4.4.3 for Android
 */

public class AFInMobiBannerViewCustomEvent implements AFMedBannerViewCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFInMobiBannerViewCustomEvent";

	// Adapter events listener
	private AFMedBannerViewCustomEventListener m_listener;

	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// true if SDK has been initialized yet
	static private boolean m_sdkInitialized;
	
	// Current banner ad view
	private IMBanner m_adView;
	
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
		
		String appId = null;
		
		// Reset
		if (m_adView != null)
			m_adView = null;
		m_listenerAlreadyNotified = false;
		
		// Get appId from payload
		if (parameters != null)
			appId = parameters.get ("appId");
		
		if (appId != null) {
			final AFInMobiBannerViewCustomEvent adapter = this;
			
			if (!m_sdkInitialized) {
				m_sdkInitialized = true;
				af_Log.d (CLASS_TAG, "initialize InMobi");
				InMobi.initialize(activity, appId);
			}
			
			m_adView = new IMBanner(activity, appId, getOptimalSlotSize (activity));
			m_adView.setGravity(Gravity.CENTER_HORIZONTAL);
			m_adView.setRefreshInterval(IMBanner.REFRESH_INTERVAL_OFF);
			m_adView.setIMBannerListener(new IMBannerListener () {
				@Override
				public void onBannerRequestSucceeded(IMBanner banner) {
					af_Log.d (CLASS_TAG, "onBannerRequestSucceeded");
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter, m_adView);
					}					
				}

				@Override
				public void onBannerRequestFailed(IMBanner banner, IMErrorCode err) {
					af_Log.d (CLASS_TAG, "onBannerRequestFailed");
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
					}					
				}

				@Override
				public void onBannerInteraction(IMBanner banner, Map<String, String> params) {
					af_Log.d (CLASS_TAG, "onBannerInteraction");
				}

				@Override
				public void onShowBannerScreen(IMBanner banner) {
					af_Log.d (CLASS_TAG, "onShowBannerScreen");
					if (m_listener != null) {
						m_listener.bannerViewCustomEventBeginOverlayPresentation(adapter);
					}					
				}

				@Override
				public void onDismissBannerScreen(IMBanner banner) {
					af_Log.d (CLASS_TAG, "onDismissBannerScreen");
					if (m_listener != null) {
						m_listener.bannerViewCustomEventEndOverlayPresentation(adapter);
					}					
				}

				@Override
				public void onLeaveApplication(IMBanner banner) {
					af_Log.d (CLASS_TAG, "onLeaveApplication");
					if (m_listener != null) {
						m_listener.bannerViewCustomEventWillLeaveApplication(adapter);
					}					
				}				
			});
					
		    // Load ad
			af_Log.d (CLASS_TAG, "load banner");
			m_adView.loadBanner ();
		}
		else {
			/* Invalid parameters */
			Log.d (CLASS_TAG, "missing appId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}			
		}
	}
	
	private static Integer getOptimalSlotSize(Activity ctxt) {
	    Display display = ((WindowManager) ctxt.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        double density = displayMetrics.density;
        double width = displayMetrics.widthPixels;
        double height = displayMetrics.heightPixels;
        
		int[][] maparray = { { IMBanner.INMOBI_AD_UNIT_728X90, 728, 90 }, {
							   IMBanner.INMOBI_AD_UNIT_468X60, 468, 60 }, {
							   IMBanner.INMOBI_AD_UNIT_320X50, 320, 50 } };
		
		for (int i = 0; i < maparray.length; i++) {
			if (maparray[i][1] * density <= width
		        && maparray[i][2] * density <= height) {
		       return maparray[i][0];
		    }
		}
		
		return IMBanner.INMOBI_AD_UNIT_320X50;
    }
}
