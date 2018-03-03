package com.example.imran.blooddonors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by IMRAN on 12/19/2017.
 */

public class Intro extends Activity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intr);

            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(Intro.this, Login.class);
                    startActivity(intent);

                    finish();
                }
            }, 2000);
    }
}
