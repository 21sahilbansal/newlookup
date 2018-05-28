package com.loconav.lookup.sharedetailsfragmants;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.CustomActionBar;
import com.loconav.lookup.EnterDetails;
import com.loconav.lookup.R;

import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.application.LookUpApplication.sharedPreferences;

/**
 * Created by prateek on 13/11/17.
 */

public class DeviceChange extends Fragment {

    EditText ownerName, vehicleNo, oldImei, newImei, oldSimNo,
            newSimNo, deviceModel, clientID;
    Button share;
    CommonFunction commonFunction;
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        CustomActionBar customActionBar = new CustomActionBar();
        customActionBar.getActionBar((AppCompatActivity)getActivity(),R.drawable.leftarrow,R.string.device_change,true);
        View view = inflater.inflate(R.layout.devicechange, vg, false);
        ownerName = (EditText)view.findViewById(R.id.owner_name);
        vehicleNo = (EditText)view.findViewById(R.id.vehicle_no);
        oldImei = (EditText)view.findViewById(R.id.old_imei);
        newImei = (EditText)view.findViewById(R.id.new_imei);
        oldSimNo = (EditText)view.findViewById(R.id.old_sim_no);
        newSimNo = (EditText)view.findViewById(R.id.new_sim_no);
        deviceModel = (EditText)view.findViewById(R.id.device_model);
        clientID = (EditText)view.findViewById(R.id.client_id);
        share = (Button)view.findViewById(R.id.share);
        commonFunction = new CommonFunction();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commonFunction.validate(new EditText[]{ownerName, vehicleNo,
                        oldImei, newImei, oldSimNo, newSimNo, deviceModel, clientID})) {
                    String message = "Device Change" + "\n";
                    message += "Owner's name: "+ ownerName.getText().toString() + "\n";
                    message += "Vehicle  no.: "+ vehicleNo.getText().toString() + "\n";
                    message += "Old Imei No: "+ oldImei.getText().toString() + "\n";
                    message += "New Imei No: "+ newImei.getText().toString() + "\n";
                    message += "Old Sim No: "+ oldSimNo.getText().toString() + "\n";
                    message += "New Sim No: "+ newSimNo.getText().toString() + "\n";
                    message += "Device Model: " + deviceModel.getText().toString()+ "\n";
                    message += "Client ID: " + clientID.getText().toString()+ "\n";
                    message += "USER ID: " + sharedPreferences.getString(USER_ID, "") +  "\n";
                    message += "Sent By Device Checker:"+ " " + System.currentTimeMillis();
                    commonFunction.sendAppMsg(getActivity(), message);
                }
            }
        });
        String deviceId = ((EnterDetails)getActivity()).getDeviceID();
        commonFunction.setDeviceId(newSimNo, newImei, deviceId);
        return view;
    }
}
