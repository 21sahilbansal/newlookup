package com.loconav.lookup.sharedetailsfragmants;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.EnterDetails;
import com.loconav.lookup.R;
import com.loconav.lookup.databinding.DevicechangeBinding;
import com.loconav.lookup.model.Client;

import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.application.LookUpApplication.sharedPreferences;

/**
 * Created by prateek on 13/11/17.
 */

public class DeviceChange extends Fragment {

    private DevicechangeBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.devicechange, vg, false);

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunction.validate(new EditText[]{binding.ownerName, binding.vehicleNo,
                        binding.oldImei, binding.newImei, binding.oldSimNo, binding.newSimNo, binding.deviceModel, binding.clientId})) {
                    String message = "Device Change" + "\n";
                    message += "Owner's name: "+ binding.ownerName.getText().toString() + "\n";
                    message += "Vehicle  no.: "+ binding.vehicleNo.getText().toString() + "\n";
                    message += "Old Imei No: "+ binding.oldImei.getText().toString() + "\n";
                    message += "New Imei No: "+ binding.newImei.getText().toString() + "\n";
                    message += "Old Sim No: "+ binding.oldSimNo.getText().toString() + "\n";
                    message += "New Sim No: "+ binding.newSimNo.getText().toString() + "\n";
                    message += "Device Model: " + binding.deviceModel.getText().toString()+ "\n";
                    message += "Client ID: " + binding.clientId.getText().toString()+ "\n";
                    message += "USER ID: " + sharedPreferences.getString(USER_ID, "") +  "\n";
                    message += "Sent By Device Checker:"+ " " + System.currentTimeMillis();
                    CommonFunction.sendAppMsg(getActivity(), message);
                }
            }
        });
        String deviceId = ((EnterDetails)getActivity()).getDeviceID();
        Client client = ((EnterDetails)getActivity()).getClient();
        CommonFunction.setEditText(binding.newImei , deviceId);
        CommonFunction.setEditText(binding.ownerName, client.getName());
        CommonFunction.setEditText(binding.clientId, client.getClientId());
        return binding.getRoot();
    }
}
