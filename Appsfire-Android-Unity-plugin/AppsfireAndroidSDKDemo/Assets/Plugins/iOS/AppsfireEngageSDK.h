/*!
 *  @header    AppsfireEngageSDK.h
 *  @abstract  Appsfire Engage SDK Header
 *  @version   2.4.0
 */

#import <UIKit/UIViewController.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSError.h>
#import "AppsfireSDKConstants.h"

@protocol AppsfireEngageSDKDelegate;

/*!
 *  Engage SDK top-level class.
 */
@interface AppsfireEngageSDK : NSObject

/** @name Library Life
 *  Methods about the general life of the library.
 */

/*!
 *  @brief Sets delegate when Appsfire Engage SDK calls one.
 *  @since 1.1.4
 *
 *  @note Most of the time Appsfire Engage SDK will alert you for on basic events.
 *  Please refer to the documentation to take note of available methods.
 *
 *  @param delegate A pointer on the object that will receive the calls.
 */
+ (void)setDelegate:(id<AppsfireEngageSDKDelegate>)delegate;


/** @name Push Settings
 *  Methods to manage push settings
 */

/*!
 *  @brief Register the push token for APNS (Apple Push Notification Service).
 *
 *  @note You should call this method in the "application:didRegisterForRemoteNotificationsWithDeviceToken:" method in your application delegate.
 *
 *  @param deviceToken The push token as an NSData object. We'll take care of its NSString representation.
 */
+ (void)registerPushToken:(NSData *)deviceToken;

/*!
 *  @brief Handle the badge count for this app locally (only on the device and only while the app is alive).
 *  @since 1.0.3
 *
 *  @note Note that `handleBadgeCountLocally` overrides any settings established by `handleBadgeCountLocallyAndRemotely`, and vice versa.
 *
 *  @param handleLocally A boolean to determine if the badge count should be handled locally.
 */
+ (void)handleBadgeCountLocally:(BOOL)handleLocally;

/*!
 *  @brief Handle the badge count for this app remotely (Appsfire Engage SDK will update the icon at all times, locally and remotely, even when app is closed).
 *  @since 1.0.3
 *
 *  @note Note that `handleBadgeCountLocallyAndRemotely` overrides any settings established by `handleBadgeCountLocally`.
 *  @note IMPORTANT: If you set this option to YES, you need to provide us with your Push Certificate.
 *  For more information, visit your "Manage Apps" section on http://dashboard.appsfire.com/app/manage
 *
 *  @param handleLocallyAndRemotely Boolean to determine if badge count should be handled locally and remotely.
 */
+ (void)handleBadgeCountLocallyAndRemotely:(BOOL)handleLocallyAndRemotely;


/** @name Present & Close
 *  Methods to present / dismiss notifications and feedback
 */

/*!
 *  @brief Present the panel for notifications / feedback in a specific style
 *  @since 2.0
 *
 *  @note Use this method for an easy way to present the Notification Wall. It'll use the window to display, and handle itself so you don't have anything to do except for calling the presentation method.
 *
 *  @param content The default parameter (AFSDKPanelContentDefault) displays the Notification Wall. But if you choose to only display the feedback form (AFSDKPanelContentFeedbackOnly), the Notification Wall will be hidden.
 *  @param style The panel can displayed in a modal fashion over your application (AFSDKPanelStyleDefault) or in full screen (AFSDKPanelStyleFullscreen).
 *
 *  @return If a problem occures when trying to present the panel, an error will be returned with the appropriate information.
 */
+ (NSError *)presentPanelForContent:(AFSDKPanelContent)content withStyle:(AFSDKPanelStyle)style;

/*!
 *  @brief Closes the Notification Wall and/or Feedback Form
 *
 *  @note In the case you are handling the panel with a controller, you are responsible for dismissing it yourself.
 */
+ (void)dismissPanel;

