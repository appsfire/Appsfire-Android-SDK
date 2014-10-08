package com.appsfire.adapter.chartboost;

import java.util.Map;

import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEvent;
import com.appsfire.adUnitJAR.mediation.AFMedInterstitialCustomEventListener;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.appbooster.jar.af_Log;
import com.chartboost.sdk.*;
import com.chartboost.sdk.Chartboost.CBAgeGateConfirmation;
import com.chartboost.sdk.Model.CBError.CBClickError;
import com.chartboost.sdk.Model.CBError.CBImpressionError;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * ChartBoost interstitials implementation for ad mediation
 * Compatible with the Appsfire SDK 2.1 for Android
 * Compatible with the Chartboost SDK 4.1.1 for Android
 */

public class AFChartBoostInterstitialCustomEvent implements AFMedInterstitialCustomEvent {
	// Tag for logging
	private final static String CLASS_TAG = "AFChartBoostInterstitialCustomEvent";

	// Chartboost instance
	private static Chartboost m_cb;
	
	// App ID
	private static String m_appId;
	
	// App signature
	private static String m_appSignature;
	
	// Adapter events listener
	private AFMedInterstitialCustomEventListener m_listener;

	// Requested location
	private String m_location;
	
	// Activity that requested the ad
	private Activity m_activity;
	
	// True if interstitial is ready to show
	private boolean m_bInterstitialReady;
	
	// true if we already notified the adapter listener of the success or failure of loading an ad
	private boolean m_listenerAlreadyNotified;
	
	// true if status bar is hidden
	private boolean m_bStatusBarHidden = false;
	
	// true if action bar is hidden
	private boolean m_bActionBarHidden = false;
	
	static public void configure (String appId, String appSignature) {
		m_appId = appId;
		m_appSignature = appSignature;
	}
	
	@Override
	public void destroy() {
		af_Log.d (CLASS_TAG, "destroy");
	}

	@Override
	public void pause() {
		af_Log.d (CLASS_TAG, "pause");
		if (m_cb != null && m_activity != null)
			m_cb.onStop(m_activity);
	}

	@Override
	public void resume() {
		af_Log.d (CLASS_TAG, "resume");
		if (m_cb != null && m_activity != null)
			m_cb.onStart(m_activity);
	}
	
