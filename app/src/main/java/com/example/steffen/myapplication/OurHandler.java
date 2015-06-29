package com.example.steffen.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Steffen on 29.06.2015.
 */
public class OurHandler extends Handler {
    public OurHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int startId = msg.arg1;
        Object someObject = msg.obj;
        // Do some processing


        MainActivity.timeSec++;
        MainActivity.timer.setText(MainActivity.timeSec + ":000");


        //boolean stopped = stopSelfResult(startId);
        // stopped is true if the service is stopped
    }
}
