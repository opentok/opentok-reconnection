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

## Development and Contributing

Interested in contributing? We :heart: pull requests! See the 
[Contribution](CONTRIBUTING.md) guidelines.

## Getting Help

We love to hear from you so if you have questions, comments or find a bug in the project, let us know! You can either:

- Open an issue on this repository
- See <https://support.tokbox.com/> for support options
- Tweet at us! We're [@VonageDev](https://twitter.com/VonageDev) on Twitter
- Or [join the Vonage Developer Community Slack](https://developer.nexmo.com/community/slack)

## Further Reading

- Check out the Developer Documentation at <https://tokbox.com/developer/>
  