package com.appsfire.adunitsampleapp;

import android.content.Context;
import android.content.Intent;

import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;
import com.appsfire.appbooster.jar.push.af_GCMBaseIntentService;
import com.appsfire.appbooster.jar.push.af_PushManager;

public final class GCMIntentService extends af_GCMBaseIntentService {

	public GCMIntentService() {
		super(AFSDKFactory.getAFAdSDK().getAPIKey(), af_PushManager.APPSFIRE_SENDER_ID);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		boolean handledByAppbooster = af_PushManager.onMessage(context, intent, MainAdUnitActivity.class, null, R.drawable.icon);
		if (!handledByAppbooster) {
			// Your code here, if needed
		}
	}
}
