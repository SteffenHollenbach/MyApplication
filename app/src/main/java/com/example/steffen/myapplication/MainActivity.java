package com.example.steffen.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    // Declaring our tabs and the corresponding fragments.
    ActionBar.Tab OverviewTabX, WorkTabX, StoreTabX;
    Fragment OverviewFragmentTab = new OverviewTab();
    Fragment WorkFragmentTab = new WorkTab();
    Fragment StoreFragmentTab = new StoreTab();



    public static Context context;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manin_animal);


        // Asking for the default ActionBar element that our platform supports.
        ActionBar actionBar = getSupportActionBar();

        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(false);

        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(false);

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Setting custom tab icons.
        OverviewTabX = actionBar.newTab().setText("Übersicht");
        WorkTabX = actionBar.newTab().setText("Arbeit");
        StoreTabX = actionBar.newTab().setText("Kaufen");


        // Setting tab listeners.
        OverviewTabX.setTabListener(new TabListener(OverviewFragmentTab));
        WorkTabX.setTabListener(new TabListener(WorkFragmentTab));
        StoreTabX.setTabListener(new TabListener(StoreFragmentTab));

        // Adding tabs to the ActionBar.
        actionBar.addTab(OverviewTabX);
        actionBar.addTab(WorkTabX);
        actionBar.addTab(StoreTabX);

        context = this;

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
                                    OverviewTab.textView_feed_percent.setText(MyService.getInt_feed() + "");
                                    OverviewTab.seekbar_feed.setProgress(Integer.parseInt(MyService.getInt_feed()+""));
                                    OverviewTab.textView_thirst_percent.setText(MyService.getInt_thirst()+"");
                                    OverviewTab.seekbar_thirst.setProgress(Integer.parseInt(MyService.getInt_thirst()+""));
                                    OverviewTab.textView_fun_percent.setText(MyService.getInt_fun()+"");
                                    OverviewTab.seekbar_fun.setProgress(Integer.parseInt(MyService.getInt_fun()+""));
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


}
