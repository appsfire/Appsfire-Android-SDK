# Adobe Air Native Extension for Appsfire SDK (Android)

This documentation is a brief introduction to the Appsfire SDK for Adobe Air. We recommend you to read the Appsfire documentation [SDK](http://docs.appsfire.com) before jumping into its Adobe Air implementation. Most of the iOS Appsfire SDK methods were implemented in the Appsfire ANE. If you have any problem during the integration, don't hesitate to [contact us](mailto:support@appsfire.com).

### Appsfire SDK
This ANE includes the Appsfire Android SDK.

### Requirements
Appsfire Android SDK supports **Android 2.3 (Gingerbread) and up**.

### Getting Started

After you have set up your app on the [Appsfire Dashboard](http://dashboard.appsfire.com), you are ready to begin integrating the Appsfire SDK into your AIR project.

First, import the Appsfire ANE in your Adobe Air project. We recommend to create a directory in your project for native extensions, and copy `Appsfire_Android.ane` and `Appsfire_Android.swc` to that directory. Then, if you are using *Flash Builder*, you can just add that directory as a native extension directory in your project settings.

Second, make sure you add the `<extensionID>` declaration to your AIR application descriptor's root `<application>` element as shown in the following example:

```xml
<extensions>
  <extensionID>com.appsfire.AppsfireANE</extensionID>
</extensions>
```

### How to use the Adobe Air Native Extension

##### Appsfire SDK Setup

First, import the two Appsfire ANE classes into your code.

```actionscript
import com.appsfire.AppsfireANE.Appsfire;
import com.appsfire.AppsfireANE.AppsfireEvent;
```

We recommend to create a variable in your class to store a reference to the global Appsfire instance.

```actionscript
private var appsfire:Appsfire;
appsfire = Appsfire.getInstance();
```

To initialize Appsfire, call the `afsdk_connectWithAPIKey` method with your API token.
This API token can be found in your [Appsfire dashboard](http://dashboard.appsfire.com).

Here is how a typical initialization of the SDK looks like in an application:

```actionscript
// Connects with API Key.
appsfire.afsdk_connectWithAPIKey("demokey");

// Enable the debug mode. Should be set to false in production environment.
appsfire.afadsdk_setDebugModeEnabled(true);

// Register for ads (in this example, get an event when modal ads are available)
eventDispatcher = new EventDispatcher ();
eventDispatcher.addEventListener(AppsfireEvent.AFADSDK_MODAL_AD_IS_READY_FOR_REQUEST, onModalAdAvailable);
appsfire.afsdk_setEventDispatcher(eventDispatcher);

// Tells the SDK to start getting ads.
appsfire.afadsdk_prepare();

```

Here is a typical event handler:

```actionscript
// A modal ad is available
static public function onModalAdAvailable(e:Event):void  {
	appsfire.afadsdk_logMessage("an interstitial is available for display");
}
```

And finally, how to display an ad interstitial (Sushi):

```actionscript
try {
	if (appsfire.afadsdk_isThereAModalAdAvailable("AFAdSDKModalTypeSushi")) {
		// Show interstitial
		appsfire.afadsdk_requestModalAd("AFAdSDKModalTypeSushi");
	}
	else {
    // No ad is available yet
	}
```

##### Appsfire methods
**Note**: calling these methods on Android will do nothing.

All the methods listed below are extracted from the `Appsfire.as` class.

```objc
/*
 *  @brief Set up the Appsfire SDK with your API key.
 *
 *  @param key Your API key can be found on http://dashboard.appsfire.com
 *
 *  @return `true` if no error was detected, `false` if a problem occurred (likely due to the API key).
 */
public function afsdk_connectWithAPIKey(apiKey : String) : Boolean;

/*
 *  @brief Tells you if the SDK is initialized.
 *
 *  @note Once the SDK is initialized, you can present the notifications or the feedback.
 *
 *  @return `true` if the sdk is initialized, `false` if not.
 */
public function afsdk_isInitialized() : Boolean;

/*
 *  @brief Pause any refresh timer.
 */
public function afsdk_pause() : void;

/*
 *  @brief Resume any refresh timer.
 */
public function afsdk_resume() : void;

/*
 *  @brief Set the event dispatcher for receiving sdk events
 *
 *  @param eventDispatcher event dispatcher.
 */
public function afsdk_setEventDispatcher(eventDispatcher:EventDispatcher):void;

/*
 *  @brief Green light so the library can prepare itself.
 *
 *  @note If not already done, this method is automatically called at during a modal ad request.
 */
public function afadsdk_prepare() : void;

/*
 *  @brief Ask if Ad SDK is initialized
 *
 *  @note There are various checks like waiting for the Appsfire SDK initialization, internet connection ...
 *  Usually the library is quickly initialized ( < 1s ).
 *
 *  @return `true` if the library is initialized, `false` if the library isn't yet.
 */
public function afadsdk_isInitialized() : Boolean;

/*
 *  @brief Ask if ads are loaded from the web service
 *
 *  @note This doesn't necessarily means that an ad is available.
 *  But it's always good to know if you want to debug the implementation and check that the web service responded correctly.
 *
 *  @return `true` if ads are loaded from the web service.
 */
public function afadsdk_areAdsLoaded() : Boolean;

/*
 *  @brief Specify if the library should be used in debug mode.
 *
 *  @note Whenever this mode is enabled, the web service will return a fake ad.
 *  By default, this mode is disabled. You must decide if you want to enable the debug mode before any prepare/request.
 *
 *  @param use A boolean to specify if the debug mode should be enabled.
 */
public function afadsdk_setDebugModeEnabled(isDebugEnabled : Boolean = true) : void;

/*
 *  @brief Request a modal ad.
 *
 *  @note If the library isn't initialized, or if the ads aren't loaded yet, then the request will be added to a queue and treated as soon as possible.
 *  You cannot request two ad modals at the same time. In the case where you already have a modal request in the queue, the previous one will be canceled.
 *
 *  @param modalType The kind of modal you want to request (AFAdSDKModalTypeSushi or AFAdSDKModalTypeUraMaki).
 *  @param timerCount the number of seconds used for the timer. if `0` no timer is used.
 */
public function afadsdk_requestModalAd(modalType : String, timerCount : Number = 0) : void;

/*
 *  @brief Ask if ads are loaded and if there is at least one modal ad available.
 *
 *  @note If ads aren't downloaded yet, then the method will return `false`.
 *  To test the library, and then have always have a positive response, please use the "debug" mode (see online documentation for more precisions).
 *
 *  @param modalType The kind of modal you want to check (AFAdSDKModalTypeSushi or AFAdSDKModalTypeUraMaki).
 *  Note that most of ads should be available for both formats.
 *
 *  @return `true` if ads are available, `false` otherwise.
 */
public function afadsdk_isThereAModalAdAvailable(modalType : String) : Boolean;

/*
 *  @brief Check if there is any modal ad being displayed right now by the library.
 *
 *  @return `true` if a modal ad is being displayed, `false` otherwise
 */
public function afadsdk_isModalAdDisplayed() : Boolean;
```

##### Listening to Appsfire Events

Appsfire fires a number of events upon activation of the delegate methods `afsdk_setUseDelegate` and `afadsdk_setUseDelegate`.

Here is an example of how you would listen to them:
```actionscript
appsfire.addEventListener(AppsfireEvent.AFADSDK_MODAL_AD_DID_APPEAR, onModalAdDidAppear);

function onModalAdDidAppear( event:AppsfireEvent ):void {
  trace("Modal Ad Did Appear!");
}
```

Here is the complete list of you can listen to:

```actionscript
// Fired when the Ad Unit did initialize.
public static const AFADSDK_ADUNIT_DID_INITIALIZE : String;

// Fired when the SDK is ready to show modal ads.
public static const AFADSDK_MODAL_AD_IS_READY_FOR_REQUEST : String;

// Fired when an the SDK couldn't show a modal ad.
public static const AFADSDK_MODAL_AD_REQUEST_DID_FAIL_WITH_ERROR : String;

// Fired when a modal ad is about to be displayed.
public static const AFADSDK_MODAL_AD_WILL_APPEAR : String;

// Fired when a modal ad is displayed on the screen.
public static const AFADSDK_MODAL_AD_DID_APPEAR : String;

// Fired when a modal ad is about to be dismissed
public static const AFADSDK_MODAL_AD_WILL_DISAPPEAR : String;

// Fired when a modal as is dismissed.
public static const AFADSDK_MODAL_AD_DID_DISAPPEAR : String;
```
