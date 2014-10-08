package com.appsfire.adapter.facebook;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View.MeasureSpec;

import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.facebook.ads.*;
import com.smartadserver.android.library.SASBannerView;

/**
 * Facebook banner view implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the Facebook SDK 3.17.1 for Android 
 */

public class AFFacebookBannerViewCustomEvent implements AFMedBannerViewCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFFacebookBannerViewCustomEvent";

	// Adapter events listener
	private AFMedBannerViewCustomEventListener m_listener;

	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// Activity that requested the banner
	private Activity m_activity;
	
	// Current banner ad view
	private AFFacebookAdView m_adView;
		
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
		
		String placementId = null;
		
		// Reset
		if (m_adView != null)
			m_adView = null;
		m_listenerAlreadyNotified = false;
		m_activity = activity;
		
		// Get placement ID from payload
		if (parameters != null)
			placementId = parameters.get("placementId");
		
		if (placementId != null) {
			final AFFacebookBannerViewCustomEvent adapter = this;
			
			m_adView = new AFFacebookAdView (activity, placementId, AdSize.BANNER_HEIGHT_90);
			m_adView.setAdListener(new AdListener () {
				@Override
				public void onAdLoaded(Ad ad) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter, m_adView);
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
				public void onAdClicked(Ad ad) {
					if (m_listener != null) {
						m_listener.bannerViewCustomEventWillLeaveApplication(adapter);
					}					
				}				
			});
			
		    // Initiate a generic request to load it with an ad
			m_adView.loadAd();
		}
		else {
			/* Invalid parameters */
			Log.d (CLASS_TAG, "missing placementId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}			
		}
	}
	
	// Derive Facebook banner view class to give it a maximum height
	
	public class AFFacebookAdView extends AdView {
		public AFFacebookAdView(Context context, String placementId, AdSize adSize) {
			super(context, placementId, adSize);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			if (m_activity != null) {
				int dpHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, m_activity.getResources().getDisplayMetrics());
			    heightMeasureSpec = MeasureSpec.makeMeasureSpec(dpHeight, MeasureSpec.AT_MOST);
			}
		    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
