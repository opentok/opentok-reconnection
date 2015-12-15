Android Automatic Reconnection Sample
=====================================

This sample shows how to use the OpenTok Android SDK automatic reconnection beta feature.

*Important* To use this feature, you must contact TokBox to participate in the beta program.
See the [OpenTok beta programs](https://tokbox.com/platform/beta-programs) page.

Clients connected to sessions that use the automatic reconnection feature can do the following:

* Attempt to reconnect to the session if the client has disconnected due to a temporary drop in
  network connectivity.

* Attempt to reconnect to a stream it is subscribing to that is temporarily dropped.

* Determine whether signals sent while attempting to reconnect to a session are sent upon
  reconnection (or not). (For more information, see the
  [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/).

## Testing the app

To configure and test the app:

1. Import the project into Android Studio.

   To import the project directly into Android Studio:

   * In Android Studio, choose File > New > Import Project and choose the settings.gradle file in
     the OpenTokSamples directory. Or, if you are viewing the Android Studio welcome screen,
     click "Import Project".

   * Copy the opentok-android-sdk-2.7.0.jar file into the libs directory of the app module.
     This file is included in the OpenTok/libs subdirectory of the OpenTok Android SDK, available at
     <http://tokbox.com/opentok/libraries/client/android/>.

   * Copy the directories containing native dependencies required for your target environments:

     * armeabi
     * armeabi-v7a
     * x86

     You will need the armeabi directory, the armeabi-v7a directory, or both for support on
     ARM-based devices. You will need the x86 directory for support on x86-based devices or in
     the supported Android emulators. These directories are included in the OpenTok/libs
     subdirectory of the OpenTok Android SDK.

2. Configure the project to use your own OpenTok session and token:

   Open the MainActivity.java file (in the com.opentok.reconnection.sample package) and set
   the `SESSION_ID`, `TOKEN`, and `APIKEY` strings to your own session ID, token, and API key
   respectively.

   For more information, see the OpenTok [Session Creation
   Overview](https://tokbox.com/opentok/tutorials/create-session/) and the [Token Creation
   Overview](https://tokbox.com/opentok/tutorials/create-token/).

3.  Connect your Android device to a USB port on your computer. Set up
   [USB debugging](http://developer.android.com/tools/device.html) on your device.
   Or launch the Genymotion x86 emulator or the official Android x86 emulator in combination
   with the Intel HAXM software.

4.  Run the app on your device, selecting the default activity as the launch action.

5. Disable the internet connection (both the Wi-Fi connection and any other network connection)
   on your debug device.

   The app displays an Android ProgressDialog with the message "Reconnecting. Please wait...."
   It also logs "Reconnecting the session" to the debug console.

6. Reconnect your device to the network. Upon reconnecting to the OpenTok session, the app
   app hides the reconnection ProgressDialog and logs "Session has been reconnected" to the debug
   console.

Now we will add a second client connected to the session:

1. Install and run the app on another Android device. Or run one instance in a supported Android
   emulator and run another on a device. Mute the speakers so that you will not receive audio
   feedback.

2. Disconnect the internet connection on the device that is _not_ being debugged.

   The app logs a "Subscriber has been disconnected by connection error" to the debug console.

5. Reconnect the device to the network. When the subscribing client reconnects to the stream, it
   displays a Toast message "Subscriber has been reconnected," and it logs the message to the
   debug console.

Finally, the app shows how to disable
[OpenTok signaling](https://tokbox.com/developer/guides/signaling/android/) while reconnecting
to a session:

1. The app sends a signal when the battery usage on the device changes. However, if the signal is
   initiated while the app is disconnected from the session, it will not be sent. (You can also
   have an app send signals initiated while reconnecting; the signals are sent when it reconnects.)

## Understanding the code

Locate the MainActivity.java file (in the com.opentok.reconnection.sample package. The MainActivity
class connects to the OpenTok session, and adds a SessionListener object to act as the
SessionListener and Session.ReconnectionListener for the Session object:

```java
  mSessionListener = new SessionListener();
  mSession.setSessionListener(mSessionListener);
  mSession.setReconnectionListener(mSessionListener);
```

The SessionListener class (defined in the MainActivity.java file), implements the
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

The SubscriberListener class (defined in the MainActivity.java file), implements the
`Subscriber.StreamListener.onReconnecting(Session session)` and
`Subscriber.StreamListener.onReconnected(Session session)` methods of the
Session.SessionReconnectionListener interface (defined in the OpenTok Android SDK):

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
