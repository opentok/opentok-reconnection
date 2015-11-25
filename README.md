opentok-reconnection
====================
Samples for the [OpenTok](https://tokbox.com/developer/) automatic reconnection beta feature.

*Important* To use this feature, you must contact TokBox to participate in the beta program.
See the [OpenTok beta programs](https://tokbox.com/platform/beta-programs) page.

Clients connected to sessions that use the automatic reconnection feature can do the following:

* Attempt to reconnect to the session if the client has disconnected due to a temporary drop in
  network connectivity.

* Attempt to reconnect to a stream it is subscribing to that is temporarily dropped.

* Determine whether signals sent while attempting to reconnect to a session are sent upon
  reconnection (or not). (For more information, see the
  [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/).

This repo include samples for each of the OpenTok client SDKs. See the Android-sample, iOS-sample,
and js-sample subdirectories.
