opentok-reconnection
====================

This project contains sample code demonstrating the [OpenTok](https://tokbox.com/developer/)
Automatic Reconnection feature.

With Automatic Reconnections, your client can now automatically reconnect to OpenTok sessions
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

A preview of this feature is currently available to an initial group of
developers. To use this feature, you need to do the following:

* Join the preview program

* Use a preview version of the OpenTok client SDK

* Modify your server-side code to use the OpenTok preview servers

* Send us your feedback

See the following sections for details.

## Joining the preview

We are making a preview of the Automatic Reconnection feature available to an initial group
of developers. To be a part of this group, please send the following information to
*automatic-reconnection-beta@tokbox.com*:

* Provide your preferred contact email for this developer preview.

* List the platforms (Android, iOS, Web, etc.) on which your product uses OpenTok.

* Explain the current problems that you have you hope would be solved with Automatic
  Reconnections.

* Are session disconnections a common issue for your product experience with OpenTok?
  In your own investigations, what have been common causes for these disconnections?

* How are you solving session disconnections right now? Do you take certain
  steps in your code/architecture implementation to handle this? Do you take
  certain steps in your product/user experience to account for session disconnections?

A developer preview API key and secret will be sent to your contact email. Use this API key
to evaluate and test the Automatic Reconnection feature. 

Once you receive your developer preview API key and secret, you will also need to update
the OpenTok server-side and client code used by your app (see the following sections).


## Using a preview version of the OpenTok client SDK

This repository includes JavaScript, iOS and Android client code samples demonstrating how
to use the OpenTok Automatic Reconnection feature. See the Android-sample, iOS-sample, and 
js-sample subdirectories.

The Automatic Reconnection feature only works with a developer preview version of the OpenTok
client-side SDK, which is available for the following platforms:

* Android SDK: https://mobile-meet.tokbox.com/latest?product=otkit-android-sdk&redirect=1

* iOS SDK: https://mobile-meet.tokbox.com/latest?product=otkit-ios-sdk&redirect=1

* JS SDK: https://preview.tokbox.com/v2/js/opentok.js


## Modifying your server-side code

You need to configure the [OpenTok server-side SDK](https://tokbox.com/developer/sdks/server/)
you are using to use our preview environment. To do so, you will provide an `apiUrl` parameter 
when initializing the OpenTok object with the server-side SDK. Set the value of the `apiUrl` 
to `https://anvil-preview.opentok.com` as shown below.

You will also need to use the developer preview API key and secret provided to you.

##### Node.js

```javascript
var OpenTok = require('opentok'),
    apiUrl = 'https://anvil-preview.opentok.com',
    opentok = new OpenTok(apiKey, apiSecret, apiUrl);
```

##### Java

```java
String apiUrl = "https://anvil-preview.opentok.com";
OpenTok opentok = new OpenTok(apiKey, apiSecret, apiUrl);
```

##### PHP

```php
use OpenTok\OpenTok;

$apiUrl = 'https://anvil-preview.opentok.com';
$opentok = new OpenTok($apiKey, $apiSecret, $apiUrl);
```

##### Python

```python
from opentok import OpenTok

api_url = 'https://anvil-preview.opentok.com'
opentok = OpenTok(api_key, api_secret, api_url)
```

##### Ruby

```ruby
require "opentok"

api_url = "https://anvil-preview.opentok.com"
opentok = OpenTok::OpenTok.new api_key, api_secret, api_url
```

##### .Net

```dotnet
using OpenTokSDK;

// ...

string ApiUrl = "https://anvil-preview.opentok.com";
var OpenTok = new OpenTok(ApiKey, ApiSecret, ApiUrl);
```

## Sending us feedback

As you use the preview feature, we would like to have your feedback and comments. In particular, we are trying to answer the following questions:

* Are we solving your connectivity problem?

* Is the API solving your use case?

* Is the feature performing as you expected?

Please send us your comments at *automatic-reconnection-beta@tokbox.com*.

