OpenTok.js Automatic Reconnection Sample Code
=============================================

This sample code shows you how to use the OpenTok Automatic Reconnection feature on the web.

**Important:** To use this feature, you must contact TokBox to participate in this developer preview.
See the main project [README](../README.md) on how to enroll.

Clients connected to sessions that use the Automatic Reconnection feature can do the following:

* Attempt to automatically reconnect to the session if the client has disconnected due 
  to a temporary drop in network connectivity.

* Attempt to automatically reconnect to a subscriber stream that is temporarily dropped.

* Upon a successful reconnection, automatically resend signals that are initiated 
  by the client when it was temporarily disconnected. (For more information about Signaling, 
  see the [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/).)

## Trying the sample code

To configure and try the sample code:

1. In the `web/js/` directory, make a copy of the `sample-config.js` file and rename this 
   copy as `config.js`. Within the file, set the following variables to your developer preview
   API key, an OpenTok session ID to use, and a token for that session:

	 ```javascript
   var apiKey = 'YOUR-API-KEY';
   var sessionId = 'YOUR-SESSION-ID';
   var token = 'YOUR-SESSION-TOKEN';
   ```

   Use the API key provided to you for the purposes of this developer preview of the
   Automatic Reconnection feature. With the API key, you can generate a session ID
   and token to use. To obtain an API key, please see the main project [README](../README.md) 
   on how to enroll in this developer preview.

   For the purposes of quickly demonstrating the Automatic Reconnection feature, this sample code
   uses the `config.js` file to set an API key, a session ID and a token to use. However,
   when deploying your application to production, you should not hardcode the session ID
   and token as we do here. Instead, you should use one of the
   [OpenTok Server SDKs](https://tokbox.com/developer/sdks/server/) to
   dynamically generate a session ID and token.

2. In the `web/` directory, open the `index.html` file. Note that the app references the
   developer preview version of OpenTok.js:

   ```html
   <sscript src="https://preview.tokbox.com/v2/js/opentok.js" type="text/javascript" charset="utf-8"></script>
   ```

   You must use this version to use the Automatic Reconnection feature.

3. Install the sample code on a web server. Note that you must load the code from a web server
   (you can run a web server locally on your machine and make it accessible at localhost).
   Browsers do not support WebRTC video in pages loaded directly from a file:// URL.

4. In a web browser, navigate to the `index.html` page for the app. The app connects to the
   OpenTok session with the specified session ID.

5. Grant the page access to your camera and microphone.

6. Disconnect the internet connection for your computer. (For example, if you are using Wi-Fi,
   disable the Wi-Fi connection.)

   The Session Status text field shown on the webpage changes to "Disconnected from session. Attempting to
   reconnect..."

7. Reconnect your computer to the network. Upon reconnecting to the OpenTok session, the displayed
   text field changes to "Reconnected to the session."

Now we will add a second client connected to the session:

1. Mute your computer (so that you will not receive audio feedback).

2. Open a new browser window or tab, and navigate to the `index.html` page, where it will
   connect to the OpenTok session.

3. Grant the page access to your camera and microphone.

   In addition to the published video, each window displays the video from the other 
   participant it is subscribed to.

4. Disconnect the internet connection for your computer.

   The subscriber video element now shows "Stream has been disconnected unexpectedly. 
   Attempting to automatically reconnect..."

5. Reconnect your computer to the network. Upon reconnecting to the OpenTok session, the
   "attempting to reconnect" message for the subscriber is hidden.

Now we will test how OpenTok signaling can be disabled when reconnecting to a session:

1. Leave the two browser windows open (and connected to the OpenTok session).

2. In one window, click the "Send Signal" button. Note that the signal is received and logged in
   each client.

3. Disconnect the internet connection for your computer, and click the "Send Signal" button again.

4. Reconnect your computer to the network. Upon reconnecting to the OpenTok session, note that the
   signal is not sent. This is because the `retryAfterReconnect` option is set to `false` in the
   call to the `Session.signal()` method, so signals that were initiated when the client is 
   disconnected will not be sent. However, signals initiated by other clients that are still connected 
   to the session will be received when your client successfully reconnects.

## Understanding the code

Within the `web/js` directory, locate the `app.js` file, which contains code to connect to 
an OpenTok session and adds a number of event listeners for the Session object:

```javascript
session.on({
  sessionReconnecting: function(event) {
    document.getElementById('log').innerText =
      'Disconnected from the session. Attempting to reconnect...';
  },
  sessionReconnected: function(event) {
    document.getElementById('log').innerText = 'Reconnected to the session.';
  },
  sessionDisconnected: function(event) {
    document.getElementById('log').innerText = 'Disconnected from the session.';
  }
});
```

The Session object in the code snippet above dispatches the `sessionReconnecting` 
event when the client is 
attempting to reconnect to the session. The `sessionReconnected` event is dispatched 
when the reconnection attempt succeeds. The code adjusts the user interface 
message in response to each event.

In the code snippet below, when the `streamCreated` event occurs (indicating that 
another client's stream is available in the session), the client subscribes to 
that stream. The Subscriber object dispatches the `disconnected` event when the 
subscriber stream is dropped and dispatches the `connected` event when it is restored.
The client creates a `<div>` element to notify the user when the subscriber is disconnected.
These elements are initially hidden, and they are hidden again when 
a subscriber stream is restored.

```javascript
session.on({
  streamCreated: function(event) {
    var subscriber = session.subscribe(event.stream, 'subscribers', {insertMode: 'append'});
    var subscriberDisconnectedNotification = document.createElement('div');
    subscriberDisconnectedNotification.className = 'subscriberDisconnectedNotification';
    subscriberDisconnectedNotification.innerText =
      'Stream has been disconnected unexpectedly. Attempting to automatically reconnect...';
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

Finally, note that the object passed into the `Session.signal()` method includes 
a `retryAfterReconnect` property. When set to `false`, signals that are initiated 
when the client is attempting to reconnect are _not_ sent upon reconnection. If 
you set it to `true`, signals that are initiated when the client is attempting to 
reconnect _are_ sent upon reconnection.

```javascript
function sendSignal() {
  session.signal({retryAfterReconnect: retrySignalOnReconnect});
}
```

The `retrySignalOnReconnect` variable is set in the `config.js` file.
