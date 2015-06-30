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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    public static Context context;

    public static TextView textView_feed, textView_feed_percent, textView_thirst, textView_thirst_percent, textView_fun, textView_fun_percent, textView_mood;
    SeekBar seekbar_feed, seekbar_thirst, seekbar_fun;
    Button button_feed, button_thirst, button_fun;
    public static int timeSec = 0;
    public static int maxValue = 1000;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    Handler mHandler;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manin_animal);

        context = this;
        textView_feed = (TextView) findViewById(R.id.textView_feed);
        textView_feed_percent = (TextView) findViewById(R.id.textView_feed_percent);
        textView_thirst = (TextView) findViewById(R.id.textView_thirst);
        textView_thirst_percent = (TextView) findViewById(R.id.textView_thirst_percent);
        textView_fun = (TextView) findViewById(R.id.textView_fun);
        textView_fun_percent = (TextView) findViewById(R.id.textView_fun_percent);
        textView_mood = (TextView) findViewById(R.id.textView_mood);

        button_feed = (Button) findViewById(R.id.button_feed);
        button_thirst = (Button) findViewById(R.id.button_thirst);
        button_fun = (Button) findViewById(R.id.button_fun);

        seekbar_feed = (SeekBar) findViewById(R.id.seekBar_feed);
        seekbar_thirst = (SeekBar) findViewById(R.id.seekBar_thirst);
        seekbar_fun= (SeekBar) findViewById(R.id.seekBar_fun);

        seekbar_feed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        seekbar_thirst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        seekbar_fun.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        createThread();

        if (isMyServiceRunning(MyService.class)){
            Log.e("WhatTheDroidService", "Service gefunden");
            startThread();
        } else {
            Log.e("WhatTheDroidService", "Service nicht gefunden");
            startService(new Intent(context, MyService.class));
            if (!t.isAlive()){
                startThread();
            }
        };

        final int addValue = maxValue / 10;

        button_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MyService.setInt_feed(addValue);
                refreshStatsGUI();
            }
        });

        button_thirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MyService.setInt_thirst(addValue);
                refreshStatsGUI();
            }
        });

        button_fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MyService.setInt_fun(addValue);
                refreshStatsGUI();
            }
        });




    }

    public void refreshStatsGUI(){
        try {
            textView_feed_percent.setText(MyService.getInt_feed()+"");
            seekbar_feed.setProgress(Integer.parseInt(MyService.getInt_feed()+""));
            textView_thirst_percent.setText(MyService.getInt_thirst()+"");
            seekbar_thirst.setProgress(Integer.parseInt(MyService.getInt_thirst()+""));
            textView_fun_percent.setText(MyService.getInt_fun()+"");
            seekbar_fun.setProgress(Integer.parseInt(MyService.getInt_fun()+""));
        } catch (Exception e) {}
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
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    textView_feed_percent.setText(MyService.getInt_feed()+"");
                                    seekbar_feed.setProgress(Integer.parseInt(MyService.getInt_feed()+""));
                                    textView_thirst_percent.setText(MyService.getInt_thirst()+"");
                                    seekbar_thirst.setProgress(Integer.parseInt(MyService.getInt_thirst()+""));
                                    textView_fun_percent.setText(MyService.getInt_fun()+"");
                                    seekbar_fun.setProgress(Integer.parseInt(MyService.getInt_fun()+""));
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
        MyService.safeStats();
    }

    @Override
    protected void onDestroy() {
        super.onStop();  // Always call the superclass method first
        MyService.safeStats();
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
