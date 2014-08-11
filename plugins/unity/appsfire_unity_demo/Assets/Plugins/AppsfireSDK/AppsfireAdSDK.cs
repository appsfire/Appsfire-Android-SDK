#if UNITY_ANDROID

using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;

public enum AFAdSDKModalType
{
	AFAdSDKModalTypeSushi = 1,
	AFAdSDKModalTypeUraMaki
}

public enum AFAdSDKAdAvailability
{
	AFAdSDKAdAvailabilityPending = 0,
	AFAdSDKAdAvailabilityYes,
	AFAdSDKAdAvailabilityNo
}

public class AppsfireAdSDK : MonoBehaviour {
	
	/* Interface to native implementation */
	
	[DllImport ("libafadunitnative")]
    private static extern void AFAdSDK_prepare();
	
	[DllImport ("libafadunitnative")]
    private static extern bool AFAdSDK_areAdsLoaded();
	
	[DllImport ("libafadunitnative")]
    private static extern void AFAdSDK_setDebugModeEnabled(bool bEnable);
		
	[DllImport ("libafadunitnative")]
    private static extern void AFAdSDK_requestModalAd(AFAdSDKModalType modalType);
		
	[DllImport ("libafadunitnative")]
    private static extern bool AFAdSDK_isAModalAdOfTypeAvailable(AFAdSDKModalType modalType);

	[DllImport ("libafadunitnative")]
    private static extern bool AFAdSDK_isModalAdDisplayingNow();

	
	/*!
	 *  @brief Green light so the library can prepare itself.
	 *
	 *  @note If not already done, this method is automatically called at during a modal ad request.
	 */
	public static void Prepare()
	{
		if (Application.platform == RuntimePlatform.Android)
            AFAdSDK_prepare();
	}
	
	/*!
	 *  @brief Ask if AdUnit is initialized
	 *
	 *  @note There are various checks like waiting for Appboster SDK initialization, internet connection ...
	 *  Usually the library is quickly initialized ( < 1s ).
	 *
	 *  @return `YES` if the library is initialized, `NO` if the library isn't yet.
	 */
	public static bool IsInitialized()
	{
        if (Application.platform == RuntimePlatform.Android)
            return AFAdSDK_areAdsLoaded();
		return false;
	}
		
	/*!
	 *  @brief Specify if the library should be used in debug mode.
	 *
	 *  @note Whenever this mode is enabled, the web service will return a fake ad.
	 *  By default, this mode is disabled. You must decide if you want to enable the debug mode before any prepare/request.
	 *
	 *  @param use A boolean to specify if the debug mode should be enabled.
	 */
	public static void SetDebugModeEnabled(bool use)
	{
        if (Application.platform == RuntimePlatform.Android)
            AFAdSDK_setDebugModeEnabled(use);
	}
		
	/*!
	 *  @brief Request a modal ad.
	 *
	 *  @note If the library isn't initialized, or if the ads aren't loaded yet, then the request will be added to a queue and treated as soon as possible.
	 *  You cannot request two ad modals at the same time. In the case where you already have a modal request in the queue, the previous one will be canceled.
	 */
	public static void RequestModalAd(AFAdSDKModalType modalType)
	{
        if (Application.platform == RuntimePlatform.Android)
            AFAdSDK_requestModalAd(modalType);
	}
	
	/*!
	 *  @brief Ask if ads are loaded and if there is at least one modal ad available
	 *
	 *  @note If ads aren't downloaded yet, then the method will return `AFAdSDKAdAvailabilityPending`.
	 *  To test the library, and then have always have a positive response, please use the "debug" mode (see online documentation for more precisions).
	 *
	 *  @param modalType The kind of modal you want to check.
	 *  Note that most of ads should be available for both formats.
	 *
	 *  @return `AFAdSDKAdAvailabilityPending` if ads aren't loaded yet, `AFAdSDKAdAvailabilityYes` and if there is at least one modal ad available, `AFAdSDKAdAvailabilityNo` otherwise.
	 */
	public static AFAdSDKAdAvailability IsThereAModalAdAvailable(AFAdSDKModalType modalType)
	{
        if (Application.platform == RuntimePlatform.Android)
            return AFAdSDK_isAModalAdOfTypeAvailable(modalType) ? AFAdSDKAdAvailability.AFAdSDKAdAvailabilityYes : AFAdSDKAdAvailability.AFAdSDKAdAvailabilityNo;
		return AFAdSDKAdAvailability.AFAdSDKAdAvailabilityNo;
	}
	
	/*!
	 *  @brief Cancel any pending ad modal request you have made in the past.
	 *
	 *  @return `YES` if a modal ad was canceled, `NO` otherwise.
	 *  If `YES` is returned, you'll get an delegate event via 'modalAdRequestDidFailWithError:'.
	 */
	public static bool CancelPendingAdModalRequest()
	{
//      if (Application.platform == RuntimePlatform.Android)
//			return afsdkad_cancelPendingAdModalRequest();
		return false;
	}
	
	/*!
	 *  @brief Check if there is any modal ad being displayed right now by the library.
	 *
	 *  @return `YES` if a modal ad is being displayed, `NO` otherwise
	 */
	public static bool IsModalAdDisplayed()
	{
        if (Application.platform == RuntimePlatform.Android)
            return AFAdSDK_isModalAdDisplayingNow();
		return false;
	}		
}

