opentok-reconnection
====================

With Automatic Reconnections, your client can now automatically reconnect to OpenTok sessions during temporary drops in network connectivity. If you have a mobile client using OpenTok, the feature helps restores connectivity during transitions between network interfaces such as Wi-Fi and LTE, allowing you to build a more robust integration and end-user experience.

*Important* To use this feature, you must contact TokBox to participate in the beta program.
See the [OpenTok beta programs](https://tokbox.com/platform/beta-programs) page.

At this stage of the development we would like to have an agile communication and development cycle with you. Reconnection is enabled on demand, for a given APIKey and on the preview environment using the preview clients (Android, iOS or Js). We appreciate if you can provide frequent feedback from product and development side, in particular we are trying to answer the following questions:
* Are we solving the connectivity problem?
* Is the API solving your use case?
* Is the feature performing as you expected?

The purpose of this repository is twofold,
* Explain the process to use the preview environment where the feature is enabled
* Provide an example to help you integrate the feature into your application. 

From a process perspective, there are two simple steps:
* Creation of a new account for testing. You need a new APIkey and secret to use the preview environment consisting of OpenTok cloud and clients (iOS, Android and JS). OpenTok cloud and clients  are updated frequently with our latest features and bug fixes. TokBox will take care of this step, Please, send us the email you want to use for this account and we will enable reconnection for the new API Key.
* Update your application (client and back-end). Note that you have to update the server SDK you are using to point to our preview environment (https://http://anvil-preview.opentok.com/).
 
From sample perspective, this repository include samples for the [OpenTok](https://tokbox.com/developer/) automatic reconnection beta feature and for each of the OpenTok client SDKs. See the Android-sample, iOS-sample, and js-sample subdirectories.

Clients connected to sessions that use the automatic reconnection feature can do the following:

* Attempt to reconnect to the session if the client has disconnected due to a temporary drop in
  network connectivity.

* Attempt to reconnect to a stream it is subscribing to that is temporarily dropped.

* Determine whether signals sent while attempting to reconnect to a session are sent upon
  reconnection (or not). For more information, see the signaling developer guide for   
  [JS](https://tokbox.com/developer/guides/signaling/js/), [iOS] (https://tokbox.com/developer/guides/signaling/ios/) and [Android] (https://tokbox.com/developer/guides/signaling/android/).

