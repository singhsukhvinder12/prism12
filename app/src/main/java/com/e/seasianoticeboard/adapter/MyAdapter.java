package com.e.seasianoticeboard.adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.e.seasianoticeboard.view.core.fragment.CorridorFragment;
import com.e.seasianoticeboard.view.core.fragment.ChoiceFragment;

public class MyAdapter extends FragmentPagerAdapter {

     private Context myContext;
    private int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }


    @Override
    public int getCount() {
        return totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CorridorFragment();
            case 1:
                return new ChoiceFragment();

            default:
                return null;
        }
    }
}