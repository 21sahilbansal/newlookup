package com.loconav.lookup.sharedetailsfragmants;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loconav.lookup.CustomActionBar;
import com.loconav.lookup.EnterDetails;
import com.loconav.lookup.R;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.SimchangeBinding;
import com.loconav.lookup.model.Client;

import static com.loconav.lookup.Constants.USER_ID;

/**
 * Created by prateek on 13/11/17.
 */

public class SimChange extends Fragment {
    private SimchangeBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.simchange, vg, false);

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunction.validate(new EditText[]{binding.ownerName, binding.vehicleNo,
                        binding.oldSimNo, binding.newSimNo, binding.imei, binding.clientId})) {
                    String message = "Sim Change" + "\n";
                    message += "Owner's name: "+ binding.ownerName.getText().toString() + "\n";
                    message += "Vehicle  no.: "+ binding.vehicleNo.getText().toString() + "\n";
                    message += "Old Sim No: "+ binding.oldSimNo.getText().toString() + "\n";
                    message += "New Sim No: "+ binding.newSimNo.getText().toString() + "\n";
                    message += "IMEI: " + binding.imei.getText().toString()+ "\n";
                    message += "Client ID: "+ binding.clientId.getText().toString()+"\n";
                    message += "USER ID: " + SharedPrefHelper.getInstance(getContext()).getStringData(USER_ID)+  "\n";
                    message += "Sent By Device Checker:"+ " " + System.currentTimeMillis() ;
                    CommonFunction.sendAppMsg(getActivity(), message);
                }
            }
        });
        String deviceId = ((EnterDetails)getActivity()).getDeviceID();
        Client client = ((EnterDetails)getActivity()).getClient();
        CommonFunction.setEditText(binding.imei, deviceId);
        CommonFunction.setEditText(binding.ownerName, client.getName());
        CommonFunction.setEditText(binding.clientId, client.getClientId());
        return binding.getRoot();
    }
}
