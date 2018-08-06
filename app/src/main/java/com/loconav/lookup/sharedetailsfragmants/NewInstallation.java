package com.loconav.lookup.sharedetailsfragmants;

import android.content.Intent;
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
import com.loconav.lookup.ShareAndUpload;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.NewinstallationBinding;
import com.loconav.lookup.model.Client;

import static com.loconav.lookup.Constants.USER_ID;

/**
 * Created by prateek on 13/11/17.
 */

public class NewInstallation extends Fragment {
    private NewinstallationBinding binding;
    SharedPrefHelper sharedPrefHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        sharedPrefHelper=SharedPrefHelper.getInstance(getContext());
        final String deviceId = ((EnterDetails)getActivity()).getDeviceID();
        final Client client = ((EnterDetails) getActivity()).getClient();

        binding = DataBindingUtil.inflate(inflater, R.layout.newinstallation, vg, false);
         binding.ownerName.setText(client.getName());

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunction.validate(new EditText[]{binding.dealerName,binding.ownerName,binding.clientId,binding.location, binding.registrationNo,binding.chassisNo, binding.manufacture,
                        binding.model, binding.typeOfGoods, binding.odometerReading, binding.simNo, binding.imei, binding.deviceModel})) {
                    String message = "";
                    message += "Dealer's name: "+ binding.dealerName.getText().toString() + "\n";
                    message += "Location: "+ binding.location.getText().toString() + "\n";
                    message += "Registration no.: "+ binding.registrationNo.getText().toString()+ "\n";
                    message += "Chassis no: "+ binding.chassisNo.getText().toString() + "\n";
                    message += "Manufacture: "+ binding.manufacture.getText().toString() + "\n";
                    message += "Model: "+ binding.model.getText().toString() + "\n";
                    message += "Client Id: "+ binding.clientId.getText().toString() + "\n";
                    message += "Owner Name: "+ binding.ownerName.getText().toString() + "\n";
                    message += "Type of goods: "+ binding.typeOfGoods.getText().toString() + "\n";
                    message += "Odometer Reading: "+ binding.odometerReading.getText().toString() + "\n";
                    message += "Sim No: "+ binding.simNo.getText().toString() + "\n";
                    message += "IMEI: "+ binding.imei.getText().toString() + "\n";
                    message += "Device Model: "+ binding.deviceModel.getText().toString()+"\n";
                    message += "USER ID: " + SharedPrefHelper.getInstance(getContext()).getStringData(USER_ID) +  "\n";
                    message += "Sent By Device Checker:"+ " " + System.currentTimeMillis() ;
                    String url = "http://www.loconav.com/?type=new_vehicle&model="+
                            binding.model.getText().toString()+"&manufacturer="+
                            binding.manufacture.getText().toString()+"&deviceid="+ deviceId;
                    sharedPrefHelper.setStringData("message", message);
                    sharedPrefHelper.setStringData("upload_url", url);
                    Intent intent = new Intent(getActivity(), ShareAndUpload.class);
                    startActivity(intent);
                }
            }
        });
        CommonFunction.setEditText(binding.imei, deviceId);
        CommonFunction.setEditText(binding.ownerName, client.getName());
        CommonFunction.setEditText(binding.clientId, client.getClientId());

        return binding.getRoot();
    }
}
