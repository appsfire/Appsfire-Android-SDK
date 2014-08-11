#import "AppsfireSDK.h"
#import "AppsfireEngageSDK.h"
#import "AppsfireAdSDK.h"
#import <Foundation/Foundation.h>

typedef struct AF_RGBA {
    double red;
    double green;
    double blue;
    double alpha;
} AF_RGBA;

@interface AppsfireSDKWrapper : NSObject <AppsfireAdSDKDelegate, AFAdSDKModalDelegate>

//
+ (AppsfireSDKWrapper *)sharedInstance;

//
@property (nonatomic, copy) NSString *callbackHandlerName;

@end

extern "C" {
	
	// general
	void afsdk_iniAndSetCallbackHandler(const char* handlerName);
	bool afsdk_connectWithAPIKey(const char* apikey, AFSDKFeature features);
	bool afsdk_isInitialized();
	void afsdk_resetCache();
	
	// engage
	void afsdk_pause();
	void afsdk_resume();
	void afsdk_handleBadgeCountLocally(bool handleLocally);
	void afsdk_handleBadgeCountLocallyAndRemotely(bool handleLocallyAndRemotely);
	bool afsdk_presentPanelForContentAndStyle(AFSDKPanelContent content, AFSDKPanelStyle style);
	void afsdk_dismissPanel();
	bool afsdk_isDisplayed();
	void afsdk_openSDKNotificationID(int notificationID);
	void afsdk_setBackgroundAndTextColor (struct AF_RGBA backgroundColor, struct AF_RGBA textColor);
	void afsdk_setCustomKeysValues(const char*  attributes);
	bool afsdk_setUserEmailAndModifiable(const char* email, bool modifiable);
	void afsdk_showFeedbackButton(bool showButton);
	int afsdk_numberOfPendingNotifications();
	
	// advertising
	void afsdkad_prepare();
	bool afsdkad_isInitialized();
	void afsdkad_setUseInAppDownloadWhenPossible(bool use);
	void afsdkad_setDebugModeEnabled(bool use);
	void afsdkad_requestModalAd(AFAdSDKModalType modalType);
	AFAdSDKAdAvailability afsdkad_isThereAModalAdAvailable(AFAdSDKModalType modalType);
	bool afsdkad_cancelPendingAdModalRequest();
	bool afsdkad_isModalAdDisplayed();

}
