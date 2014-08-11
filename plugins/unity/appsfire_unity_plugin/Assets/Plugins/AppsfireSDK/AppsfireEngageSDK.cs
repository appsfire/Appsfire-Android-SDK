#if UNITY_IPHONE

using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;

public struct AF_RGBA
{
    public double red, green, blue, alpha;
	public AF_RGBA(double red, double green, double blue, double alpha)
	{
		this.red = red; this.green = green; this.blue = blue; this.alpha = alpha;
	}
}

public enum AFSDKPanelContent
{
    AFSDKPanelContentDefault = 0,
    AFSDKPanelContentFeedbackOnly
}

public enum AFSDKPanelStyle
{
	AFSDKPanelStyleDefault = 0,
	AFSDKPanelStyleFullscreen
}

public class AppsfireEngageSDK : MonoBehaviour {
	
	/* Interface to native implementation */
	
	[DllImport ("__Internal")]
	private static extern void afsdk_pause();
	
	[DllImport ("__Internal")]
	private static extern void afsdk_resume();
	
	[DllImport ("__Internal")]
	private static extern void afsdk_handleBadgeCountLocally(bool handleLocally);

	[DllImport ("__Internal")]
	private static extern void afsdk_handleBadgeCountLocallyAndRemotely(bool handleLocallyAndRemotely);

	[DllImport ("__Internal")]
	private static extern bool afsdk_presentPanelForContentAndStyle(AFSDKPanelContent content, AFSDKPanelStyle style);

	[DllImport ("__Internal")]
	private static extern void afsdk_dismissPanel();
	
	[DllImport ("__Internal")]
	private static extern bool afsdk_isDisplayed();

	[DllImport ("__Internal")]
	private static extern bool afsdk_openSDKNotificationID(int notificationID);

	[DllImport ("__Internal")]
	private static extern void afsdk_setBackgroundAndTextColor(AF_RGBA backgroundColor, AF_RGBA textColor);
	
	[DllImport ("__Internal")]
	private static extern void afsdk_setCustomKeysValues(string attributes);
	
	[DllImport ("__Internal")]
	private static extern bool afsdk_setUserEmailAndModifiable(string email, bool modifiable);

	[DllImport ("__Internal")]
	private static extern void afsdk_showFeedbackButton(bool showButton);

	[DllImport ("__Internal")]
	private static extern int afsdk_numberOfPendingNotifications();
	
