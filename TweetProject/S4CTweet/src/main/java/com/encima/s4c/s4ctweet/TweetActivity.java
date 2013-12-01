package com.encima.s4c.s4ctweet;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;


public class TweetActivity extends Activity {

    String ACCESS_TOKEN = "";
    String ACCESS_TOKEN_SECRET = "";
    String CONSUMER_KEY = "";
    String CONSUMER_SECRET = "";
    String path;
    SharedPreferences prefs;

    EditText tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        // Show the Up button in the action bar.

        tweet = (EditText) findViewById(R.id.editText);

        setupActionBar();
        prefs = getSharedPreferences("s4c", MODE_PRIVATE);
        path = prefs.getString("path", null);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void handleTweet(View v) {

        new AsyncTask<SharedPreferences,Object, Boolean>() {
            @Override
            protected Boolean doInBackground(SharedPreferences... params) {
                sendTweet();
                return true;
            }
        }.execute();
    }

    public void sendTweet() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        TwitterFactory factory = new TwitterFactory(cb.build());
        Twitter twitter = factory.getInstance();

        String tweetTxt = tweet.getText().toString();
        StatusUpdate status = null;
        try {
            status = new StatusUpdate(tweetTxt);
            if(path != null) {
                status.setMedia(new File(path));
            }
            twitter.updateStatus(status);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        final StatusUpdate finalStatus = status;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Successfully updated the status to [" + finalStatus.getStatus() + "].", Toast.LENGTH_LONG).show();
                tweet.setText("");
                prefs.edit().putString("path", null).commit();
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