/*!
 *  @brief Get a view controller that contains the notifications view.
 *  @since 2.0
 *
 *  @note Use this method if you prefer to wrap the notifications view in a tab bar or push it in a navigation controller.
 *  @note IMPORTANT: You are responsible for presenting and dismissing the controller. Any leak of this controller could lead to a dysfunction of the sdk.
 *
 *  @param error If a problem occured when trying to get the controller, error will be filled with some information.
 *
 *  @return Return the view controller that you can present and dismiss. If nil, then you probably need to check the error pointer.
 */
+ (UIViewController *)getPanelViewControllerWithError:(NSError **)error;

/*!
 *  @brief Tells you if the SDK is displayed.
 *
 *  @return `YES` if notifications panel or feedback screen is displayed, `NO` if none.
 */
+ (BOOL)isDisplayed;

/*!
 *  @brief Opens the SDK to a specific notification ID.
 *  @since 1.1.4
 *
 *  @note Calls "SDKopenNotificationResult:" on delegate set by "setDelegate" if it exists.
 *
 *  @param notificationID The notification ID you would like to open. Generally this ID is sent via a push to your app.
 */
+ (void)openSDKNotificationID:(int)notificationID;


/** @name Options
 *  Methods about various settings (like graphics) of the library.
 */

/*!
 *  @brief You can customize a bit the colors used for the user interface. It'll mainly affect the header and the footer of the panel that you present.
 *  @since 2.0
 *
 *  @note You must specify both the background and text colors.
 *
 *  @param backgroundColor The color used for the background.
 *  @param textColor The color used for the text (over the specific background color).
 */
+ (void)setBackgroundColor:(UIColor *)backgroundColor textColor:(UIColor *)textColor;

/*!
 *  @brief Send data to SDK in key/value pairs. Strings matching any of your [KEYS] will be replaced by the respective value you send.
 *  @since 2.0
 *
 *  @param keyValues A dictionary containing the keys/values to replace. (See documentation for example)
 */
+ (void)setCustomKeysValues:(NSDictionary *)keyValues;

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
+ (BOOL)setUserEmail:(NSString *)email isModifiable:(BOOL)modifiable;

/*!
 *  @brief Allow you to display or hide feedback button.
 *  @since 1.1.5
 *
 *  @param show The boolean to tell if feedback button should be displayed or not. Default value is `YES`.
 */
+ (void)setShowFeedbackButton:(BOOL)show;


/** @name Getters
 *  Methods to get various information from the library
 */

/*!
 *  @brief Returns the number of unread notifications that require attention.
 *
 *  @return Return an integer that represent the number of unread notifications.
 *  If SDK isn't initialized, this number will be 0.
 */
+ (NSUInteger)numberOfPendingNotifications;


/** @name Deprecated Methods
 *  Methods which are about to be removed from the SDK.
 */

+ (NSString *)getSessionID __deprecated_msg("We plan to remove this method in a future release. If you still find an interest in using it, please contact us!");

+ (void)pause __deprecated_msg("We plan to remove this method in a future release. If you still find an interest in using it, please contact us!");

+ (void)resume __deprecated_msg("We plan to remove this method in a future release. If you still find an interest in using it, please contact us!");

@end


/*!
 *  Appsfire Engage SDK protocol.
 */
@protocol AppsfireEngageSDKDelegate <NSObject>

@optional

/*!
 *  @brief For when the SDK controller has explicitly been asked to be dismissed (e.g., from an end user action)
 *  @since 2.0
 *
 *  @note If you don't take this message into account, nothing will happen on UI.
 *
 *  @param controller A pointer to the UIViewController which should be dismissed.
 */
- (void)panelViewControllerNeedsToBeDismissed:(UIViewController *)controller;

/*!
 *  @brief Async callback of the "openSDKNotificationID:" method.
 *  @since 1.1.4
 *
 *  @param succeeded Indicate if the operation succeeded or not.
 *  @param error If an error occured (i.e. if succeeded = NO), then the error explains why.
 */
- (void)openNotificationDidFinish:(BOOL)succeeded withError:(NSError *)error;

@end
