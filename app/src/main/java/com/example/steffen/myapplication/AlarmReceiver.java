package com.example.steffen.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Steffen on 29.06.2015.
 */
public class AlarmReceiver extends BroadcastReceiver{

    int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "I'm running" + i, Toast.LENGTH_SHORT).show();
        i++;
    }
}
