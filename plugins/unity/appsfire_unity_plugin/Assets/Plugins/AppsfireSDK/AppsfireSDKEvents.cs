#if UNITY_ANDROID

using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;

public enum AFSDKErrorCode
{
    // base sdk
    AFSDKErrorCodeUnknown,                              // unknown
    AFSDKErrorCodeLibraryNotInitialized,                // library isn't initialized yet
    AFSDKErrorCodeInternetNotReachable,                 // internet isn't reachable (and is required)
    AFSDKErrorCodeNeedsApplicationDelegate,             // you need to set the application delegate to proceed
    // advertising sdk / requesting a modal ad
    AFSDKErrorCodeAdvertisingNoAd,                      // no ad available
    AFSDKErrorCodeAdvertisingBadCall,                   // the request call isn't appropriate
    AFSDKErrorCodeAdvertisingAlreadyDisplayed,          // an ad is currently displayed for this format
    AFSDKErrorCodeAdvertisingCanceledByDevelopper,      // the request was canceled by the developer
    // base sdk / presenting panel
    AFSDKErrorCodePanelAlreadyDisplayed,                // the panel is already displayed
    // base sdk / open notification
    AFSDKErrorCodeOpenNotificationNotFound              // the notification wasn't found
}

public class AppsfireSDKEvents : MonoBehaviour {
    [DllImport("libafadunitnative")]
    private static extern bool AFAdSDK_setUserValue(string strUserValue);
   
    /* Ad SDK events */

	// did initialize
	public delegate void AFSDKAdDidInitializeHandler();
	public static event AFSDKAdDidInitializeHandler afsdkIsInitialized;
	
	// modal ad is ready for request
	public delegate void AFSDKAdModalAdIsReadyForRequestHandler();
	public static event AFSDKAdModalAdIsReadyForRequestHandler afsdkadModalAdsRefreshedAndAvailable;

	// modal ad request did fail with error code
	public delegate void AFSDKAdModalAdRequestDidFailWithErrorCodeHandler(AFSDKErrorCode errorCode);
	public static event AFSDKAdModalAdRequestDidFailWithErrorCodeHandler afsdkadModalAdRequestDidFailWithErrorCode;
	
	// modal ad will appear
	public delegate void AFSDKAdModalAdWillAppearHandler();
	public static event AFSDKAdModalAdWillAppearHandler afsdkadModalAdWillAppear;

	// modal ad did appear
	public delegate void AFSDKAdModalAdDidAppearHandler();
	public static event AFSDKAdModalAdDidAppearHandler afsdkadModalAdDidAppear;

	// modal ad will disappear
	public delegate void AFSDKAdModalAdWillDisappearHandler();
	public static event AFSDKAdModalAdWillDisappearHandler afsdkadModalAdWillWisappear;

	// modal ad did disappear
	public delegate void AFSDKAdModalAdDidDisappearHandler();
	public static event AFSDKAdModalAdDidDisappearHandler afsdkadModalAdDidDisappear;

    /*
     *	Awake
     */

    void Awake()
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            gameObject.name = this.GetType().ToString();
            AFAdSDK_setUserValue(gameObject.name);
        }
        DontDestroyOnLoad(this);
    }
	
	/*
	 *	Events
	 */
	
	// sdk did initialize
	public void AFSDKAdDidInitialize(string empty)
	{
		if (afsdkIsInitialized != null)
			afsdkIsInitialized();
	}
	
	// modal ad is ready for request
	public void AFSDKAdModalAdIsReadyForRequest(string empty)
	{
		if (afsdkadModalAdsRefreshedAndAvailable != null)
			afsdkadModalAdsRefreshedAndAvailable();
	}
	
	// modal ad request did fail witht error code
	public void AFSDKAdModalAdRequestDidFailWithErrorCode(string errorCode)
	{
		if (afsdkadModalAdRequestDidFailWithErrorCode != null)
			afsdkadModalAdRequestDidFailWithErrorCode((AFSDKErrorCode)Int32.Parse(errorCode));
	}
	
	// modal ad will appear
	public void AFSDKAdModalAdWillAppear(string empty)
	{
		if (afsdkadModalAdWillAppear != null)
			afsdkadModalAdWillAppear();
	}
	
	// modal ad did appear
	public void AFSDKAdModalAdDidAppear(string empty)
	{
		if (afsdkadModalAdDidAppear != null)
			afsdkadModalAdDidAppear();
	}

	// modal ad will disappear
	public void AFSDKAdModalAdWillDisappear(string empty)
	{
		if (afsdkadModalAdWillWisappear != null)
			afsdkadModalAdWillWisappear();
	}
	
	// modal ad did disappear
	public void AFSDKAdModalAdDidDisappear(string empty)
	{		
		if (afsdkadModalAdDidDisappear != null)
			afsdkadModalAdDidDisappear();
	}
	
}

