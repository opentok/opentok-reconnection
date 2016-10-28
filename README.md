opentok-reconnection
====================

This project contains sample code demonstrating the [OpenTok](https://tokbox.com/developer/)
Automatic Reconnection feature.

With Automatic Reconnection, your client can now automatically reconnect to OpenTok sessions
after temporary drops in network connectivity. If you have a mobile client using OpenTok, the
feature helps restore connectivity during transitions between network interfaces such as Wi-Fi
and LTE, allowing you to build a more robust integration and end-user experience.

Automatic reconnection is available to OpenTok sessions that use the the [OpenTok Media
Router](https://tokbox.com/developer/guides/create-session/#media-mode).

Clients connected to sessions that use the Automatic Reconnection feature can do the following:

* Attempt to automatically reconnect to the session if the client has disconnected due
  to a temporary drop in network connectivity.

* Attempt to automatically reconnect to a subscriber stream that is temporarily dropped.

* Upon a successful reconnection, automatically resend signals that are initiated
  by the client when it was temporarily disconnected. (For more information about Signaling,
  see the [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/).)
