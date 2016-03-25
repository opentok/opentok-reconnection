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
this group, you would need to do the following: 

1. Answer the following questions and send back your responses to us 
at **automatic-reconnection-beta@tokbox.com**

> 1. Your preferred contact email for this developer preview.
>
> 2. Please list which platforms (Android, iOS, Web, etc.) that your 
>    product is implemented on with OpenTok.
>
> 3. Please explain the current problems that you have that you hope 
>    would be solved by the Automatic Reconnection feature.
>
> 4. Are session disconnections a common issue for your product experience with OpenTok? 
>    In your own investigations, what have been common causes for these disconnections? 
>
> 5. How are you solving session disconnections right now? Do you take certain 
>    steps in your code/architecture implementation to handle this? Do you take 
>    certain steps in your product/user experience to account for session disconnections?

2. A developer preview API key and secret would then be provided to your
given contact email. You will use this API key with your choice of an OpenTok 
client-side SDK and a server-side SDK.

3. Select your choice of an OpenTok client-side SDK from the available Android, iOS 
and JS options. For the purposes of this developer preview, you will need to use 
one of the developer preview versions of the client-side SDK provided below:
  * Android SDK: https://mobile-meet.tokbox.com/latest?product=otkit-android-sdk&redirect=1
  * iOS SDK: https://mobile-meet.tokbox.com/latest?product=otkit-ios-sdk&redirect=1
  * JS SDK: https://preview.tokbox.com/v2/js/opentok.js

4. Select your choice of an OpenTok server-side SDK from https://tokbox.com/developer/sdks/server/.
Once you have selected a language for the server-side SDK, you will need to configure it
to point to our Developer Preview server environment
	* Node.js: 
	* Java:
  * PHP:
  * Python:
  * Ruby:
  * .Net: 

5. Use your selected OpenTok server-side SDK to generate a Session ID and token with
your provided Developer Preview API key and secret. You will use the Session ID and token
with the sample code provided in this project to test out the automatic reconnections feature.

This repository include samples for each of the OpenTok client 
SDKs: Android, iOS and Javascript. See the Android-sample, iOS-sample,
and js-sample subdirectories.


## Automatic Reconnections Overview

Clients connected to sessions that use the automatic reconnection feature can do the following:

* Attempt to automatically reconnect to the session if the client has disconnected due
  to a temporary drop in network connectivity.

* Attempt to automatically reconnect to a subscriber stream that is temporarily dropped.

* Upon a successful reconnection, automatically resend signals that are initiated
  by the client when it was temporarily disconnected. (For more information about Signaling,
  see the [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/))



