opentok-reconnection
====================

This project contains sample code demonstrating the [OpenTok](https://tokbox.com/developer/)
automatic reconnection developer preview.

With automatic reconnections, your client can now automatically reconnect to OpenTok sessions
after temporary drops in network connectivity. If you have a mobile client using OpenTok, the
feature helps restore connectivity during transitions between network interfaces such as Wi-Fi
and LTE, allowing you to build a more robust integration and end-user experience.

Clients connected to sessions that use the automatic reconnection feature can do the following:

* Attempt to automatically reconnect to the session if the client has disconnected due
  to a temporary drop in network connectivity.

* Attempt to automatically reconnect to a subscriber stream that is temporarily dropped.

* Upon a successful reconnection, automatically resend signals that are initiated
  by the client when it was temporarily disconnected. (For more information about Signaling,
  see the [Signaling developer guide](https://tokbox.com/developer/guides/signaling/js/).)

The preview of the automatic reconnection feature is currently available to an initial group of
developers. To use the preview feature, you need to do the following:

* Join the preview program

* Modify your server-side code that uses the OpenTok server SDKs to use the OpenTok preview servers.
  (The preview feature is not available using the standard OpenTok servers.)

* Use a preview version of the OpenTok client SDK.

See the following sections for details.

## Joining the preview

We are making a preview of of the automatic reconnection feature available to an initial group
of developers. To be a part of this group, please send the following information to
*automatic-reconnection-beta@tokbox.com*:

* Provide your preferred contact email for this developer preview.

* List the platforms (Android, iOS, Web, etc.) on which your product uses OpenTok.

* Explain the current problems that you have you hope would be solved by the automatic
  reconnection feature.

* Are session disconnections a common issue for your product experience with OpenTok?
  In your own investigations, what have been common causes for these disconnections?

* How are you solving session disconnections right now? Do you take certain
  steps in your code/architecture implementation to handle this? Do you take
  certain steps in your product/user experience to account for session disconnections?

A developer preview API key and secret will be sent to your contact email. Use this API key
to evaluate and test the automatic reconnection feature. Reconnection is enabled on demand,
for a given API key and on the preview environment using preview versions of the OpenTok client
SDKs (Android, iOS, or JavaScript).

Once you receive your developer preview API key and secret, you will need to make some changes
to the OpenTok server-side and client code used by your app (see the following sections).

## Modifying your server-side code

You need to configure the [OpenTok server-side SDK](https://tokbox.com/developer/sdks/server/)
to use the preview environment (for testing the automatic reconnection feature). To do this,
you need to provide an `apiUrl` parameter when initializing the OpenTok object with the
server-side SDK. Set the `apiUrl` to `https://anvil-preview.opentok.com`. You also need to use
the developer preview API key and secret.

See the following examples:

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

$apiUrl = 'https://anvil-tbdev.opentok.com';
$opentok = new OpenTok($apiKey, $apiSecret, $apiUrl);
```

##### Python

```python
from opentok import OpenTok

api_url = 'https://anvil-tbdev.opentok.com'
opentok = OpenTok(api_key, api_secret, api_url)
```

##### Ruby

```ruby
require "opentok"

api_url = "https://anvil-tbdev.opentok.com"
opentok = OpenTok::OpenTok.new api_key, api_secret, api_url
```

##### .Net

```dotnet
using OpenTokSDK;

// ...

string ApiUrl = "https://anvil-preview.opentok.com";
var OpenTok = new OpenTok(ApiKey, ApiSecret, ApiUrl);
```

## Using a preview version of the OpenTok client SDK

This repository includes samples for the OpenTok automatic reconnection beta feature and for each of the OpenTok client SDKs. See the Android-sample, iOS-sample, and js-sample subdirectories.

To use  developer preview, you will need to use the developer preview version of the OpenTokSDK
client-side SDK:

* Android SDK: https://mobile-meet.tokbox.com/latest?product=otkit-android-sdk&redirect=1

* iOS SDK: https://mobile-meet.tokbox.com/latest?product=otkit-ios-sdk&redirect=1

* JS SDK: https://preview.tokbox.com/v2/js/opentok.js

## Send us feedback

As you use the preview feature, we would like to have an agile communication and development cycle
with you. We appreciate if you can provide frequent feedback. In particular we are trying to answer the following questions:

* Are we solving the connectivity problem?

* Is the API solving your use case?

* Is the feature performing as you expected?

Please send your comments to *automatic-reconnection-beta@tokbox.com*.