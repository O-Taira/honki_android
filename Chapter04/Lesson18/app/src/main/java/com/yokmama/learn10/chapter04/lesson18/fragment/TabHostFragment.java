package com.yokmama.learn10.chapter04.lesson18.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.yokmama.learn10.chapter04.lesson18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabHostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tab_host, container, false);

        FragmentTabHost host = (FragmentTabHost)rootView.findViewById(R.id.tabHost);
        host.setup(getActivity(), getFragmentManager(), R.id.content);

        TabHost.TabSpec tabSpec1 = host.newTabSpec("List").setIndicator("List");
        host.addTab(tabSpec1, ListViewFragment.class, null);

        TabHost.TabSpec tabSpec2 = host.newTabSpec("Grid").setIndicator("Grid");
        host.addTab(tabSpec2, GridViewFragment.class, null);

        TabHost.TabSpec tabSpec3 = host.newTabSpec("Scroll").setIndicator("Scroll");
        host.addTab(tabSpec3, ScrollViewFragment.class, null);

        return rootView;
    }


}