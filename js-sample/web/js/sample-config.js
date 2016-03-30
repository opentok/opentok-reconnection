// Make a copy of this file and rename it as 'config.js'. 
// Then replace the following values with your OpenTok API key 
// as well as a test session ID and token.
// 
// Use the API key provided to you for the purposes of this developer 
// preview of the Automatic Reconnections feature. With the API key, 
// you can generate a session ID and token to use. To obtain an API key, 
// please see https://github.com/opentok/opentok-reconnection 
//
// For the purposes of quickly demonstrating the Automatic Reconnections
// functionality through this sample code, we are utilizing the 'config.js'
// file to set an API key, session ID and token to use. However, when 
// deploying your application to production, you should not hardcode the 
// session ID and token as we do here. Instead, you shoud use one of the
// OpenTok Server SDKs to dynamically generate a session ID and token.
// 
var apiKey = 'YOUR-API-KEY';
var sessionId = 'YOUR-SESSION-ID';
var token = 'YOUR-SESSION-TOKEN';

// Set this to true and signals initiated while reconnecting to a session
// will be sent when reconnected:
var retrySignalOnReconnect = false;