#endif

#if UNITY_IPHONE

using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;

public enum AFSDKErrorCode
{
    // General
    /** Unknown */
    AFSDKErrorCodeUnknown,
    /** Library isn't initialized yet */
    AFSDKErrorCodeLibraryNotInitialized,
    /** Internet isn't reachable (and is required) */
    AFSDKErrorCodeInternetNotReachable,
    /** You need to set the application delegate to proceed */
    AFSDKErrorCodeNeedsApplicationDelegate,
    
    // Initialization
    /** Missing the API Key */
    AFSDKErrorCodeInitializationMissingAPIKey,
    /** Missing the Features bitmask */
    AFSDKErrorCodeInitializationMissingFeatures,
    
    // Advertising sdk
    /** No ad available */
    AFSDKErrorCodeAdvertisingNoAd,
    /** The request call isn't appropriate */
    AFSDKErrorCodeAdvertisingBadCall,
    /** An ad is currently displayed for this format */
    AFSDKErrorCodeAdvertisingAlreadyDisplayed,
    /** The request was canceled by the developer */
    AFSDKErrorCodeAdvertisingCanceledByDevelopper,
    
    // Engage sdk
    /** The panel is already displayed */
    AFSDKErrorCodePanelAlreadyDisplayed,
    /** The notification wasn't found */
    AFSDKErrorCodeOpenNotificationNotFound,
    
    // In-app purchase
    /** The property object is not valid */
    AFSDKErrorCodeIAPPropertyNotValid,
    /** The property object is missing a title attribute */
    AFSDKErrorCodeIAPTitleMissing,
    /** The property object is missing a message attribute */
    AFSDKErrorCodeIAPMessageMissing,
    /** The property object is missing a cancel button title attribute */
    AFSDKErrorCodeIAPCancelButtonTitleMissing,
    /** The property object is missing a buy button title attribute */
    AFSDKErrorCodeIAPBuyButtonTitleMissing,
    /** The property object is missing a buy block handler */
    AFSDKErrorCodeIAPBuyBlockMissing,
    
    // Mediation sdk
    /** The placement id is not valid. */
    AFSDKErrorCodeMediationPlacementIdNotValid,
    /** The payload received for the placement id is not valid */
    AFSDKErrorCodeMediationPayloadNotValid,
    /** The received custom class does not exist */
    AFSDKErrorCodeMediationCustomClassNotFound,
    /** The received custom class does not conform to protocol */
    AFSDKErrorCodeMediationCustomClassNotConformToProtocol,
    /** The received placement id does not have any ads to show */
    AFSDKErrorCodeMediationNoAds,
    /** The ad request timed out */
    AFSDKErrorCodeMediationAdRequestTimeout,
    /** The interstitial has expired, you need to create a new one */
    AFSDKErrorCodeMediationInterstitialExpired,
    /** The interstitial has already been used, you need to create a new one */
    AFSDKErrorCodeMediationInterstitialAlreadyUsed,
}

public class AppsfireSDKEvents : MonoBehaviour {

	/* Interface to native implementation */
	
	[DllImport("__Internal")]
	private static extern void afsdk_iniAndSetCallbackHandler(string handlerName);
	
	/* Base SDK Events */

	// is initializing
	public delegate void AFSDKIsInitializingHandler();
	public static event AFSDKIsInitializingHandler afsdkIsInitializing;

	// is initialized
	public delegate void AFSDKIsInitializedHandler();
	public static event AFSDKIsInitializedHandler afsdkIsInitialized;
	
	/* Engage SDK Events */
	
	// notifications count was updated
	public delegate void AFSDKNotificationsNumberChangedHandler();
	public static event AFSDKNotificationsNumberChangedHandler afsdkNotificationsNumberChanged;
	
	// panel was presented
	public delegate void AFSDKPanelWasPresentedHandler();
	public static event AFSDKPanelWasPresentedHandler afsdkPanelWasPresented;
	
	// panel was dismissed
	public delegate void AFSDKPanelWasDismissedHandler();
	public static event AFSDKPanelWasDismissedHandler afsdkPanelWasDismissed;
	
	/* Advertising SDK Delegate */
	
	// modal ads refreshed and available
	public delegate void AFSDKAdModalAdsRefreshedAndAvailableHandler();
	public static event AFSDKAdModalAdsRefreshedAndAvailableHandler afsdkadModalAdsRefreshedAndAvailable;

