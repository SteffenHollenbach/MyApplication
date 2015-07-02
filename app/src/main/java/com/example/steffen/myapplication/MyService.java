package com.example.steffen.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

    public static int maxValue = OverviewTab.maxValue;

    private NotificationManager nm;
    private Timer timer = new Timer();
    private final Calendar time = Calendar.getInstance();
    Notification notification;
    static SharedPreferences prefs;

    static Context c;

    @Override
    public void onCreate() {
        Log.e("WhatTheDroidService", System.currentTimeMillis()
                + ": WhatTheDroidService erstellt.");


        prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        c = MainActivity.context;


        createNotification();
        loadStats();
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(this, "Service created at " + time.getTime(), Toast.LENGTH_LONG).show();
        //updateNotification("My Cat");
        incrementCounter();
    }

    private boolean firstTime = true;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

    private void createNotification(){
        Intent notificationIntent = new Intent(this.getBaseContext(), MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent intent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setSmallIcon(R.drawable.dryer)
                .setContentTitle("SOS, Katzen-Alarm, pass gut auf!")
                .setOnlyAlertOnce(true)
                .setTicker("Deine Katze braucht dich!")
                .setSubText("Hilf ihr!")
                .setVibrate(new long[]{0, 200, 200, 400, 300, 600 , 400, 800})
                .setContentIntent(intent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(NOTIFICATION_SERVICE)
                .setContentText("FEHLER");
    }

    private void updateNotification(String message) {


        if (firstTime) {


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

                int critical = maxValue/10*3;

                if (int_feed > 0) {int_feed = int_feed - 20;}
                if (int_feed < 0) {int_feed = 0;}
                if (int_thirst > 0) {int_thirst = int_thirst -30;} //ACHTUNG ************************************************ -3
                if (int_thirst < 0) {int_thirst = 0;}
                if (int_fun > 0) {int_fun = int_fun -10;}
                if (int_fun < 0) {int_fun = 0;}

                if (int_feed < critical || int_thirst < critical || int_fun < critical) {
                    if ((int_feed < critical && int_thirst < critical) || (int_feed < critical && int_fun < critical) ||
                            (int_fun < critical && int_thirst < critical) || (int_feed < critical && int_thirst < critical && int_fun < critical)) {
                        updateNotification("Deiner Katze fehlt einiges.");
                    } else {

                        if (int_feed < critical) {
                            updateNotification("Deine Katze ist hungrig.");
                        } else if (int_thirst < critical) {
                            updateNotification("Deine Katze ist durstig.");
                        } else if (int_fun < critical) {
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

    static int defaultValue = maxValue / 2;

    public static void safeStats(){
        prefs.edit().putInt("FEED_KEY", int_feed).apply();
        prefs.edit().putInt("THIRST_KEY", int_thirst).apply();
        prefs.edit().putInt("FUN_KEY", int_fun).apply();
    }

    public static void clearStats(){
        prefs.edit().putInt("FEED_KEY", defaultValue).apply();
        prefs.edit().putInt("THIRST_KEY", defaultValue).apply();
        prefs.edit().putInt("FUN_KEY", defaultValue).apply();
    }

    public static void loadStats(){
        int_feed = prefs.getInt("FEED_KEY", defaultValue);
        int_thirst = prefs.getInt("THIRST_KEY", defaultValue);
        int_fun = prefs.getInt("FUN_KEY", defaultValue);
    }

    public static int getInt_feed() {
        return int_feed * 100 / maxValue;
    }

    public static int getInt_thirst() {
        return int_thirst * 100 / maxValue;
    }

    public static int getInt_fun() {
        return int_fun * 100 / maxValue;
    }


    public static void setInt_feed(int int_feed) {
        MyService.int_feed = MyService.int_feed + int_feed;
        if (MyService.int_feed > maxValue) {MyService.int_feed = maxValue;}
    }

    public static void setInt_thirst(int int_thirst) {
        MyService.int_thirst = MyService.int_thirst + int_thirst;
        if (MyService.int_thirst > maxValue) {MyService.int_thirst = maxValue;}
    }

    public static void setInt_fun(int int_fun) {
        MyService.int_fun = MyService.int_fun + int_fun;
        if (MyService.int_fun > maxValue) {MyService.int_fun = maxValue;}
    }

}
