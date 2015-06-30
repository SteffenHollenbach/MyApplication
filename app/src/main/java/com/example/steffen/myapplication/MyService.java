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

    public static int int_feed;
    public static int int_thirst;
    public static int int_fun;


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

        loadStats();
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(this, "Service created at " + time.getTime(), Toast.LENGTH_LONG).show();
        //updateNotification("My Cat");
        incrementCounter();
    }

    private boolean firstTime = true;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

    private void updateNotification(String message) {
        if (firstTime) {
            mBuilder.setSmallIcon(R.drawable.dryer)
                    .setContentTitle("My Cat")
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

                }
            };

            public void run() {
                if (int_feed > 0) {int_feed--;}
                if (int_thirst > 0) {int_thirst--;}
                if (int_fun > 0) {int_fun--;}
                //MainActivity.timer.setText(counter+"");
                if (int_feed < 30 || int_thirst < 30 || int_fun < 30) {
                    if (int_feed + int_thirst + int_fun < 159) {
                        updateNotification("Deiner Katze fehlt einiges.");
                    } else {

                        if (int_feed < 30) {
                            updateNotification("Deine Katze ist hungrig.");
                        } else if (int_thirst < 30) {
                            updateNotification("Deine Katze ist durstig.");
                        } else if (int_fun < 30) {
                            updateNotification("Deiner Katze ist langweilig.");
                        }
                    }
                } else {
                    nm.cancel(1);
                }

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
        clearStats();
        nm.cancel(1);
        Toast.makeText(this, "Service destroyed at " + time.getTime(), Toast.LENGTH_LONG).show();
        //counter=null;

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

    public static void safeStats(){
        prefs.edit().putInt("FEED_KEY", int_feed).apply();
        prefs.edit().putInt("THIRST_KEY", int_thirst).apply();
        prefs.edit().putInt("FUN_KEY", int_fun).apply();
    }

    public static void clearStats(){
        prefs.edit().putInt("FEED_KEY", 50).apply();
        prefs.edit().putInt("THIRST_KEY", 50).apply();
        prefs.edit().putInt("FUN_KEY", 50).apply();
    }

    public static void loadStats(){
        int_feed = prefs.getInt("FEED_KEY", 50);
        int_thirst = prefs.getInt("THIRST_KEY", 50);
        int_fun = prefs.getInt("FUN_KEY", 50);
    }

    public static int getInt_feed() {
        return int_feed;
    }

    public static int getInt_thirst() {
        return int_thirst;
    }

    public static int getInt_fun() {
        return int_fun;
    }


    public static void setInt_feed(int int_feed) {
        MyService.int_feed = MyService.int_feed + int_feed;
    }

    public static void setInt_thirst(int int_thirst) {
        MyService.int_thirst = MyService.int_thirst + int_thirst;
    }

    public static void setInt_fun(int int_fun) {
        MyService.int_fun = MyService.int_fun + int_fun;
    }

}
