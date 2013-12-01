package com.encima.s4c.radioreader;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import org.json.JSONObject;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String progs = NetworkUtils.getJSON(getString(R.string.radio_url));
        Log.d("s4c", progs);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
