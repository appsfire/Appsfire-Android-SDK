/*
 * Copyright (c) 2011, MoPub Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'MoPub Inc.' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mopub.mobileads;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.millennialmedia.android.*;

import java.util.Map;

import static com.mopub.mobileads.MoPubErrorCode.NETWORK_INVALID_STATE;
import static com.mopub.mobileads.MoPubErrorCode.NETWORK_NO_FILL;

/**
 * Compatible with version 5.3.0 of the Millennial Media SDK.
 */

class MillennialInterstitial extends CustomEventInterstitial {
    private MMInterstitial mMillennialInterstitial;
    private CustomEventInterstitialListener mInterstitialListener;
    public static final String APID_KEY = "adUnitID";

    @Override
    protected void loadInterstitial(final Context context, final CustomEventInterstitialListener customEventInterstitialListener,
                                    final Map<String, Object> localExtras, final Map<String, String> serverExtras) {
        mInterstitialListener = customEventInterstitialListener;

        final String apid;
        if (extrasAreValid(serverExtras)) {
            apid = serverExtras.get(APID_KEY);
        } else {
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        MMSDK.initialize(context);

        final Location location = (Location) localExtras.get("location");
        if (location != null) {
            MMRequest.setUserLocation(location);
        }

        mMillennialInterstitial = new MMInterstitial(context);
        mMillennialInterstitial.setListener(new MillennialInterstitialRequestListener());

        if (mMillennialInterstitial.isAdAvailable()) {
            Log.d("MoPub", "Millennial interstitial ad already loaded.");
            mInterstitialListener.onInterstitialLoaded();
        } else {
            mMillennialInterstitial.setMMRequest(new MMRequest());
            mMillennialInterstitial.setApid(apid);
            mMillennialInterstitial.fetch();
        }
    }

    @Override
    protected void showInterstitial() {
        if (mMillennialInterstitial.isAdAvailable()) {
            mMillennialInterstitial.display();
        } else {
            Log.d("MoPub", "Tried to show a Millennial interstitial ad before it finished loading. Please try again.");
        }
    }

    @Override
    protected void onInvalidate() {
        mMillennialInterstitial.setListener(null);
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        return serverExtras.containsKey(APID_KEY);
    }

    class MillennialInterstitialRequestListener implements RequestListener {
        @Override
        public void MMAdOverlayLaunched(final MMAd mmAd) {
            Log.d("MoPub", "Showing Millennial interstitial ad.");
            mInterstitialListener.onInterstitialShown();
        }

        @Override
        public void MMAdOverlayClosed(final MMAd mmAd) {
            Log.d("MoPub", "Millennial interstitial ad dismissed.");
            mInterstitialListener.onInterstitialDismissed();
        }

        @Override public void MMAdRequestIsCaching(final MMAd mmAd) {}

        @Override
        public void requestCompleted(final MMAd mmAd) {
            if (mMillennialInterstitial.isAdAvailable()) {
                Log.d("MoPub", "Millennial interstitial ad loaded successfully.");
                mInterstitialListener.onInterstitialLoaded();
            } else {
                Log.d("MoPub", "Millennial interstitial ad failed to load.");
                mInterstitialListener.onInterstitialFailed(NETWORK_INVALID_STATE);
            }
        }

        @Override
        public void requestFailed(final MMAd mmAd, final MMException e) {
            Log.d("MoPub", "Millennial interstitial ad failed to load.");
            mInterstitialListener.onInterstitialFailed(NETWORK_NO_FILL);
        }

        @Override
        public void onSingleTap(final MMAd mmAd) {
            Log.d("MoPub", "Millennial interstitial clicked.");
            mInterstitialListener.onInterstitialClicked();
        }

    }
}
