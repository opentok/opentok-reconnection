iOS Automatic Reconnection Sample Code
======================================

This sample code shows you how to use the OpenTok Automatic Reconnection feature on iOS.

Clients connected to sessions that use the Automatic Reconnection feature can do the following:

* Attempt to automatically reconnect to the session if the client has disconnected due 
  to a temporary drop in network connectivity.

* Attempt to automatically reconnect to a subscriber stream that is temporarily dropped.

* Upon a successful reconnection, automatically resend signals that are initiated
  by the client when it was temporarily disconnected. (For more information about Signaling,
  see the [Signaling developer guide](https://tokbox.com/developer/guides/signaling/android/).)

**Important:** To use this feature, you must contact TokBox to enroll in the Automatic Reconnection beta.
See the main project [README](../README.md) on how to enroll.

## Trying the sample code

Install the required CocoaPod dependencies and add some settings for the app:

1. Open the iOS-sample directory in the command line, and enter the following:

  ```
  pod install
  ```

2. Open the `Reconnection sample.xcodeproj` file in Xcode.

3. In the `VideoController.m file`, set values for the `kApiKey`, `kSessionId`,
   and `kToken` constants to your own session ID, token, and API key respectively.

   Your API key must be enrolled in the Automatic Reconnection beta in order to use this
   feature. To enroll an API key for this beta, please review the main project [README](../README.md)
   for details.

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
to a session. The app sends a signal when the battery usage on the device changes. 
However, if the signal is initiated while the app is disconnected from the session, 
it will not be sent. You can also have an app send signals while it is still in the 
process of reconnecting; the signals are sent when the app successfully reconnects 
to the session.

## Understanding the code

Open the `ViewController.m` file. When the main view loads, the ViewController object connects to
the OpenTok session.

The ViewController class implements the OTSessionDelegate protocol defined in the OpenTok iOS SDK.
This protocol includes methods that are called when the client is attempting to reconnect to the
session and when it successfully reconnects. In the methods below, the code displays and hides an 
alert with the message "Session is reconnecting".

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

The ViewController class also implements the OTSubscriberDelegate protocol defined in the OpenTok
iOS SDK. This protocol includes methods that are called when the subscriber stream is dropped
("disconnected") and when it is restored ("reconnected").

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
method includes a `retryAfterReconnect` parameter. When set to `NO`, signals that are initiated 
when the client is attempting to reconnect are _not_ sent upon reconnection. This is illustrated 
in the code below that sends a signal when the battery level changes.

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
