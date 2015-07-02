package com.example.steffen.myapplication;

/**
 * Created by Steffen on 02.07.2015.
 */
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class OverviewTab extends Fragment {

    public static TextView textView_feed, textView_feed_percent, textView_thirst, textView_thirst_percent, textView_fun, textView_fun_percent, textView_mood;
    static SeekBar seekbar_feed, seekbar_thirst, seekbar_fun;
    Button button_feed, button_thirst, button_fun;
    public static int timeSec = 0;
    public static int maxValue = 1000;
    final int addValue = maxValue / 10;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    Handler mHandler;
    Thread t;


    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.overview_layout, container, false);

        textView_feed = (TextView) rootView.findViewById(R.id.textView_feed);
        textView_feed = (TextView) rootView.findViewById(R.id.textView_feed);
        textView_feed_percent = (TextView) rootView.findViewById(R.id.textView_feed_percent);
        textView_thirst = (TextView) rootView.findViewById(R.id.textView_thirst);
        textView_thirst_percent = (TextView) rootView.findViewById(R.id.textView_thirst_percent);
        textView_fun = (TextView) rootView.findViewById(R.id.textView_fun);
        textView_fun_percent = (TextView) rootView.findViewById(R.id.textView_fun_percent);
        textView_mood = (TextView) rootView.findViewById(R.id.textView_mood);

        button_feed = (Button) rootView.findViewById(R.id.button_feed);
        button_thirst = (Button) rootView.findViewById(R.id.button_thirst);
        button_fun = (Button) rootView.findViewById(R.id.button_fun);

        seekbar_feed = (SeekBar) rootView.findViewById(R.id.seekBar_feed);
        seekbar_thirst = (SeekBar) rootView.findViewById(R.id.seekBar_thirst);
        seekbar_fun= (SeekBar) rootView.findViewById(R.id.seekBar_fun);





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
        return rootView;
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


}
