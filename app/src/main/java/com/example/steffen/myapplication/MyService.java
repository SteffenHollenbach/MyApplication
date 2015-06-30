package com.example.steffen.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Steffen on 29.06.2015.
 */
public class MyService extends Service {

    public static Long counter;
    private NotificationManager nm;
    private Timer timer = new Timer();
    private final Calendar time = Calendar.getInstance();
    Notification notification;
    static SharedPreferences prefs;

    @Override
    public void onCreate() {
        Log.e("WhatTheDroidService", System.currentTimeMillis()
                + ": WhatTheDroidService erstellt.");


        prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);

        counter = getCount();
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(this, "Service created at " + time.getTime(), Toast.LENGTH_LONG).show();
        updateNotification("Start");
        incrementCounter();
    }

    private boolean firstTime = true;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

    private void updateNotification(String message) {
        if (firstTime) {
            mBuilder.setSmallIcon(R.drawable.dryer)
                    .setContentTitle("My Notification")
                    .setOnlyAlertOnce(true);
            firstTime = false;
        }
        mBuilder.setContentText(message);


        nm.notify(1, mBuilder.build());
    }


    private void incrementCounter() {
        timer.scheduleAtFixedRate(new TimerTask(){

            private Handler updateUI = new Handler(){
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    if (counter % 10 == 0) {
                        Toast.makeText(getApplicationContext(), "Weitere 10 geschafft; counter is at: " + counter, Toast.LENGTH_LONG).show();
                    };
                }
            };

            public void run() {
                counter++;
                //MainActivity.timer.setText(counter+"");
                updateNotification("Counter: " + counter);

                try {

                    updateUI.sendEmptyMessage(0);
                } catch (Exception e) {e.printStackTrace(); }
            }




        }, 0, 1000L);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("WhatTheDroidService", System.currentTimeMillis()
                + ": WhatTheDroidService gestartet.");



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("WhatTheDroidService", System.currentTimeMillis()
                + ": WhatTheDroidService zerstoert.");

        shutdownCounter();
        clearCount();
        nm.cancel(1);
        Toast.makeText(this, "Service destroyed at " + time.getTime() + "; counter is at: " + counter, Toast.LENGTH_LONG).show();
        counter=null;

    }

    private void shutdownCounter() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    public static void safeCount(){
        prefs.edit().putLong("COUNTER_KEY", counter).apply();
    }

    public static void clearCount(){
        prefs.edit().putLong("COUNTER_KEY", 0).apply();
    }

    public static long getCount(){
        return prefs.getLong("COUNTER_KEY", 0);
    }

    public static long getCountLive(){
        return counter;
    }
}