	/*!
	 *  @brief Handle the badge count for this app locally (only on the device and only while the app is alive).
	 *  @since 1.0.3
	 *
	 *  @note Note that <handleBadgeCountLocally> overrides any settings established by <handleBadgeCountLocallyAndRemotely>, and vice versa.
	 *
	 *  @param handleLocally A boolean to determine if the badge count should be handled locally.
	 */
	public static void HandleBadgeCountLocally(bool handleLocally)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_handleBadgeCountLocally(handleLocally);
	}

	/*!
	 *  @brief Handle the badge count for this app remotely (Appsfire SDK will update the icon at all times, locally and remotely, even when app is closed).
	 *  @since 1.0.3
	 *
	 *  @note Note that <handleBadgeCountLocallyAndRemotely> overrides any settings established by <handleBadgeCountLocally>.
	 *  @note IMPORTANT: If you set this option to YES, you need to provide us with your Push Certificate.
	 *  For more information, visit your "Manage Apps" section on http://dashboard.appsfire.com/app/manage
	 *
	 *  @param handleLocallyAndRemotely Boolean to determine if badge count should be handled locally and remotely.
	 */
	public static void HandleBadgeCountLocallyAndRemotely(bool handleLocallyAndRemotely)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_handleBadgeCountLocallyAndRemotely(handleLocallyAndRemotely);
	}
	
	/*!
	 *  @brief Present the panel for notifications / feedback in a specific style
	 *  @since 2.0
	 *
	 *  @note Use this method for an easy way to present the Notification Wall. It'll use the window to display, and handle itself so you don't have anything to do except for calling the presentation method.
	 *
	 *  @param content The default parameter (AFSDKPanelContentDefault) displays the Notification Wall. But if you choose to only display the feedback form (AFSDKPanelContentFeedbackOnly), the Notification Wall will be hidden.
	 *  @param style The panel can displayed in a modal fashion over your application (AFSDKPanelStyleDefault) or in full screen (AFSDKPanelStyleFullscreen).
	 *
	 *  @return Returns false if a problem occures when trying to present the panel.
	 */
	public static bool PresentPanelForContentAndStyle(AFSDKPanelContent content, AFSDKPanelStyle style)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdk_presentPanelForContentAndStyle(content, style);
		return false;
	}
		
	/*!
	 *  @brief Closes the Notification Wall and/or Feedback Form
	 *
	 *  @note In the case you are handling the panel with a controller, you are responsible for dismissing it yourself.
	 */
	public static void DismissPanel()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_dismissPanel();
	}

	/*!
	 *  @brief Tells you if the SDK is displayed.
	 *
	 *  @return `YES` if notifications panel or feedback screen is displayed, `NO` if none.
	 */
	public static bool IsDisplayed()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdk_isDisplayed();
		return false;
	}
	
	/*!
	 *  @brief Opens the SDK to a specific notification ID.
	 *  @since 1.1.4
	 *
	 *  @note Calls "SDKopenNotificationResult:" on delegate set by "setApplicationDelegate" if it exists.
	 *
	 *  @param notificationID The notification ID you would like to open. Generally this ID is sent via a push to your app.
	 */
	public static void OpenSDKNotificationID(int notificationID)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_openSDKNotificationID(notificationID);
	}
	
	/*!
	 *  @brief You can customize a bit the colors used for the user interface. It'll mainly affect the header and the footer of the panel that you present.
	 *  @since 2.0
	 *
	 *  @note You must specify both the background and text colors.
	 *
	 *  @param backgroundColor The color used for the background.
	 *  @param textColor The color used for the text (over the specific background color).
	 */
	public static void SetBackgroundAndTextColor(AF_RGBA backgroundColor, AF_RGBA textColor)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_setBackgroundAndTextColor(backgroundColor, textColor);
	}
	
	/*!
	 *  @brief Send data to SDK in key/value pairs. Strings matching any of your [KEYS] will be replaced by the respective value you send.
	 *  @since 2.0
	 *
	 *  @param customValues A dictionary containing the keys/values to replace. (See documentation for example)
	 */
	public static void SetCustomKeysValues(Dictionary<string, string> attributes)
	{
		if (Application.platform != RuntimePlatform.IPhonePlayer)
			return;
	    string attributesString = "";
	    foreach(KeyValuePair<string, string> kvp in attributes) {
	        attributesString += kvp.Key + "=/=" + kvp.Value + "\n";
	    }
		afsdk_setCustomKeysValues(attributesString);	
	}
	
	/*!
	 *  @brief Set user email.
	 *  @since 1.1.0
	 *
	 *  @note If you know your user's email, call this function so that we avoid asking the user to enter his or her email when sending feedback.
	 *
	 *  @param email The user's email
	 *  @param modifiable If `modifiable` is set to FALSE, the user won't be able to modify his/her email in the Feedback form.
	 *
	 *  @return `YES` if no error was detected, `NO` if a problem occured (likely because email was invalid).
	 */
	public static bool SetUserEmailAndModifiable(string email, bool modifiable)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdk_setUserEmailAndModifiable(email, modifiable);
		return false;
	}
	
	/*!
	 *  @brief Allow you to display or hide feedback button.
	 *  @since 1.1.5
	 *
	 *  @param showButton The boolean to tell if feedback button should be displayed or not. Default value is `YES`.
	 */
	public static void ShowFeedbackButton(bool showButton)
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_showFeedbackButton(showButton);
	}
	
	/*!
	 *  @brief Returns the number of unread notifications that require attention.
	 *
	 *  @return Return an integer that represent the number of unread notifications.
	 *  If SDK isn't initialized, this number will be 0.
	 */
	public static int NumberOfPendingNotifications()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			return afsdk_numberOfPendingNotifications();
		return 0;
	}
	
	/*!
	 *  @brief Pause any refresh timer.
	 */
	[System.Obsolete]
	public static void Pause()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_pause();
	}
		
	/*!
	 *  @brief Resume any refresh timer.
	 */
	[System.Obsolete]
	public static void Resume()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
			afsdk_resume();
	}
	
}

#endif
