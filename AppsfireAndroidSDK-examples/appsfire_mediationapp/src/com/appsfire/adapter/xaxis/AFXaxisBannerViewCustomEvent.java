package com.appsfire.adapter.xaxis;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedBannerViewCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.xaxis.mobilesdk.IAdClickListener;
import com.xaxis.mobilesdk.IBannerAd;
import com.xaxis.mobilesdk.IHandleClickToAction;
import com.xaxis.mobilesdk.IReceiveAd;
import com.xaxis.mobilesdk.XAdView;
import com.xaxis.mobilesdk.errorhandler.XAdException;
import com.xaxis.mobilesdk.utilities.XConstant.ACTION_TYPE;

/**
 * AdMob banner view implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the Xaxis SDK 1.3.2 for Android
 */

public class AFXaxisBannerViewCustomEvent implements AFMedBannerViewCustomEvent, IReceiveAd, IBannerAd, IAdClickListener, IHandleClickToAction {
	// Tag for logging
	private final static String CLASS_TAG = "AFXaxisBannerViewCustomEvent";

	// Adapter events listener
	private AFMedBannerViewCustomEventListener m_listener;

	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// Current banner ad view
	private XAdView m_adView;
	
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
		
		String domainName = null, pageName = null, adPosition = null;
		
		// Reset
		if (m_adView != null)
			m_adView = null;
		m_listenerAlreadyNotified = false;
		
		// Get ad unit IDs from payload
		if (parameters != null) {
			domainName = parameters.get("domainName");
			pageName = parameters.get("pageName");
			adPosition = parameters.get("adPosition");
		}
		
		if (domainName != null && pageName != null && adPosition != null) {
			m_adView = new XAdView (activity, this);
			m_adView.setAdClickListener(this);
			m_adView.setAdClickToActionListener(this);
			RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layoutparams.addRule (RelativeLayout.ALIGN_PARENT_TOP);
			m_adView.setLayoutParams(layoutparams);			
			m_adView.setBannerAdEventsListener(this);

			m_adView.getAdSlotConfiguration().setOpenInExternalBrowser(true);
			
			m_adView.loadAd(domainName, pageName, adPosition, null, null);			
		}
		else {
			/* Invalid parameters */
			Log.d (CLASS_TAG, "missing domainName, pageName or adPosition, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}			
		}
	}
	
	@Override
	public void xAdLoaded(View xAdView) {
		af_Log.d (CLASS_TAG, "xAdLoaded");
		if (m_listener != null && !m_listenerAlreadyNotified) {
			m_listenerAlreadyNotified = true;
			m_listener.didLoadAd (this, m_adView);
		}					
	}

	@Override
	public void xAdFailed(View xAdView, XAdException xAdError) {
		af_Log.d (CLASS_TAG, "xAdFailed, error:" + xAdError);
		if (m_listener != null && !m_listenerAlreadyNotified) {
			m_listenerAlreadyNotified = true;
			m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
		}					
	}

	@Override
	public boolean xAdShouldDisplay(View arg0, WebView arg1, String arg2) {
		af_Log.d (CLASS_TAG, "xAdShouldDisplay");
		return true;
	}
	
	@Override
	public void onBrowserOpen(XAdView xAdView) {
		af_Log.d (CLASS_TAG, "onBrowserOpen");
	}

	@Override
	public void onBrowserClose(XAdView xAdView) {
		af_Log.d (CLASS_TAG, "onBrowserClose");
	}

	@Override
	public void onLeaveApplication(XAdView xAdView) {
		af_Log.d (CLASS_TAG, "onLeaveApplication");
		if (m_listener != null) {
			m_listener.bannerViewCustomEventWillLeaveApplication(this);
		}					
	}

	@Override
	public boolean shouldHandleClickToAction(ACTION_TYPE actionType, Intent actionIntent) {
		return true;
	}

	@Override
	public void onBannerAdExpand(XAdView xAdView) {
		if (m_listener != null) {
			m_listener.bannerViewCustomEventBeginOverlayPresentation(this);
		}					
	}

	@Override
	public void onBannerClosed() {
		if (m_listener != null) {
			m_listener.bannerViewCustomEventEndOverlayPresentation(this);
		}					
	}

	@Override
	public void onBannerResize(int w, int h) {
	}
}
