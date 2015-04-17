package com.yokmama.learn10.chapter07.lesson34.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.yokmama.learn10.chapter07.lesson34.R;

/**
 * Created by kayo on 15/04/15.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class FragmentTransitionsActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_transitions);

        findViewById(R.id.btn_add_fragment_explode).setOnClickListener(this);
        findViewById(R.id.btn_add_fragment_fade).setOnClickListener(this);
        findViewById(R.id.btn_add_fragment_slide).setOnClickListener(this);
        findViewById(R.id.btn_add_fragment_none).setOnClickListener(this);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransitionsFragment newFrg = FragmentTransitionsFragment.newInstance(0, 0);
            fm.beginTransaction().replace(R.id.container, newFrg).commit();
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransitionsFragment f = (FragmentTransitionsFragment) fm.findFragmentById(R.id.container);
        FragmentTransitionsFragment newFrg = FragmentTransitionsFragment.newInstance(f.getPage() + 1, v.getId());

        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, newFrg)
                .commit();
    }

}
