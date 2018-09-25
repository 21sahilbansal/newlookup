package com.loconav.lookup;


import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.LookUpEntryBinding;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.VersionResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.sharedetailsfragmants.SimChangeFragment;

import retrofit2.Call;
import retrofit2.Response;

public class LookUpEntry extends BaseActivity {

    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private FragmentAdapterClass fragmentAdapter ;
    private LookUpEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.look_up_entry);
//        To remove shadow partition between tabs and title bar.
        getSupportActionBar().setElevation(0);
        fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(),2);
        binding.pager.setAdapter(fragmentAdapter);
        binding.tabLayout.setupWithViewPager(binding.pager);
        checkVersion();
    }


    private void checkVersion() {
        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Utility.isNetworkAvailable(this)) {
            apiService.getVersion((int) Float.parseFloat(version)).enqueue(new RetrofitCallback<VersionResponse>() {
                @Override
                public void handleSuccess(Call<VersionResponse> call, Response<VersionResponse> response) {
                    if (response.body().getForce_update()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LookUpEntry.this, R.style.DialogTheme);
                        builder.setMessage("Update App                                        ")
                                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse("http://play.loconav.com");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }

                @Override
                public void handleFailure(Call<VersionResponse> call, Throwable t) {
                    Log.e("ss", "handleFailure: ");
                }
            });
        } else
            Toast.makeText(getBaseContext(), "Internet not available", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        Log.e("ss", "onBackPressed: "+ Utility.isNetworkAvailable(this));
        if (binding.pager.getCurrentItem() != 0) {
            binding.pager.setCurrentItem(binding.pager.getCurrentItem() - 1,false);
        }else{
            finish();
        }

    }

    @Override
    public boolean showBackButton() {
        return false;
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
        FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WhatToDo();
                case 1:
                    return new FastTagFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), LocationService.class));
        } else {
            if(!isMyServiceRunning(LocationService.class)) {
                Log.e(getClass().getSimpleName(), "onReceive: " + "service restarted");
                startService(new Intent(getApplicationContext(), LocationService.class));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
