opentok-reconnection
====================
This project contains sample code demonstrating the 
OpenTok Automatic Reconnection Developer Preview.

With Automatic Reconnections, your client can now automatically reconnect 
to OpenTok sessions during temporary drops in network connectivity. 
If you have a mobile client using OpenTok, the feature helps restores 
connectivity during transitions between network interfaces such as Wi-Fi and LTE, 
allowing you to build a more robust integration and end-user experience.

We are about to provide a preview of Automatic Reconnections to an 
initial group of developers. If you are interested in being a part of 
this group, you would need to do the following:. 


Answer the following questions and send back your responses to us 
at **automatic-reconnection-beta@tokbox.com**

> 1. Please list which platforms (iOS, Android, Web, etc.) that your 
>    product is implemented on with OpenTok.
>
> 2. Please explain the current problems that you have that you hope 
>    would be solved by the Automatic Reconnection feature.
>
> 3. Are session disconnections a common issue for your product experience with OpenTok? 
>    In your own investigations, what have been common causes for these disconnections? 
>
> 4. How are you solving session disconnections right now? Do you take certain 
>    steps in your code/architecture implementation to handle this? Do you take 
>    certain steps in your product/user experience to account for session disconnections?


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

This repository include samples for each of the OpenTok client 
SDKs: Android, iOS and Javascript. See the Android-sample, iOS-sample,
and js-sample subdirectories.
