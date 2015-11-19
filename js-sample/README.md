OpenTok.js Automatic Reconnection Sample
========================================

This sample shows how to use the OpenTok.js automatic reconnection beta feature.

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

1. Make a copy the sample-config.js file in the web/js/ directory. Name the copy config.js (and
   save it to the web/js/directory). Set the following to your OpenTok API, a session ID, and
   a token for that session:

   ```
   var apiKey = '';
   var sessionId = '';
   var token = '';
   ```

   You can get your API key as well as a test session ID and token at the
   [OpenTok dashboard](https://dashboard.tokbox.com/). However, in a shipping application, use
   one of the [OpenTok server SDKs](https://tokbox.com/developer/sdks/server/) to generate a
   session ID and token.

2. Install the sample code on a web server. Note that you must load the code from a web server.
   Browsers do not support WebRTC video in pages loaded from a file:// URL.

3. In a web browser, navigate to the index.html page for the app. The app connects to the
   OpenTok session.

4. Grant the page access to your camera and microphone.

5. Disconnect the internet connection for your computer. (For example, if you are using Wi-Fi
   disable the Wi-Fi connection.)

   The Session Status text field in the app changes to "Disconnected from session. Attempting to
   reconnect."

6. Reconnect your computer to the network. Upon reconnecting to the OpenTok session, the Session
   Status text field changes to "Reconnected to the session."

Now we will add a second client connected to the session:

1. Mute your computer (so that you will not receive audio feedback).

2. Open a new browser window or tab, and navigate to the index.html page for the app. The app
   connects to the OpenTok session.

3. Grant the page access to your camera and microphone.

   In addition to the published video, each window displays the video from the other, which it
   subscribes to.

4. Disconnect the internet connection for your computer.

   The subscriber video stream display includes a "Stream disconnected temporarily. Attempting to
   reconnect."

5. Reconnect your computer to the network. Upon reconnecting to the OpenTok session, the
   "attempting to reconnect" message for the subscriber is hidden.

Now we will test how OpenTok signaling can be disabled when reconnecting to a session:

1. Leave the two browser windows open (and connected to the OpenTok session).

2. In one window, click the Send Signal button. Note that the signal is received and logged in
   each client.

3. Disconnect the internet connection for your computer, and click the Send Signal button again.

4. Reconnect your computer to the network. Upon reconnecting to the OpenTok session, note that the
   signal is not sent. This is because the `retryAfterReconnect` option is set to `false` in the
   call to the `Session.signal()` method. Note, however, that signals initiated by other clients
   that have _not_ lost their connections to the session while you are reconnecting will be
   received when your client reconnects.

## Understanding the code

Locate the main app.js file in the web/js directory. It connects to the OpenTok session and adds
a number of event listeners for the Session object:

```javascript
session.on({
  sessionReconnecting: function(event) {
    document.getElementById('log').innerText =
      'Disconnected from the session. Attempting to reconnect.';
  },
  sessionReconnected: function(event) {
    document.getElementById('log').innerText = 'Reconnected to the session.';
  },
  disconnected: function(event) {
    document.getElementById('log').innerText = 'Disconnected from the session.';
  }
});
```

The Session object dispatches `sessionReconnecting` and `sessionReconnected` events when the
client is attempting to reconnect to the session and when it successfully reconnects. The code
adjusts user interface messages upon each of these events being dispatched.

When the Session object dispatches the `streamCreated` event (indicating that another client's
stream is available in the session), the client subscribes to that stream. The Subscriber object
dispatches `disconnected` and `connected` events when the subscriber's stream is dropped
("disconnected") and when it is restored ("connected"):

```javascript
session.on({
  streamCreated: function(event) {
    var subscriber = session.subscribe(event.stream, 'subscribers', {insertMode: 'append'});
    var subscriberDisconnectedNotification = document.createElement('div');
    subscriberDisconnectedNotification.className = 'subscriberDisconnectedNotification';
    subscriberDisconnectedNotification.innerText =
      'Stream disconnected temporarily. Attempting to reconnect.';
    subscriber.element.appendChild(subscriberDisconnectedNotification);

    subscriber.on({
      disconnected: function(event) {
        subscriberDisconnectedNotification.style.visibility = 'visible';
      },
      connected: function(event) {
        subscriberDisconnectedNotification.style.visibility = 'hidden';
      }
    });
  }
});
```

Note that the app creates DIV elements (`subscriberDisconnectedNotification`) to be made visible
when the subscriber stream is dropped. These elements are initially hidden, and they are hidden
again when a subscriber stream is restored.

Finally, note that the object passed into the `Session.signal()` method includes a
`retryAfterReconnect` property. When set to `false`, signals that are initiated when the client
is attempting to reconnect are _not_ sent upon reconnection:

```javascript
function sendSignal() {
  session.signal({retryAfterReconnect: retrySignalOnReconnect});
}
```

The `retrySignalOnReconnect` setting is in the js/config.js file. If you set it to `true`,
signals that are initiated when the client is attempting to reconnect _are_ sent upon reconnection.
