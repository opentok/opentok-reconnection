iOS Automatic Reconnection Sample
=================================

This sample shows how to use the OpenTok iOS SDK automatic reconnection beta feature.

*Important* To use this feature, you must contact TokBox to participate in the beta program.
See the [OpenTok beta programs](https://tokbox.com/platform/beta-programs) page. Also, you must
compile your app using the preview version of the OpenTok iOS SDK, available from
https://mobile-meet.tokbox.com/latest?product=otkit-android-sdk&redirect=1.

Clients connected to sessions that use the automatic reconnection feature can do the following:

* Attempt to reconnect to the session if the client has disconnected due to a temporary drop in
  network connectivity.

* Attempt to reconnect to a stream it is subscribing to that is temporarily dropped.

* Determine whether signals sent while attempting to reconnect to a session are sent upon
  reconnection (or not). (For more information, see the
  [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/).)

## Testing the app

Install the required CocoaPod dependencies and add some settings for the app:

1. Open the iOS-sample directory in the command line, and enter the following:

  ```
  pod install
  ```

2. Open the Reconnection sample.xcworkspace file in Xcode.

3. In the VideoController.m file, set values for the `kApiKey`, `kSessionId`,
   and `kToken` constants. For testing, you can obtain these values at the
   [OpenTok dashboard](https://dashboard.tokbox.com/users/sign_in). In a production application,
   use one of the [OpenTok server SDKs](https://tokbox.com/developer/sdks/server/) to generate
   session IDs and tokens.

   For more information, see the OpenTok
   [Session Creation Overview](https://tokbox.com/developer/guides/create-session/) and the
   [Token Creation Overview](https://tokbox.com/developer/guides/create-token/).

4. Debug the app on your device or using the Xcode iOS Simulator. 

   The app publishes an audio-video stream to the OpenTok session.

5. Disable the internet connection (both the Wi-Fi connection and any other
   network connection) on your debug device.

   The app displays an alert with the message "Session is reconnecting."

6. Reconnect your device to the network.

   Upon reconnecting to the OpenTok session, the app removes the alert.

Now we will add a second client connected to the session:

1. Install and run the app on another iOS device. Or run one instance in the Xcode iOS Simulator
   and run another on an iOS device. Mute the speakers so that you will not receive audio
   feedback.

2. Disconnect the internet connection on the device that is _not_ being debugged.

   A reconnecting spinner icon is displayed in the debug app's subscriber.

5. Reconnect the device to the network.

   When the subscribing client reconnects to the stream, the reconnecting spinner icon is
   removed from the user interface.

Finally, the app shows how to disable
[OpenTok signaling](https://tokbox.com/developer/guides/signaling/ios/) while reconnecting
to a session:

1. The app sends a signal when the battery usage on the device changes. However, if the signal is
   initiated while the app is disconnected from the session, it will not be sent. (You can also
   have an app send signals initiated while reconnecting; the signals are sent when it reconnects.)

## Understanding the code

Open the ViewController.m file. When the main view loads, ViewController object connects to
the OpenTok session.

The ViewController class implements the OTSessionDelegate protocol (defined in the OpenTok iOS SDK).
This protocol includes methods that are called when the client is attempting to reconnect to the
session and when it successfully reconnects:

```objc
- (void)sessionDidBeginReconnecting:(OTSession *)session
{
    [self showReconnectingAlert];
}

- (void)sessionDidReconnect:(OTSession *)session
{
    [self dismissReconnectingAlert];
}
```

The code displays and hides the session reconnecting alert when these messages are sent.

The ViewController class also implements the OTSubscriberDelegate protocol (defined in the OpenTok
iOS SDK). This protocol includes methods that are called when the subscriber's stream is dropped
("disconnected") and when it is restored ("reconnected").:

```objc
- (void)subscriberDidDisconnectFromStream:(OTSubscriberKit *)subscriber
{
    [self.reconnectingSpinner startAnimating];
    [self.subscriber.view addSubview:self.streamReconnectingView];
}

- (void)subscriberDidReconnectToStream:(OTSubscriberKit *)subscriber
{
    [self.reconnectingSpinner stopAnimating];
    [self.streamReconnectingView removeFromSuperview];
}
```

Finally, note that the `[OTSession signalWithType:string:connection:retryAfterReconnect:error:]`
method (defined in the OpenTok iOS SDK) includes a `retryAfterReconnect` parameter. When set
to `NO`, signals that are initiated when the client is attempting to reconnect are _not_ sent
upon reconnection. This is illustrated in the code that sends a signal when the battery level
changes:

```objc
- (void)batteryLevelChanged:(NSNotification *)notification
{
    NSString * signalText = [NSString stringWithFormat:@"%f", [UIDevice currentDevice].batteryLevel];
    OTError * error;
    [self.session signalWithType:@"signal"
                          string:signalText
                      connection:nil
             retryAfterReconnect:NO
                           error:&error];
    
    if (error) {
        [self showErrorAlert:[error localizedDescription]];
    }
}
```

Because the `retryAfterReconnect` parameter is set to `NO`, signals that are initiated when
the client is attempting to reconnect are not sent upon reconnection.
