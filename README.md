opentok-reconnection
====================

This project contains sample code demonstrating the [OpenTok](https://tokbox.com/developer/)
Automatic Reconnection feature.

With Automatic Reconnection, your client can now automatically reconnect to OpenTok sessions
after temporary drops in network connectivity. If you have a mobile client using OpenTok, the
feature helps restore connectivity during transitions between network interfaces such as Wi-Fi
and LTE, allowing you to build a more robust integration and end-user experience.

Clients connected to sessions that use the Automatic Reconnection feature can do the following:

* Attempt to automatically reconnect to the session if the client has disconnected due
  to a temporary drop in network connectivity.

* Attempt to automatically reconnect to a subscriber stream that is temporarily dropped.

* Upon a successful reconnection, automatically resend signals that are initiated
  by the client when it was temporarily disconnected. (For more information about Signaling,
  see the [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/).)

The Automatic Reconnection feature is currently available as an open beta.
To use this feature, you need to do the following:

* Join the beta program

* Enroll your API keys for the Automatic Reconnection feature

* Send us your feedback

See the following sections for details.

## Joining the beta

To join the open beta for the Automatic Reconnection feature, 
please send the following information to 
*automatic-reconnection-beta@tokbox.com*:

* Provide your preferred contact email for this beta.

* List the platforms (Android, iOS, Web, etc.) on which your product uses OpenTok.

* Provide your API keys that are used for development (if any), for beta testing (if any)
  and for production release.

## Enrolling your API keys

Based upon the information provided in the prior step, TokBox will enroll your
API keys that you use for testing and development for the Automatic Reconnection feature. 
We would initially recommend that you first evaluate the feature using your development and
testing branches. As you discover issues and/or have specific feedback, let us know
at *automatic-reconnection-beta@tokbox.com*.

Should you require this feature for your production API key, be aware that this feature
is still in beta, and thus would be subject to change and issues. We can work with you 
on a roll-out plan to deploy the Automatic Reconnection feature to your production traffic, 
should you be comfortable of the risks involved.

## Sending us feedback

As an open beta, we would like to have your feedback and comments. 
In particular, we are trying to answer the following questions:

* Is the Automatic Reconnection feature solving your connectivity problem?

* Is the API solving your use case?

* Is the feature performing as you expected?

Please send us your comments at *automatic-reconnection-beta@tokbox.com*.

