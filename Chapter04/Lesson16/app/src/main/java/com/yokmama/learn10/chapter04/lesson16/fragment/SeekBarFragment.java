package com.yokmama.learn10.chapter04.lesson16.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yokmama.learn10.chapter04.lesson16.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeekBarFragment extends Fragment {
    private TextView textView1;
    private TextView textView2;
    private SeekBar mSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_seek_bar, container, false);

        textView1 = (TextView)rootView.findViewById(R.id.textView1);
        textView2 = (TextView)rootView.findViewById(R.id.textView2);
        mSeekBar = (SeekBar)rootView.findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView1.setText("progress:"+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView2.setText("start :"+seekBar.getProgress());
                textView2.setTextColor(Color.RED);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView2.setText("stop :"+seekBar.getProgress());
                textView2.setTextColor(Color.BLACK);
            }
        });

        return rootView;
    }


}