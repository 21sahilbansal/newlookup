package com.loconav.lookup;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.MESSENGER_SCANNED_ID;
import static com.loconav.lookup.Constants.USER_ID;

public class LookUpEntry extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout ;
    ViewPager viewPager ;
    FragmentAdapterClass fragmentAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        tabLayout.addTab(tabLayout.newTab().setText("DEVICE"));
        tabLayout.addTab(tabLayout.newTab().setText("FAST TAG"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {

                viewPager.setCurrentItem(LayoutTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });
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
}

//
//
//