	// modal ads refreshed and none is available
	public delegate void AFSDKAdModalAdsRefreshedAndNotAvailableHandler();
	public static event AFSDKAdModalAdsRefreshedAndNotAvailableHandler afsdkadModalAdsRefreshedAndNotAvailable;

	/* Modal Ad Delegate */

	// modal ad request did fail with error code
	public delegate void AFSDKAdModalAdRequestDidFailWithErrorCodeHandler(AFSDKErrorCode errorCode);
	public static event AFSDKAdModalAdRequestDidFailWithErrorCodeHandler afsdkadModalAdRequestDidFailWithErrorCode;
	
	// modal ad will appear
	public delegate void AFSDKAdModalAdWillAppearHandler();
	public static event AFSDKAdModalAdWillAppearHandler afsdkadModalAdWillAppear;

	// modal ad did appear
	public delegate void AFSDKAdModalAdDidAppearHandler();
	public static event AFSDKAdModalAdDidAppearHandler afsdkadModalAdDidAppear;

	// modal ad will disappear
	public delegate void AFSDKAdModalAdWillDisappearHandler();
	public static event AFSDKAdModalAdWillDisappearHandler afsdkadModalAdWillWisappear;

	// modal ad did disappear
	public delegate void AFSDKAdModalAdDidDisappearHandler();
	public static event AFSDKAdModalAdDidDisappearHandler afsdkadModalAdDidDisappear;
			
	/*!
	 *	Awake
	 */
	
	void Awake()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
		{
			gameObject.name = this.GetType().ToString();
			afsdk_iniAndSetCallbackHandler(gameObject.name);
		}
		//
		DontDestroyOnLoad(this);
	}  
	
	/*
	 *	Events Base SDK
	 */
	
	// sdk is initializing
	public void AFSDKIsInitializing(string empty)
	{
		if (afsdkIsInitializing != null)
			afsdkIsInitializing();
	}
	
	// sdk is initialized
	public void AFSDKIsInitialized(string empty)
	{
		if (afsdkIsInitialized != null)
			afsdkIsInitialized();		
	}
	
	/*
	 *	Events Engage SDK
	 */
	
	// notifications count was updated
	public void AFSDKNotificationsNumberChanged(string empty)
	{
		if (afsdkNotificationsNumberChanged != null)
			afsdkNotificationsNumberChanged();
	}
	
	// panel was presented
	public void AFSDKPanelWasPresented(string empty)
	{
		if (afsdkPanelWasPresented != null)
			afsdkPanelWasPresented();
	}
	
	// panel was dismissed
	public void AFSDKPanelWasDismissed(string empty)
	{
		if (afsdkPanelWasDismissed != null)
			afsdkPanelWasDismissed();
	}
	
	/*
	 *	Events Monetization SDK
	 */
	
	// modal ads refreshed and available
	public void AFSDKAdModalAdsRefreshedAndAvailable(string empty)
	{
		if (afsdkadModalAdsRefreshedAndAvailable != null)
			afsdkadModalAdsRefreshedAndAvailable();
	}
	
	// modal ads refreshed and none is available
	public void AFSDKAdModalAdsRefreshedAndNotAvailable(string empty)
	{
		if (afsdkadModalAdsRefreshedAndNotAvailable != null)
			afsdkadModalAdsRefreshedAndNotAvailable();
	}
	
	/*
	 *	Events Modal Ad
	 */
	
	// modal ad request did fail witht error code
	public void AFSDKAdModalAdRequestDidFailWithErrorCode(string errorCode)
	{
		if (afsdkadModalAdRequestDidFailWithErrorCode != null)
			afsdkadModalAdRequestDidFailWithErrorCode((AFSDKErrorCode)Int32.Parse(errorCode));
	}
	
	// modal ad will appear
	public void AFSDKAdModalAdWillAppear(string empty)
	{
		if (afsdkadModalAdWillAppear != null)
			afsdkadModalAdWillAppear();
	}
	
	// modal ad did appear
	public void AFSDKAdModalAdDidAppear(string empty)
	{
		if (afsdkadModalAdDidAppear != null)
			afsdkadModalAdDidAppear();
	}

	// modal ad will disappear
	public void AFSDKAdModalAdWillDisappear(string empty)
	{
		if (afsdkadModalAdWillWisappear != null)
			afsdkadModalAdWillWisappear();
	}
	
	// modal ad did disappear
	public void AFSDKAdModalAdDidDisappear(string empty)
	{		
		if (afsdkadModalAdDidDisappear != null)
			afsdkadModalAdDidDisappear();
	}
	
}

#endif
