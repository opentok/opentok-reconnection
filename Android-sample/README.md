Android Automatic Reconnection Sample Code
==========================================

This sample code shows you how to use the OpenTok Automatic Reconnection feature on Android.

**Important:** To use this feature, you must contact TokBox to participate in this developer preview.
See the main project [README](../README.md) on how to enroll.

Clients connected to sessions that use the Automatic Reconnection feature can do the following:

* Attempt to automatically reconnect to the session if the client has disconnected due
  to a temporary drop in network connectivity.

* Attempt to automatically reconnect to a subscriber stream that is temporarily dropped.

* Upon a successful reconnection, automatically resend signals that are initiated
  by the client when it was temporarily disconnected. (For more information about Signaling,
  see the [Signaling developer guide](https://tokbox.com/developer/guides/signaling/android/).)

## Trying the sample code

To configure and try the sample code:

1. Import the project into Android Studio.

   * In Android Studio, choose *File > New > Import Project* and choose the `settings.gradle` file in
     the `Android-sample` directory. Or, if you are viewing the Android Studio welcome screen,
     click "Import Project".

   * For the purposes of this developer preview, you will need to be enrolled in the Automatic 
     Reconnection developer preview. See the main project [README](../README.md) for details.

2. Configure the project to use your own OpenTok session and token:

   Open the `MainActivity.java` file (in the *com.opentok.reconnection.sample* package) and set
   the `SESSION_ID`, `TOKEN`, and `APIKEY` strings to your own session ID, token, and API key
   respectively.

   Use the API key provided to you when you enrolled in the Automatic Reconnections developer preview.
   With the API key, you will use one of our server-side SDKs to generate a session ID and token.
   To learn how, please review the main project [README](../README.md).

3. Connect your Android device to a USB port on your computer. Set up
   [USB debugging](http://developer.android.com/tools/device.html) on your device.
   Or launch the Genymotion x86 emulator or the official Android x86 emulator in combination
   with the Intel HAXM software.

4. Run the app on your device, selecting the default activity as the launch action.

5. Disable the internet connection (both the Wi-Fi connection and any other mobile data connection)
   on your device.

   The app displays an Android [ProgressDialog](http://developer.android.com/reference/android/app/ProgressDialog.html)
   with the message "Reconnecting. Please wait....". It also logs "Reconnecting to the session" 
   to the debug console.

6. Enable internet access for your device, this time preferably on your mobile data network. 
   Upon reconnecting to the OpenTok session, the app hides the ProgressDialog and logs 
   "Session has been reconnected" to the debug console.

Now we will add a second client to the session:

1. Install and run the app on another Android device. Or run one instance in a supported Android
   emulator and run another on a device. Mute the speakers so that you will not receive audio
   feedback.

2. Disconnect the internet connection on the second device/client while keeping the original 
   first device connected to the debug console. From the first client, you will now notice that
   the message "Subscriber has been disconnected by connection error" has been logged to the 
   debug console.

5. Reconnect the second device/client to the network. When the subscriber on the first client 
   successfully reconnects to the stream, a Toast message "Subscriber has been reconnected"
   will be displayed and the same message is logged to the ebug console.

Finally, the sample code shows how to disable
[OpenTok signaling](https://tokbox.com/developer/guides/signaling/android/) while reconnecting
to a session:

1. The app sends a signal when the battery usage on the device changes. However, if the signal is
   initiated while the app is disconnected from the session, it will not be sent. (You can also
   have an app re-send signals that were initiated while reconnecting)

## Understanding the code

Locate the `MainActivity.java` file (in the *com.opentok.reconnection.sample* package). 
The MainActivity class connects to the OpenTok session, and adds a SessionListener 
object to act as the
SessionListener and Session.ReconnectionListener for the Session object:

```java
  mSessionListener = new SessionListener();
  mSession.setSessionListener(mSessionListener);
  mSession.setReconnectionListener(mSessionListener);
```

The SessionListener class (defined in the `MainActivity.java` file), implements the
`Session.SessionReconnectionListener.onReconnecting(Session session)` and
`Session.SessionReconnectionListener.onReconnected(Session session)` methods of the
Session.SessionReconnectionListener interface (defined in the OpenTok Android SDK):

```java
@Override
public void onReconnecting(Session session) {
    Log.i(LOGTAG, "Reconnecting the session "+session.getSessionId());
    showReconnectionDialog(true);
}

@Override
public void onReconnected(Session session) {
    Log.i(LOGTAG, "Session has been reconnected");
    showReconnectionDialog(false);
}
```

These methods are called when the client is attempting to reconnect to the session and when it
successfully reconnects. The code displays and hides the session reconnection dialog box when
these methods are called.

When the `SessionListener.onStreamReceived(Session session, Stream stream)` method is called
(indicating that another client's stream is available in the session), the client subscribes to
that stream. It also calls the `setStreamListener(SubscriberListener listener)` method of the
Subscriber object:

```java
mSubscriberListener =  new SubscriberListener();
mSubscriber = new Subscriber(MainActivity.this, stream);
mSubscriber.setVideoListener(mSubscriberListener);
mSubscriber.setStreamListener(mSubscriberListener);
```

The SubscriberListener class (defined in the `MainActivity.java` file), implements the
`SubscriberKit.StreamListener.onReconnected(SubscriberKit subscriberKit)` and
`SubscriberKit.StreamListener.onDisconnected(SubscriberKit subscriberKit)` methods of the 
SubscriberKit.StreamListener interface (defined in the OpenTok Android SDK):

```java
@Override
public void onReconnected(SubscriberKit subscriberKit) {
  Log.i(LOGTAG, "Subscriber has been reconnected");
  showSubscriberReconnectionDialog(false);
}

@Override
public void onDisconnected(SubscriberKit subscriberKit) {
  Log.i(LOGTAG, "Subscriber has been disconnected by connection error");
  showSubscriberReconnectionDialog(true);
}
```

These methods are called when the subscriber's stream is dropped ("disconnected") and when it is
restored ("connected").

Finally, note that the `Session.sendSignal(String type, String data, boolean retryAfterReconnect)`
method (defined in the OpenTok Android SDK) includes a `retryAfterReconnect` parameter. When set
to `false`, signals that are initiated when the client is attempting to reconnect are _not_ sent
upon reconnection. This is illustrated in the code that sends a signal when the battery level
changes:

```java
private BroadcastReceiver mBatteryTracker = new BroadcastReceiver() {

    public void onReceive(Context context, Intent batteryStatus) {

        String action = batteryStatus.getAction();

        // information received from BatteryManager
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryUsePct = level / (float)scale;

            if (mSession != null ) {
                mSession.sendSignal("signal", String.valueOf(batteryUsePct), false);
            }
        }
    }
};
```

Because the `retryAfterReconnect` parameter is set to `false`, signals that are initiated when
the client is attempting to reconnect are not sent upon reconnection.
