package com.appsfire.adapter.xaxis;

import java.util.Map;

import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.xaxis.mobilesdk.IAdClickListener;
import com.xaxis.mobilesdk.IHandleClickToAction;
import com.xaxis.mobilesdk.IReceiveAd;
import com.xaxis.mobilesdk.IVideoAd;
import com.xaxis.mobilesdk.XAdView;
import com.xaxis.mobilesdk.XInterstitialAdDialog;
import com.xaxis.mobilesdk.errorhandler.XAdException;
import com.xaxis.mobilesdk.utilities.XConstant.ACTION_TYPE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

/**
 * Xaxis interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the Xaxis SDK 1.3.2 for Android
 */

public class AFXaxisInterstitialCustomEvent implements AFMedInterstitialCustomEvent, IReceiveAd, IVideoAd, IAdClickListener, IHandleClickToAction {
	// Tag for logging
	private final static String CLASS_TAG = "AFXaxisInterstitialCustomEvent";

	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;

	// Current interstitial
	private XInterstitialAdDialog m_interstitial;
	
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
		String domainName = null, pageName = null, adPosition = null;
		
		// Reset
		if (m_interstitial != null)
			m_interstitial = null;
		m_listenerAlreadyNotified = false;
		
		// Get ad unit IDs from payload
		if (parameters != null) {
			domainName = parameters.get("domainName");
			pageName = parameters.get("pageName");
			adPosition = parameters.get("adPosition");
		}
		
		if (domainName != null && pageName != null && adPosition != null) {
			final AFXaxisInterstitialCustomEvent adapter = this;
			
			m_interstitial = new XInterstitialAdDialog (activity, domainName, pageName, adPosition, "", "");
			m_interstitial.setAdListener(this);			
			m_interstitial.setAdClickListener(this);
			m_interstitial.setVideoAdListener(this);
			m_interstitial.setClickToActionListener(this);
			m_interstitial.setOnShowListener(new DialogInterface.OnShowListener () {
				@Override
				public void onShow(DialogInterface dialog) {
					af_Log.d (CLASS_TAG, "onShow");
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillAppear(adapter);
						m_listener.interstitialCustomEventDidAppear(adapter);
					}					
				}				
			});
			m_interstitial.setOnDismissListener(new DialogInterface.OnDismissListener() {				
				@Override
				public void onDismiss(DialogInterface dialog) {
					af_Log.d (CLASS_TAG, "onDismiss");
					if (m_listener != null) {
						m_listener.interstitialCustomEventWillDisappear(adapter);
						m_listener.interstitialCustomEventDidDisappear(adapter);
					}					
				}
			});
			
			m_interstitial.show();
		}
		else {
			// Ad unit ID missing
			Log.d (CLASS_TAG, "missing domainName, pageName or adPosition, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}
		}
	}

	@Override
	public void presentInterstitial() {
		af_Log.d (CLASS_TAG, "presentInterstitial");
	}

	@Override
	public void xAdLoaded(View xAdView) {
		af_Log.d (CLASS_TAG, "xAdLoaded");
		if (m_listener != null && !m_listenerAlreadyNotified) {
			m_listenerAlreadyNotified = true;
			m_listener.didLoadAd (this);
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
	public void onVideoPlay() {
	}
	
	@Override
	public void onVideoPause(long duration) {
	}

	@Override
	public void onVideoResume(long duration) {
	}

	@Override
	public void onVideoSkip(long duration) {
	}

	@Override
	public void onMuteVideo() {
	}

	@Override
	public void onUnMuteVideo() {
	}

	@Override
	public void onQuartileFinish(int xVideoQuartile) {
	}

	@Override
	public void onVideoPlayerEnterFullScreenMode() {
	}

	@Override
	public void onVideoPlayerExitFullScreenMode() {
	}

	@Override
	public void onVideoPlayerRewind(long fromduration, long toduration) {
	}

	@Override
	public void onVideoClick(MotionEvent event) {
	}

	@Override
	public void onPrerollAdFinished() {
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
			m_listener.interstitialCustomEventWillLeaveApplication(this);
		}					
	}

	@Override
	public boolean shouldHandleClickToAction(ACTION_TYPE actionType, Intent actionIntent) {
		return true;
	}
};
