package com.example.steffen.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    public static Context context;

    public static TextView timer;
    public static RatingBar rate;
    Button start, stop;
    public static int timeSec = 0;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    Handler mHandler;
    Thread t;

    //ToDo prüfen ob Service läuft -> Wert abholen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        timer = (TextView) findViewById(R.id.timerValue);
        start = (Button) findViewById(R.id.startButton);
        stop = (Button) findViewById(R.id.pauseButton);
        rate = (RatingBar) findViewById(R.id.ratingBar);

        timer.setText(timeSec + ":000");
        createThread();

        if (isMyServiceRunning(MyService.class)){
            Log.e("WhatTheDroidService", "Service gefunden");
            startThread();
        } else {
            Log.e("WhatTheDroidService", "Service nicht gefunden");
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startService(new Intent(context,MyService.class));
                startThread();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                stopService(new Intent(context,MyService.class));
                //t = null;
            }
        });




    }

    public void startThread(){
        t.start();
    }

    public void createThread() {
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    timer.setText(MyService.getCountLive()+"");
                                    rate.setRating(MyService.getCountLive()%5);
                                } catch (Exception e) {}

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        MyService.safeCount();
    }

    @Override
    protected void onDestroy() {
        super.onStop();  // Always call the superclass method first
        MyService.safeCount();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
