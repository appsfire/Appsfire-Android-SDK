package appsfiredemo
{
	import com.appsfire.AppsfireANE.Appsfire;
	import com.appsfire.AppsfireANE.AppsfireEvent;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.display.Stage;
	import flash.display.MovieClip;
	import flash.desktop.NativeApplication;

	import ie.jampot.nativeExtensions.Alert;
	import ie.jampot.nativeExtensions.AlertEvent;
	
	public class MyClass
	{
		static private var appsfire:Appsfire;
		static private var eventDispatcher:EventDispatcher;
		
		static public function initSDK():void {
			// Initialize Appsfire SDK
			appsfire = Appsfire.getInstance();
			eventDispatcher = new EventDispatcher ();
			eventDispatcher.addEventListener(AppsfireEvent.AFADSDK_MODAL_AD_IS_READY_FOR_REQUEST, onModalAdAvailable);
			appsfire.afsdk_setEventDispatcher(eventDispatcher);
			appsfire.afsdk_connectWithAPIKey("YOUR_API_KEY_HERE");
			appsfire.afadsdk_setDebugModeEnabled(true);
			appsfire.afadsdk_prepare();

			// Register event handlers for pausing and resuming the app
			NativeApplication.nativeApplication.addEventListener(Event.DEACTIVATE, pauseEventHandler);
			NativeApplication.nativeApplication.addEventListener(Event.ACTIVATE, resumeEventHandler);			
		}
		
		static public function buttonEventHandler(e:Event):void {
			try {				
				if (appsfire.afadsdk_isThereAModalAdAvailable("AFAdSDKModalTypeSushi")) {
					// Show interstitial
					appsfire.afadsdk_requestModalAd("AFAdSDKModalTypeSushi");
				}
				else {
					// Show toast to say the interstitial is still loading
					var aWait : Alert = new Alert();
					aWait.showAlert("The interstitial is still loading");
				}
			} catch (e:Error) {
				// Show toast with error
				var aErr : Alert = new Alert();
				aErr.showAlert("Error: " + e.toString());				
			}
		}
		
		static public function onModalAdAvailable(e:Event):void  { 
			appsfire.afadsdk_logMessage("an interstitial is available for display");
		}
		
		static public function pauseEventHandler(e:Event):void {
			appsfire.afadsdk_logMessage("pause");
			if (appsfire)
				appsfire.afsdk_pause();
		}
		
		static public function resumeEventHandler(e:Event):void {			
			appsfire.afadsdk_logMessage("resume");
			if (appsfire)
				appsfire.afsdk_resume();
		}
	}
}
