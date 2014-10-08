package com.appsfire.adapter.smartadserver;

import java.util.Map;

import com.appsfire.adUnitJAR.mediation.AFMedInfo;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.google.android.gms.ads.AdRequest;
import com.smartadserver.android.library.SASBannerView;
import com.smartadserver.android.library.SASInterstitialView;
import com.smartadserver.android.library.model.SASAdElement;
import com.smartadserver.android.library.ui.SASAdView;
import com.smartadserver.android.library.ui.SASAdView.StateChangeEvent;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * SmartAdServer interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the Smart AdServer SDK 4.2.2 for Android
 */

public class AFSASInterstitialCustomEvent implements AFMedInterstitialCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFSASInterstitialCustomEvent";

	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;

	// Current interstitial
	private AFSASInterstitialView  m_sasInterstitial;
	
	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// true if ad can be presented
	private boolean m_adCanBePresented;
	
	// SmartAdServer site ID;
	private static String m_siteId;
	
	static public void configure (String siteId) {
		m_siteId = siteId;
	}
	
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
		if (m_sasInterstitial != null) {
			m_sasInterstitial.close();
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onKeyDown (int keyCode, KeyEvent event) {		
	}
	
	@Override
	public void onKeyUp (int keyCode, KeyEvent event) {
		if (m_sasInterstitial != null)
			m_sasInterstitial.onKeyUp(keyCode, event);
	}
	
	@Override
	public void setListener(AFMedInterstitialCustomEventListener listener) {
		m_listener = listener;		
	}
	
	@Override
	public void requestInterstitialWithCustomParameters(Activity activity, Map<String, String> parameters) {
		String formatId = null, pageId = null;
		
		af_Log.d (CLASS_TAG, "requestInterstitialWithCustomParameters: " + parameters);		
		
		// Reset
		if (m_sasInterstitial != null)
			m_sasInterstitial = null;
		m_listenerAlreadyNotified = false;
		m_adCanBePresented = false;
		
		// Get ad unit IDs from payload
		if (parameters != null) {
			pageId = parameters.get("pageId");
			formatId = parameters.get("formatId");
		}
		
		if (m_siteId != null && formatId != null && pageId != null) {
			final AFSASInterstitialCustomEvent adapter = this;
			
			m_sasInterstitial = new AFSASInterstitialView (activity);
			m_sasInterstitial.setVisibility(View.INVISIBLE);
			m_sasInterstitial.addStateChangeListener(new SASAdView.OnStateChangeListener() {				
				@Override
				public void onStateChanged(StateChangeEvent evt) {
					switch (evt.getType ()) {
					case SASAdView.StateChangeEvent.VIEW_EXPANDED:
						if (m_listener != null) {
							m_listener.interstitialCustomEventWillAppear(adapter);
							m_listener.interstitialCustomEventDidAppear(adapter);
						}					
						break;
						
					case SASAdView.StateChangeEvent.VIEW_HIDDEN:
						if (m_listener != null) {
							m_listener.interstitialCustomEventWillDisappear(adapter);
							m_listener.interstitialCustomEventDidDisappear(adapter);
						}					
						break;
						
					default:
						break;
					}
				}
			});
			
			AFMedInfo information = null;		    
			if (m_listener != null)
				information = m_listener.interstitialCustomEventInformation (this);

			if (information != null) {
				m_sasInterstitial.setLocation(adNetworkLocation (information));
			}
						
			m_sasInterstitial.loadAd(Integer.parseInt(m_siteId), pageId, Integer.parseInt(formatId),true,"", new SASAdView.AdResponseHandler() {
				@Override
	            public void adLoadingCompleted(SASAdElement sasAdElement) {
					if (m_listener != null && !m_listenerAlreadyNotified) {
						m_listenerAlreadyNotified = true;
						m_listener.didLoadAd (adapter);
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
		}
		else {
			// Ad unit ID missing
			Log.d (CLASS_TAG, "missing siteId, pageId or formatId, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}
		}
	}

	@Override
	public void presentInterstitial() {
		af_Log.d (CLASS_TAG, "presentInterstitial");	
		if (m_sasInterstitial != null && !m_adCanBePresented) {
			m_adCanBePresented = true;
			m_sasInterstitial.setVisibility(View.VISIBLE);
		}
	}
	
	// Derive SmartAdServer interstitial view class
	
	public class AFSASInterstitialView extends SASInterstitialView {
		public AFSASInterstitialView(Context context) {
			super(context);
		}
		
		@Override
		public void setVisibility (int visibility) {
			if (visibility == View.VISIBLE && !m_adCanBePresented) {
				visibility = View.INVISIBLE;
			}
			super.setVisibility(visibility);
		}
	}
	
	public Location adNetworkLocation (AFMedInfo information) {
		Location location = new Location ("appsfire");
		
		location.setLongitude(information.getLocation().getLongitude());
		location.setLatitude(information.getLocation().getLatitude());
		
		return location;
	}
};
