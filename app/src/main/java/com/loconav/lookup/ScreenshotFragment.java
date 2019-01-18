package com.loconav.lookup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentScreenshotBinding;
import com.loconav.lookup.model.InstallationDetails;
import com.loconav.lookup.utils.AppUtils;
import com.loconav.lookup.utils.TimeUtils;

import static com.loconav.lookup.UserPrefs.code;
import static com.loconav.lookup.UserPrefs.name;

public class ScreenshotFragment extends BaseFragment {
    FragmentScreenshotBinding binding;
    String textToWhatsapp;
    @Override
    public int setViewId() {
        return R.layout.fragment_screenshot;
    }

    @Override
    public void onFragmentCreated() {
        InstallationDetails installationDetails=(InstallationDetails) getActivity().getIntent().getExtras().getSerializable(getString(R.string.installation_details));
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.stamp_animation);
        binding.done.setAnimation(animation);
        binding.done.setVisibility(View.VISIBLE);
        setData(installationDetails);
        binding.shareOnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWhatsapp(textToWhatsapp);
            }
        });
        binding.checkInstallLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), BaseNavigationActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString(getString(R.string.fragment_name),getString(R.string.install_log_fragment));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    public void setData(InstallationDetails details)
    {
        binding.installerName.setText(SharedPrefHelper.getInstance().getStringData(name));
        binding.installerId.setText(SharedPrefHelper.getInstance().getStringData(code));
        binding.date.setText(TimeUtils.getDate(String.valueOf(details.getInstallation_date())));
        binding.deviceNumber.setText(details.getInstallable_serial_number());
        binding.phoneNo.setText(details.getDevice_phone_number());
        binding.truckNumber.setText(details.getTruck_number());
        textToWhatsapp="Installer Name:- "+SharedPrefHelper.getInstance().getStringData(name)
                        +"\nInstaller Id:- "+SharedPrefHelper.getInstance().getStringData(code)
                        +"\nInstallation Date:- "+TimeUtils.getDate(String.valueOf(details.getInstallation_date()))
                        +"\nTruck Number:-"+details.getTruck_number()
                        +"\nPhone Number:-"+details.getDevice_phone_number()
                        +"\nDevice IMEI Number:-"+details.getInstallable_serial_number()
                        +"\nUnique Id:-"+AppUtils.getMd5(details.getTruck_number()+""+details.getInstallable_serial_number());
    }

    private void sendWhatsapp(String message){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }

    @Override
    public void bindView(View view) {
        binding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}
