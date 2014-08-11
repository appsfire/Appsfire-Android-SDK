/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var af;

var app = {    
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        sushiButton.addEventListener('touchstart', this.onAdRequest, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        var apiKey = 'F8E8C46EF05A02959E455019A0B8C558';
        var debugModeEnabled = true;

        af = new AppsfireSDK();
        af.sdk_getAFSDKVersionInfo(
          function(sdkVersion) {
            console.log('Appsfire SDK version: ' + sdkVersion);
          }, function() {
            console.log('Failed to get SDK version');
        });

        af.sdk_connectWithApiKey(
           apiKey,
           function() {
              console.log('Connected to the API, set debug mode');

              af.adsdk_setDebugModeEnabled(debugModeEnabled,
              function() {
                console.log('Debug mode set, prepare');

                af.adsdk_prepare();

                af.adsdk_isThereAModalAdAvailable(
                  function(adIsAvailable) {
                    console.log('Is Sushi ad available: ' + adIsAvailable);
                  }, "sushi");
              }, function() {
                console.log('Failed to set debug mode');
              });
           }, function() {
              console.log('Not connected to the API.');
        });
    },
    // Ad request button pushed
    onAdRequest: function() {
      console.log('Sushi interstitial ad requested');
      af.adsdk_requestModalAd ("sushi", false);
    },
};
