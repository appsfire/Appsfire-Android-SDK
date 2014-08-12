package com.appsfire.adunitsampleapp;

import java.util.Arrays;

import android.app.Application;

import com.appsfire.adUnitJAR.sdk.AFAdSDK;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFSDKFeature;
import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;
import com.appsfire.appbooster.jar.af_NotificationsManager;

/**
 * Main application class
 */

public class App extends Application {	
	// true for debug mode, false for production mode (set to false when distributing your app)
	private static final Boolean IS_AFADSDK_DEBUG = true;
		
	// App key
	private static final String YOUR_API_KEY = "YOUR_API_KEY_HERE";
	
	// Create instance of the ad SDK
	private static AFAdSDK adSdk = AFSDKFactory.getAFAdSDK().
								      setAPIKey(YOUR_API_KEY).
								      setFeatures(Arrays.asList(AFSDKFeature.AFSDKFeatureEngage, AFSDKFeature.AFSDKFeatureMonetization)).
								      setEventsDelegate(new AdUnitSampleAppEventsDelegate()).
								      setDebugModeEnabled(IS_AFADSDK_DEBUG);
		
	// RSS feed, used to fill the sashimi activity's listview with content around the ad itself
    private static RSSFeed feed = new RSSFeed ();
    
	public static AFAdSDK getSDK() {
		// Get SDK instance
		return adSdk;
	}
	
	public static RSSFeed getRSSFeed() {
		// Get RSS feed instance
		return feed;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();		
		feed.loadFeed("http://rss.cnn.com/rss/edition_business.rss");
	}
		
	@Override
	public void onTerminate() {
		af_NotificationsManager.destroy(this);
		super.onTerminate();
	}
}