#endif

#if UNITY_IPHONE

using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;

public enum AFAdSDKModalType
{
	AFAdSDKModalTypeSushi = 0,
	AFAdSDKModalTypeUraMaki
}

public enum AFAdSDKAdAvailability
{
	AFAdSDKAdAvailabilityPending = 0,
	AFAdSDKAdAvailabilityYes,
	AFAdSDKAdAvailabilityNo
}

public class AppsfireAdSDK : MonoBehaviour {
	
	/* Interface to native implementation */
		
	[DllImport ("__Internal")]
	private static extern void afsdkad_setUseInAppDownloadWhenPossible(bool use);
	
	[DllImport ("__Internal")]
	private static extern void afsdkad_setDebugModeEnabled(bool use);
		
	[DllImport ("__Internal")]
	private static extern void afsdkad_requestModalAd(AFAdSDKModalType modalType);
		
	[DllImport ("__Internal")]
	private static extern AFAdSDKAdAvailability afsdkad_isThereAModalAdAvailable(AFAdSDKModalType modalType);

	[DllImport ("__Internal")]
	private static extern bool afsdkad_cancelPendingAdModalRequest();
	
	[DllImport ("__Internal")]
	private static extern bool afsdkad_isModalAdDisplayed();
		
	[DllImport ("__Internal")]
	private static extern void afsdkad_prepare();

	[DllImport ("__Internal")]
	private static extern bool afsdkad_isInitialized();

	/*!
	 *  @brief Specify if the library should use the in-app overlay when possible.
	 *
	 *  @note If the client does not have iOS6+, it will be redirected to the App Store app.
	 *
	 *  @param use A boolean to specify the choice.
	 */
	public static void SetUseInAppDownloadWhenPossible(bool use)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdkad_setUseInAppDownloadWhenPossible(use);
	}
	
	/*!
	 *  @brief Specify if the library should be used in debug mode.
	 *
	 *  @note Whenever this mode is enabled, the web service will return a fake ad.
	 *  By default, this mode is disabled. You must decide if you want to enable the debug mode before any prepare/request.
	 *
	 *  @param use A boolean to specify if the debug mode should be enabled.
	 */
	public static void SetDebugModeEnabled(bool use)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdkad_setDebugModeEnabled(use);
	}
		
	/*!
	 *  @brief Request a modal ad.
	 *
	 *  @note If the library isn't initialized, or if the ads aren't loaded yet, then the request will be added to a queue and treated as soon as possible.
	 *  You cannot request two ad modals at the same time. In the case where you already have a modal request in the queue, the previous one will be canceled.
	 */
	public static void RequestModalAd(AFAdSDKModalType modalType)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdkad_requestModalAd(modalType);
	}
	
	/*!
	 *  @brief Ask if ads are loaded and if there is at least one modal ad available
	 *
	 *  @note If ads aren't downloaded yet, then the method will return `AFAdSDKAdAvailabilityPending`.
	 *  To test the library, and then have always have a positive response, please use the "debug" mode (see online documentation for more precisions).
	 *
	 *  @param modalType The kind of modal you want to check.
	 *  Note that most of ads should be available for both formats.
	 *
	 *  @return `AFAdSDKAdAvailabilityPending` if ads aren't loaded yet, `AFAdSDKAdAvailabilityYes` and if there is at least one modal ad available, `AFAdSDKAdAvailabilityNo` otherwise.
	 */
	public static AFAdSDKAdAvailability IsThereAModalAdAvailable(AFAdSDKModalType modalType)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdkad_isThereAModalAdAvailable(modalType);
		return AFAdSDKAdAvailability.AFAdSDKAdAvailabilityNo;
	}
	
	/*!
	 *  @brief Cancel any pending ad modal request you have made in the past.
	 *
	 *  @return `YES` if a modal ad was canceled, `NO` otherwise.
	 *  If `YES` is returned, you'll get an delegate event via 'modalAdRequestDidFailWithError:'.
	 */
	public static bool CancelPendingAdModalRequest()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdkad_cancelPendingAdModalRequest();
		return false;
	}
	
	/*!
	 *  @brief Check if there is any modal ad being displayed right now by the library.
	 *
	 *  @return `YES` if a modal ad is being displayed, `NO` otherwise
	 */
	public static bool IsModalAdDisplayed()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdkad_isModalAdDisplayed();
		return false;
	}
		
	/*!
	 *  @brief Green light so the library can prepare itself.
	 *
	 *  @note If not already done, this method is automatically called at during a modal ad request.
	 */
	public static void Prepare()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdkad_prepare();
	}
	
	/*!
	 *  @brief Ask if AdUnit is initialized
	 *
	 *  @note There are various checks like waiting for Appboster SDK initialization, internet connection ...
	 *  Usually the library is quickly initialized ( < 1s ).
	 *
	 *  @return `YES` if the library is initialized, `NO` if the library isn't yet.
	 */
	public static bool IsInitialized()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdkad_isInitialized();
		return false;
	}
	
}

#endif