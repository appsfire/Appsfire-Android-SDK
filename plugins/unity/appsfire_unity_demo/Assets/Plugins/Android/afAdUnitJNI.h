/*
 * JNI interface for the Appsfire Ad SDK - definitions
 */

#ifndef _AFADUNITJNI_H

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Error codes returned by the SDK
 */

typedef enum {	
   AFSDKSuccess = 0,
   AFSDKErrorCodeUnknown = -1,
	AFSDKErrorCodeLibraryNotInitialized = -2,
	AFSDKErrorCodeInternetNotReachable = -3,
	AFSDKErrorCodeAdvertisingNoAd = -5,
	AFSDKErrorCodeAdvertisingBadCall = -6,
	AFSDKErrorCodeAdvertisingAlreadyDisplayed = -7,
} AFAdSDKResult;

/**
 * Ad SDK events
 */

typedef enum {
   AFSDKEventAdsLoaded = 1,
   AFSDKEventAdUnitInitialized = 2,
   AFSDKEventEngageSDKInitialized = 3,
   AFSDKEventModalAdAvailable = 4,
   AFSDKEventModalAdFailedToDisplay = 5,
   AFSDKEventModalAdCancelled = 6,
   AFSDKEventModalAdUnknownType = 7,
   AFSDKEventModalAdPreDisplay = 10,
   AFSDKEventModalAdDisplayed = 11,
   AFSDKEventModalAdPreDismiss = 12,
   AFSDKEventModalAdDismissed = 13,
} AfAdSDKEventType;

/**
 * Defines the type of possible modal ads
 */

typedef enum {
	/** Native fullscreen ad */
	AFAdSDKModalTypeSushi = 1,
		
	/** Native interstitial ad */
	AFAdSDKModalTypeUraMaki,
} AFAdSDKModalType;

/**
 * Sets your app's API key to be used by the ad SDK
 *
 * \param lpszApiKey API key
 */
extern void AFAdSDK_setAPIKey (const char *lpszApiKey);

/**
 * Specify if the library should be used in debug mode. When this mode is enabled, the ad sdk will provide uncapped test
 * ads. By default, this mode is disabled and should be disabled when submitting your app to Google Play or other appstores
 * 
 * \param bEnable true if the debug mode should be enabled, false if production mode should be enabled
 */
extern void AFAdSDK_setDebugModeEnabled (bool bEnable);

/**
 * Tell the library to initialize itself and pre-load data, before loading ads. If not already done, this method is automatically
 * called when requesting a modal ad.
 *
 * \param c application context
 */
extern void AFAdSDK_prepare (void);

/**
 * Set SDK event handler
 *
 * \param eventHandler method called back when AD sdk events occur, NULL to clear
 * \param lpOpaqueData opaque data passed back to the event handler, NULL for none
 */
extern void AFAdSDK_setEventHandler(void (*eventHandler)(AfAdSDKEventType nSdkEvent, void *lpOpaqueData), void *lpOpaqueData);

/**
 * Poll to ask if the ad payload has been downloaded yet and if ads are available
 *
 * \return true if there are ads available in the downloaded payload, false if not
 */
extern bool AFAdSDK_areAdsLoaded();

/**
 * Poll to ask if a modal ad is being displayed right now by the Ad SDK.
 * 
 * \return true if a modal ad is being displayed, false otherwise
 */
extern bool AFAdSDK_isModalAdDisplayingNow();

/**
 * Poll to ask if ads are loaded and if there is at least one modal ad available.
 * 
 * \param nModalAdType modal ad type
 * 
 * \return true if ads are loaded and if there is at least one modal ad of the requested type available, false otherwise
 */
extern bool AFAdSDK_isAModalAdOfTypeAvailable (AFAdSDKModalType nModalAdType);

/**
 * Request modal ad
 *
 * \param nModalAdType modal ad type (such as AFAdSDKModalTypeSushi)
 *
 * \return AFSDKSuccess for success, or an error code (such as AFSDKErrorCodeAdvertisingAlreadyDisplayed)
 */
extern AFAdSDKResult AFAdSDK_requestModalAd (AFAdSDKModalType nModalAdType);

/**
 * Get app-specific value
 *
 * \return user value
 *
 * \private
 */
extern const char *AFAdSDK_getUserValue (void);

/**
 * Set app-specific value
 *
 * \param lpszUserValue user value
 *
 * \private
 */
extern void AFAdSDK_setUserValue (const char *lpszUserValue);

#ifdef __cplusplus
}
#endif

#endif /* _AFADUNITJNI_H */
