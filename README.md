opentok-reconnection
====================
Samples for the [OpenTok](https://tokbox.com/developer/) automatic reconnection beta feature.

*Important* To use this feature, you must contact TokBox to participate in the beta program.
See the [OpenTok beta programs](https://tokbox.com/platform/beta-programs) page.

Clients connected to sessions that use the automatic reconnection feature can do the following:

* When disconnected from a session due to a temporary drop in network connectivity,
  the client will attempt to automatically reconnect and rejoin the session. 

* When a stream that a client is subscribing to is temporarily dropped, 
  the client will attempt to automatically reconnect and resubscribe to the stream.

* Determine whether signals sent while attempting to reconnect to a session are sent upon
  reconnection (or not). For more information, see the signaling developer guide for   
  [JS](https://tokbox.com/developer/guides/signaling/js/), [iOS] (https://tokbox.com/developer/guides/signaling/ios/) and [Android] (https://tokbox.com/developer/guides/signaling/android/).

This repository include samples for each of the OpenTok client SDKs: Android, iOS and Javascript. See the Android-sample, iOS-sample,
and js-sample subdirectories.
