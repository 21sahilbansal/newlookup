package com.loconav.lookup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sejal on 28-06-2018.
 */

public class FragmentAdapterClass extends FragmentStatePagerAdapter {

    int TabCount;

    public FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {

        super(fragmentManager);

        this.TabCount = CountTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DeviceIdFragment tab1 = new DeviceIdFragment();
                return tab1;

            case 1:
                FastTagFragment tab2 = new FastTagFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}