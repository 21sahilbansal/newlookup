package com.loconav.lookup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentUserProfileBinding;
import com.loconav.lookup.login.SplashActivity;
import com.loconav.lookup.utils.AppUtils;

import static androidx.core.content.ContextCompat.getDrawable;
import static com.loconav.lookup.Constants.FRAGMENT_NAME;
import static com.loconav.lookup.Constants.IS_LOGGED_IN;
import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.UserPrefs.authenticationToken;
import static com.loconav.lookup.UserPrefs.code;
import static com.loconav.lookup.UserPrefs.location;
import static com.loconav.lookup.UserPrefs.name;
import static com.loconav.lookup.UserPrefs.phoneNumber;

public class UserProfileFragment extends BaseFragment {
    private SharedPrefHelper sharedPrefHelper ;
    private FragmentUserProfileBinding binding;
    @Override
    public int setViewId() {
        return R.layout.fragment_user_profile;
    }

    @Override
    public void onFragmentCreated() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("User Profile");
        initSharedPf();
        attachClickListener();
        fillUserId();


        binding.checkInstallLogs.setOnClickListener(v -> checkInstallLogs());
        binding.checkRepairLogs.setOnClickListener(v -> checkRepairLogs());
        binding.checkTurorials.setOnClickListener(v -> checkTutorials());


    }

    private void checkTutorials() {
        if(AppUtils.isNetworkAvailable()) {
            Intent i=new Intent(getContext(), BaseNavigationActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString(FRAGMENT_NAME,getString(R.string.tutorial_fragment));
            i.putExtras(bundle);
            startActivity(i);
        }
        else
        {
            Toaster.makeToast(getString(R.string.internet_not_available));
        }
    }

    private void initSharedPf() {
        sharedPrefHelper = SharedPrefHelper.getInstance();
    }

    private void attachClickListener() {
        binding.logout.setOnClickListener(view -> {
            AppUtils.logOut();
        });

    }

    private void fillUserId() {
        binding.userId.setText(SharedPrefHelper.getInstance().getStringData(code));
        binding.userName.setText(SharedPrefHelper.getInstance().getStringData(name));
        binding.userPhone.setText(SharedPrefHelper.getInstance().getStringData(phoneNumber));
    }

    private void checkRepairLogs()
    {
        if(AppUtils.isNetworkAvailable()) {
            Intent i=new Intent(getContext(), BaseNavigationActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString(FRAGMENT_NAME,getString(R.string.repair_log_fragment));
            i.putExtras(bundle);
            startActivity(i);
        }
        else
        {
            Toaster.makeToast(getString(R.string.internet_not_available));
        }
    }

    private void checkInstallLogs()
    {
        if(AppUtils.isNetworkAvailable()) {
            Intent i=new Intent(getContext(), BaseNavigationActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString(FRAGMENT_NAME,getString(R.string.install_log_fragment));
            i.putExtras(bundle);
            startActivity(i);
        }
        else
        {
            Toaster.makeToast(getString(R.string.internet_not_available));
        }
    }

    @Override
    public void bindView(View view) {
        binding=DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }
}
