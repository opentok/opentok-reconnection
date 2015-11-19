var session = OT.initSession(apiKey, sessionId);

session.on({
  sessionReconnecting: function(event) {
    document.getElementById('log').innerText =
      'Disconnected from the session. Attempting to reconnect.';
  },
  sessionReconnected: function(event) {
    document.getElementById('log').innerText = 'Reconnected to the session.';
  },
  disconnected: function(event) {
    document.getElementById('log').innerText = 'Disconnected from the session.';
  },
  streamCreated: function(event) {
    var subscriber = session.subscribe(event.stream, 'subscribers', {insertMode: 'append'});
    var subscriberDisconnectedNotification = document.createElement('div');
    subscriberDisconnectedNotification.className = 'subscriberDisconnectedNotification';
    subscriberDisconnectedNotification.innerText =
      'Stream disconnected temporarily. Attempting to reconnect.';
    subscriber.element.appendChild(subscriberDisconnectedNotification);
    
    subscriber.on({
      disconnected: function(event) {
        subscriberDisconnectedNotification.style.visibility = 'visible';
      },
      connected: function(event) {
        subscriberDisconnectedNotification.style.visibility = 'hidden';
      }
    });
  },
  signal: function(event) {
    if (event.from == session.connection) {
      var fromStr = 'from you'
    } else {
      fromStr = 'from another client'
    }
    var timeStamp = new Date().toLocaleString()
    timeStamp = timeStamp.substring(timeStamp.indexOf(',') + 2);
    document.getElementById('signals').innerHTML = timeStamp
      + ' - Signal received ' + fromStr + '.<br>' 
      + document.getElementById('signals').innerHTML;
  }
});

session.connect(token, function(error) {
  if (error) {
    document.getElementById('log').innerText = 'Error connecting to the session.';
    return;
  }
  document.getElementById('log').innerText = 'Connected to the session.';
  var publisher = OT.initPublisher('publisher', {insertMode: 'append'});
  session.publish(publisher);
});

function sendSignal() {
  session.signal({retryAfterReconnect: retrySignalOnReconnect});
}
