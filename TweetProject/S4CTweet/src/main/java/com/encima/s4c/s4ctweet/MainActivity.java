package com.encima.s4c.s4ctweet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Console;
import java.io.FileOutputStream;

public class MainActivity extends Activity {

    Button tweet;
    String path = null;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("s4c", MODE_PRIVATE);
        prefs.edit().putString("path", null).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Button camera = (Button) findViewById(R.id.button);
        tweet = (Button) findViewById(R.id.tweet);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 1);
            }
        });
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(photo);
            try {
                path = Environment.getExternalStorageDirectory().getPath() + "/img.jpg";
                SharedPreferences prefs = getSharedPreferences("s4c", MODE_PRIVATE);
                prefs.edit().putString("path", path).commit();
                FileOutputStream out = new FileOutputStream(path);
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Image NOT Saved", Toast.LENGTH_LONG).show();
                path = null;
                prefs.edit().putString("path", path).commit();
            }
        }
        tweet.setEnabled(true);
    }

    public void prepareTweet(View v) {
        loadTweetInterface();
    }


    public void loadTweetInterface() {
        Intent i = new Intent(this, TweetActivity.class);
        startActivity(i);
    }
}
