// Make a copy of this file and rename it as 'config.js'.
// Then replace the following values with your OpenTok API key
// as well as a test session ID and token.
//
// For the purposes of quickly demonstrating the automatic reconnection
// functionality through this sample code, we are utilizing the 'config.js'
// file to set an API key, session ID and token to use. However, when
// deploying your application to production, you should not hardcode the
// session ID and token as we do here. Instead, you shoud use one of the
// OpenTok Server SDKs to dynamically generate a session ID and token.
// See https://tokbox.com/developer/sdks/server/ on how to use and deploy
// an OpenTok Server SDK.

var apiKey = 'YOUR-API-KEY';
var sessionId = 'YOUR-SESSION-ID';
var token = 'YOUR-SESSION-TOKEN';

// Set this to true and signals initiated while reconnecting to a session
// will be sent when reconnected:
var retrySignalOnReconnect = false;
