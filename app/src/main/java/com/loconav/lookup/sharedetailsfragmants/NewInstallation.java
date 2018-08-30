package com.loconav.lookup.sharedetailsfragmants;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.ShareAndUpload;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.NewinstallationBinding;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.PassingReason;

import static com.loconav.lookup.FragmentController.loadFragment;
import static com.loconav.lookup.UserPrefs.code;
import static com.loconav.lookup.UserPrefs.phoneNumber;

/**
 * Created by prateek on 13/11/17.
 */

public class NewInstallation extends BaseTitleFragment {
    private NewinstallationBinding binding;
    private SharedPrefHelper sharedPrefHelper;
    private PassingReason passingReason;

    @Override
    public int setViewId() {
        return R.layout.newinstallation;
    }

    @Override
    public void onFragmentCreated() {
        sharedPrefHelper=SharedPrefHelper.getInstance(getContext());
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        final String deviceId = passingReason.getDeviceid();
        final Client client = passingReason.getClientId();
        binding.ownerName.setText(client.getName());
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunction.validate(new EditText[]{binding.dealerName,binding.ownerName,binding.clientId,binding.location, binding.registrationNo,binding.chassisNo, binding.manufacture,
                        binding.model, binding.typeOfGoods, binding.odometerReading, binding.simNo, binding.imei, binding.deviceModel})) {
                    String message = "";
                    message += "Dealer's name: "+ binding.dealerName.getText().toString() + "\n";
                    message += "Owner Name: "+ binding.ownerName.getText().toString() + "\n";
                    message += "Client Id: "+ binding.clientId.getText().toString() + "\n";
                    message += "Location: "+ binding.location.getText().toString() + "\n";
                    message += "Registration no.: "+ binding.registrationNo.getText().toString()+ "\n";
                    message += "Chassis no: "+ binding.chassisNo.getText().toString() + "\n";
                    message += "Manufacture: "+ binding.manufacture.getText().toString() + "\n";
                    message += "Model: "+ binding.model.getText().toString() + "\n";
                    message += "Type of goods: "+ binding.typeOfGoods.getText().toString() + "\n";
                    message += "Odometer Reading: "+ binding.odometerReading.getText().toString() + "\n";
                    message += "Sim No: "+ binding.simNo.getText().toString() + "\n";
                    message += "IMEI: "+ binding.imei.getText().toString() + "\n";
                    message += "Device Model: "+ binding.deviceModel.getText().toString()+"\n";
                    message += "SOS: " + getFeatures(binding.cbSos) +  "\n";
                    message += "Trip Button: " + getFeatures(binding.cbTrip) +  "\n";
                    message += "Immobilizer: " + getFeatures(binding.cbImm) +  "\n";
                    message += "USER ID: " + SharedPrefHelper.getInstance(getContext()).getStringData(code) +  "\n";
                    message += "Sent By Device Checker:"+ " " + System.currentTimeMillis() ;
                    String url = "http://www.loconav.com/?type=new_vehicle&model="+
                            binding.model.getText().toString()+"&manufacturer="+
                            binding.manufacture.getText().toString()+"&deviceid="+ deviceId;
                    sharedPrefHelper.setStringData("message", message);
                    sharedPrefHelper.setStringData("upload_url", url);
                    ShareAndUpload f1 = new ShareAndUpload();
                    loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
                }
            }
        });
        CommonFunction.setEditText(binding.imei, deviceId);
        CommonFunction.setEditText(binding.ownerName, client.getName());
        CommonFunction.setEditText(binding.clientId, client.getClientId());
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }


    private String getFeatures(CheckBox checkBox) {
        if(checkBox.isChecked())
            return "YES";
        else
            return "NO";
    }

    @Override
    public String getTitle() {
        return "New Installations";
    }
}
