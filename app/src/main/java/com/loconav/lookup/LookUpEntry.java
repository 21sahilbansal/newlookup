package com.loconav.lookup;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.ReasonResponse;

import java.util.ArrayList;
import java.util.List;

import static com.loconav.lookup.Constants.IS_LOGGED_IN;
import static com.loconav.lookup.Constants.REASONS_RESPONSE;

public class LookUpEntry extends BaseActivity {

    TabLayout tabLayout ;
    ViewPager viewPager ;
    FragmentAdapterClass fragmentAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_up_entry);
            getSupportActionBar().setElevation(0);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);
        fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(),2);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.e("look up entry ", "onCreate: ");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                Intent intent = new Intent(getBaseContext(), UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                    WhatToDo tab1 = new WhatToDo();
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

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Device";
                case 1:
                    return "Fastag";
                default:
                    return super.getPageTitle(position);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("look up entry ", "onResume: " );
    }
}
