package com.opentok.reconnection.sample;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final String LOGTAG = "demo-reconnection";
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ArrayList<Stream> mStreams;
    private Handler mHandler = new Handler();

    private RelativeLayout mPublisherViewContainer;
    private RelativeLayout mSubscriberViewContainer;

    // Spinning wheel for loading subscriber view
    private ProgressBar mLoadingSub;
    private ProgressDialog dialog;

    private boolean resumeHasRun = false;

    private static final String SESSION_ID = "";
    private static final String TOKEN = "";
    private static final String APIKEY = "";
    private static final boolean SUBSCRIBE_TO_SELF = false;

    private SessionListener mSessionListener;
    private SubscriberListener mSubscriberListener;
    private PublisherListener mPublisherListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG, "onCreate");
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mPublisherViewContainer = (RelativeLayout) findViewById(R.id.publisherview);
        mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberview);
        mLoadingSub = (ProgressBar) findViewById(R.id.loadingSpinner);
        dialog = new ProgressDialog(MainActivity.this);

        mStreams = new ArrayList<Stream>();

        sessionConnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSession != null) {
            mSession.onPause();

            if (mSubscriber != null) {
                mSubscriberViewContainer.removeView(mSubscriber.getView());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        } else {
            if (mSession != null) {
                mSession.onResume();
            }
        }
        reloadInterface();
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryTracker, filter);
    }

    @Override
    public void onStop() {
        super.onStop();

        unregisterReceiver(mBatteryTracker);

        if (isFinishing()) {
            if (mSession != null) {
                mSession.disconnect();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mSession != null) {
            mSession.disconnect();
        }
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mSession != null) {
            mSession.disconnect();
        }
        super.onBackPressed();
    }

    public void reloadInterface() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {
                    attachSubscriberView(mSubscriber);
                }
            }
        }, 500);
    }

    private void sessionConnect() {
        if (mSession == null) {
            mSessionListener = new SessionListener();
            mSession = new Session(MainActivity.this,
                    APIKEY, SESSION_ID);
            mSession.setSessionListener(mSessionListener);
            mSession.setReconnectionListener(mSessionListener);
            mSession.connect(TOKEN);
        }
    }


    private void subscribeToStream(Stream stream) {
        mSubscriberListener =  new SubscriberListener();
        mSubscriber = new Subscriber(MainActivity.this, stream);
        mSubscriber.setVideoListener(mSubscriberListener);
        mSubscriber.setStreamListener(mSubscriberListener);
        mSession.subscribe(mSubscriber);

        if (mSubscriber.getSubscribeToVideo()) {
            // start loading spinning
            mLoadingSub.setVisibility(View.VISIBLE);
        }
    }

    private void unsubscribeFromStream(Stream stream) {
        mStreams.remove(stream);
        if (mSubscriber.getStream().equals(stream)) {
            mSubscriberViewContainer.removeView(mSubscriber.getView());
            mSubscriber = null;
            if (!mStreams.isEmpty()) {
                subscribeToStream(mStreams.get(0));
            }
        }
    }

    private void attachSubscriberView(Subscriber subscriber) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        View view = subscriber.getView();
        mSubscriberViewContainer.removeView(view);
        mSubscriberViewContainer.addView(view, layoutParams);
        subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
    }

    private void attachPublisherView(Publisher publisher) {
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                320, 240);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.TRUE);
        layoutParams.bottomMargin = dpToPx(8);
        layoutParams.rightMargin = dpToPx(8);
        mPublisherViewContainer.addView(mPublisher.getView(), layoutParams);
    }

    private void showReconnectionDialog(boolean show){
        if (show) {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Reconnecting. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        else {
            dialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Session has been reconnected")
                    .setPositiveButton(android.R.string.ok, null);
            builder.create();
            builder.show();
        }
    }

    /**
     * Converts dp to real pixels, according to the screen density.
     *
     * @param dp A number of density-independent pixels.
     * @return The equivalent number of real pixels.
     */
    private int dpToPx(int dp) {
        double screenDensity = this.getResources().getDisplayMetrics().density;
        return (int) (screenDensity * (double) dp);
    }

    /**
     * BroadcastReceiver is used for receiving intents from the BatteryManager when the battery changed
     */
    private BroadcastReceiver mBatteryTracker = new BroadcastReceiver() {

        public void onReceive(Context context, Intent batteryStatus) {

            String action = batteryStatus.getAction();

            // information received from BatteryManager
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {

                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryUsePct = level / (float)scale;

                //Send info about the battery consume
                //if the session is reconnected at this moment, we will not send this info (retryAfterReconnection to false),
                //because the battery consume will not be the same.
                if (mSession != null ) {
                    mSession.sendSignal("signal", String.valueOf(batteryUsePct), false);
                }
            }
        }
    };

    private class SessionListener implements Session.ReconnectionListener, Session.SessionListener {

        @Override
        public void onConnected(Session session) {
            Log.i(LOGTAG, "Connected to the session.");
            if (mPublisher == null) {
                mPublisherListener = new PublisherListener();
                mPublisher = new Publisher(MainActivity.this, "publisher");
                mPublisher.setPublisherListener(mPublisherListener);
                attachPublisherView(mPublisher);
                mSession.publish(mPublisher);
            }
        }

        @Override
        public void onDisconnected(Session session) {
            Log.i(LOGTAG, "Disconnected from the session.");
            if (mPublisher != null) {
                mPublisherViewContainer.removeView(mPublisher.getView());
            }

            if (mSubscriber != null) {
                mSubscriberViewContainer.removeView(mSubscriber.getView());
            }

            mPublisher = null;
            mSubscriber = null;
            mStreams.clear();
            mSession = null;
        }

        @Override
        public void onReconnecting(Session session) {
            Log.i(LOGTAG, "Reconnecting the session "+session.getSessionId());
            showReconnectionDialog(true);
        }

        @Override
        public void onReconnected(Session session) {
            Log.i(LOGTAG, "Session has been reconnected");
            showReconnectionDialog(false);
        }


        @Override
        public void onError(Session session, OpentokError exception) {
            Log.i(LOGTAG, "Session exception: " + exception.getMessage());
        }

        @Override
        public void onStreamReceived(Session session, Stream stream) {

            if (!SUBSCRIBE_TO_SELF) {
                mStreams.add(stream);
                if (mSubscriber == null) {
                    subscribeToStream(stream);
                }
            }
        }

        @Override
        public void onStreamDropped(Session session, Stream stream) {
            if (!SUBSCRIBE_TO_SELF) {
                if (mSubscriber != null) {
                    unsubscribeFromStream(stream);
                }
            }
        }
    }

    private class PublisherListener implements Publisher.PublisherListener {

        @Override
        public void onStreamCreated(PublisherKit publisher, Stream stream) {
            if (SUBSCRIBE_TO_SELF) {
                mStreams.add(stream);
                if (mSubscriber == null) {
                    subscribeToStream(stream);
                }
            }
        }

        @Override
        public void onStreamDestroyed(PublisherKit publisher, Stream stream) {
            if ((SUBSCRIBE_TO_SELF && mSubscriber != null)) {
                unsubscribeFromStream(stream);
            }
        }

        @Override
        public void onError(PublisherKit publisher, OpentokError exception) {
            Log.i(LOGTAG, "Publisher exception: " + exception.getMessage());
        }

    }

    public class SubscriberListener implements Subscriber.VideoListener, Subscriber.StreamListener {

        @Override
        public void onVideoDataReceived(SubscriberKit subscriber) {
            Log.i(LOGTAG, "First frame received");

            // stop loading spinning
            mLoadingSub.setVisibility(View.GONE);
            attachSubscriberView(mSubscriber);
        }


        @Override
        public void onVideoDisabled(SubscriberKit subscriber, String reason) {
            Log.i(LOGTAG,
                    "Video disabled:" + reason);
        }

        @Override
        public void onVideoEnabled(SubscriberKit subscriber, String reason) {
            Log.i(LOGTAG, "Video enabled:" + reason);
        }

        @Override
        public void onVideoDisableWarning(SubscriberKit subscriber) {
            Log.i(LOGTAG, "Video may be disabled soon due to network quality degradation. Add UI handling here.");
        }

        @Override
        public void onVideoDisableWarningLifted(SubscriberKit subscriber) {
            Log.i(LOGTAG, "Video may no longer be disabled as stream quality improved. Add UI handling here.");
        }


        @Override
        public void onReconnected(SubscriberKit subscriberKit) {
            Log.i(LOGTAG, "Subscriber has been reconnected");
            Toast.makeText(MainActivity.this, "Subscriber has been reconnected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisconnected(SubscriberKit subscriberKit) {
            Log.i(LOGTAG, "Subscriber has been disconnected by connection error");
        }

    }

}