	@Override
	public boolean onBackPressed () {
		if (m_cb != null)
			return m_cb.onBackPressed();
		else
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
		
		// Reset
		m_listenerAlreadyNotified = false;
		m_bInterstitialReady = false;
		
		// Get ad unit ID from payload
		if (parameters != null) {
			m_location = parameters.get("location");
		}
		
		if (m_appId != null && m_appSignature != null) {
			final AFChartBoostInterstitialCustomEvent adapter = this;
			
			if (m_cb == null || m_activity != activity) {
				// Initialize Chartboost
				af_Log.d (CLASS_TAG, "initialize");
				m_activity = activity;
				m_cb = Chartboost.sharedChartboost();
				m_cb.onCreate (activity, m_appId, m_appSignature, new ChartboostDelegate () {
					@Override
					public void didCacheInterstitial(String ad) {
						af_Log.d (CLASS_TAG, "didCacheInterstitial");
						m_bInterstitialReady = true;
						if (m_listener != null && !m_listenerAlreadyNotified) {
							m_listenerAlreadyNotified = true;
							m_listener.didLoadAd (adapter);
						}						
					}

					@Override
					public void didFailToLoadInterstitial(String ad, CBImpressionError err) {
						af_Log.d (CLASS_TAG, "didFailToLoadInterstitial, error = " + err);
						if (m_listener != null && !m_listenerAlreadyNotified) {
							m_listenerAlreadyNotified = true;
							m_listener.didFailToLoadAdWithError(adapter, AFAdSDKError.AFSDKErrorCodeAdvertisingNoAd);
						}					
					}
					
					@Override
					public boolean shouldRequestInterstitialsInFirstSession() {
						return true;
					}

					@Override
					public boolean shouldRequestInterstitial(String ad) {
						return true;
					}

					@Override
					public boolean shouldDisplayInterstitial(String ad) {
						af_Log.d (CLASS_TAG, "shouldDisplayInterstitial");
						return true;
					}

					@Override
					public void didShowInterstitial(String ad) {
						af_Log.d (CLASS_TAG, "didShowInterstitial");
						hideStatusBar ();
						if (m_listener != null) {
							m_listener.interstitialCustomEventWillAppear(adapter);
							m_listener.interstitialCustomEventDidAppear(adapter);
						}					
					}

					@Override
					public void didClickInterstitial(String ad) {
						af_Log.d (CLASS_TAG, "didClickInterstitial");
						if (m_listener != null) {
							m_listener.interstitialCustomEventWillLeaveApplication(adapter);
						}					
					}

					@Override
					public void didDismissInterstitial(String ad) {
						af_Log.d (CLASS_TAG, "didDismissInterstitial");
						if (m_listener != null) {
							m_listener.interstitialCustomEventWillDisappear(adapter);
						}
					}

					@Override
					public void didCloseInterstitial(String ad) {
						af_Log.d (CLASS_TAG, "didCloseInterstitial");
						showStatusBar ();
						if (m_listener != null) {
							m_listener.interstitialCustomEventDidDisappear(adapter);
						}					
					}

					@Override
					public void didCacheMoreApps() {
					}

					@Override
					public void didClickMoreApps() {
					}

					@Override
					public void didCloseMoreApps() {
					}

					@Override
					public void didDismissMoreApps() {
					}

					@Override
					public void didFailToLoadMoreApps(CBImpressionError arg0) {
					}

					@Override
					public void didFailToRecordClick(String arg0, CBClickError arg1) {
					}

					@Override
					public void didShowMoreApps() {
					}

					@Override
					public boolean shouldDisplayLoadingViewForMoreApps() {
						return false;
					}

					@Override
					public boolean shouldDisplayMoreApps() {
						return false;
					}

					@Override
					public boolean shouldPauseClickForConfirmation( CBAgeGateConfirmation arg0) {
						return false;
					}

					@Override
					public boolean shouldRequestMoreApps() {
						return false;
					}					
				});
				m_cb.onStart(m_activity);
			}
			
			// Begin loading interstitial
			if (m_location != null && !m_location.equals(" ")) {
				af_Log.d (CLASS_TAG, "cache interstitial for location: " + m_location);
				m_cb.cacheInterstitial(m_location);
			}
			else {
				af_Log.d (CLASS_TAG, "cache interstitial");
				m_cb.cacheInterstitial();
			}
		}
		else {
			// Ad unit ID missing
			Log.d (CLASS_TAG, "missing appId or appSignature, giving up");
			if (m_listener != null && !m_listenerAlreadyNotified) {
				m_listenerAlreadyNotified = true;
				m_listener.didFailToLoadAdWithError(this, AFAdSDKError.AFSDKErrorCodeMediationPayloadNotValid);
			}
		}
	}

	@Override
	public void presentInterstitial() {
		af_Log.d (CLASS_TAG, "presentInterstitial");
		
		if (m_bInterstitialReady && m_cb != null) {
			if (m_location != null && !m_location.equals(" ")) {
				af_Log.d (CLASS_TAG, "show interstitial for location: " + m_location);
				m_cb.showInterstitial(m_location);
			}
			else {
				af_Log.d (CLASS_TAG, "show interstitial");
				m_cb.showInterstitial();
			}
		}
	}
	
	@SuppressLint("NewApi")
	private void hideStatusBar() {
		if (m_activity != null) {
	        // Hide status bar
	        Window appWindow = m_activity.getWindow();
	        if (Build.VERSION.SDK_INT < 16) {
	        	m_bStatusBarHidden = (appWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
		        appWindow.setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        }
	        else {
	        	m_bStatusBarHidden = (appWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
		        appWindow.setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		        
	        	ActionBar actionBar = m_activity.getActionBar();
	        	if (actionBar != null) {
		        	m_bActionBarHidden = actionBar.isShowing();
		        	actionBar.hide();
	        	}
	        }
		}
	}
	
	@SuppressLint("NewApi")
	private void showStatusBar() {
		if (m_activity != null) {
	        // Show status bar
	        Window appWindow = m_activity.getWindow();
	        if (Build.VERSION.SDK_INT < 16) {
	        	if (m_bStatusBarHidden) {
			        appWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        	}
	        }
	        else {
	        	if (m_bStatusBarHidden) {
			        appWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        	}
	        	
		        if (m_bActionBarHidden) {
		        	ActionBar actionBar = m_activity.getActionBar();
		        	actionBar.show();        	
		        }
	        }
		}
	}
};
