package com.appsfire.adapter.smartadserver;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEventListener;
import com.appsfire.adUnitJAR.mediation.AFMedInfo;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.smartadserver.android.library.SASBannerView;
import com.smartadserver.android.library.model.SASAdElement;
import com.smartadserver.android.library.ui.SASAdView;

/**
 * SmartAdServer banner view implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the Smart AdServer SDK 4.2.2 for Android
 */

public class AFSASBannerViewCustomEvent implements AFMedBannerViewCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFSmartAdServerBannerViewCustomEvent";

	// Adapter events listener
	private AFMedBannerViewCustomEventListener m_listener;

	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// Activity that requested the banner ad
	private Activity m_activity;
	
	// Current banner ad view
	private AFSASBannerView m_adView;
	
	// SmartAdServer site ID;
	private static String m_siteId;
	
	static public void configure (String siteId) {
		m_siteId = siteId;
	}
	
	@Override
	public void destroy() {
		af_Log.d (CLASS_TAG, "destroy");
		if (m_adView != null) {
			m_adView.onDestroy ();
			m_adView = null;
		}
		
		m_activity = null;
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
		String formatId = null, pageId = null;
		
		af_Log.d (CLASS_TAG, "requestInterstitialWithCustomParameters: " + parameters);
				
		// Reset
		if (m_adView != null)
			m_adView = null;
		m_listenerAlreadyNotified = false;
		
		// Get ad unit IDs from payload
		if (parameters != null) {
			pageId = parameters.get("pageId");
			formatId = parameters.get("formatId");
		}
		
		if (m_siteId != null && formatId != null && pageId != null) {
			try {
				final AFSASBannerViewCustomEvent adapter = this;
				
				m_activity = activity;
				m_adView = new AFSASBannerView(activity);
				int dpHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, activity.getResources().getDisplayMetrics());
				m_adView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpHeight));
				
				AFMedInfo information = null;		    
				if (m_listener != null)
					information = m_listener.bannerViewCustomEventInformation (this);

				if (information != null) {
					m_adView.setLocation(adNetworkLocation (information));
				}
				
				m_adView.loadAd(Integer.parseInt(m_siteId), pageId, Integer.parseInt(formatId), true, "", new SASAdView.AdResponseHandler() {
					@Override
		            public void adLoadingCompleted(SASAdElement sasAdElement) {
						if (m_listener != null && !m_listenerAlreadyNotified) {
							m_listenerAlreadyNotified = true;
							m_listener.didLoadAd (adapter, m_adView);
						}					
		            }
	
					@Override
		            public void adLoadingFailed(Exception e) {
						if (m_listener != null && !m_listenerAlreadyNotified) {
							m_listenerAlreadyNotified = true;
							m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
						}					
		            }
		        });			
			} catch (Exception e) {
				/* Exception creating ad banner */
				if (m_listener != null && !m_listenerAlreadyNotified) {
					m_listenerAlreadyNotified = true;
					m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
				}				
			}
		}
		else {
			/* Invalid parameters */
			Log.d (CLASS_TAG, "missing siteId, pageId or formatId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}			
		}
	}

	// Derive SmartAdServer banner view class to give it a maximum height
	
	public class AFSASBannerView extends SASBannerView {
		public AFSASBannerView(Context context) {
			super(context);
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			if (m_activity != null) {
				int dpHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, m_activity.getResources().getDisplayMetrics());
			    heightMeasureSpec = MeasureSpec.makeMeasureSpec(dpHeight, MeasureSpec.AT_MOST);
			}
		    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	public Location adNetworkLocation (AFMedInfo information) {
		Location location = new Location ("appsfire");
		
		location.setLongitude(information.getLocation().getLongitude());
		location.setLatitude(information.getLocation().getLatitude());
		
		return location;
	}
}

