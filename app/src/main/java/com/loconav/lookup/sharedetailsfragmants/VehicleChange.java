package com.loconav.lookup.sharedetailsfragmants;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class VehicleChange extends Fragment {
    EditText ownerName, imei, oldVehicleNo, newVehicleNo, simNo, clientID;
    Button share;
    CommonFunction commonFunction;
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        CustomActionBar customActionBar = new CustomActionBar();
        customActionBar.getActionBar((AppCompatActivity)getActivity(),R.drawable.leftarrow,
                R.string.vehicle_change,true);
        commonFunction = new CommonFunction();
        View view = inflater.inflate(R.layout.vehiclechange, vg, false);
        ownerName = (EditText)view.findViewById(R.id.owner_name);
        imei = (EditText)view.findViewById(R.id.imei);
        oldVehicleNo = (EditText)view.findViewById(R.id.old_vehicle_no);
        newVehicleNo = (EditText)view.findViewById(R.id.new_vehicle_no);
        simNo = (EditText)view.findViewById(R.id.sim_no);
        clientID = (EditText)view.findViewById(R.id.client_id);
        share = (Button)view.findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commonFunction.validate(new EditText[]{ownerName, imei, oldVehicleNo,
                        newVehicleNo, simNo, clientID})) {
                    String message = "Sim Change" + "\n";
                    message += "Owner's name: "+ ownerName.getText().toString() + "\n";
                    message += "IMEI: "+ imei.getText().toString() + "\n";
                    message += "Old Vehicle No: "+ oldVehicleNo.getText().toString() + "\n";
                    message += "New Vehicle No: "+ newVehicleNo.getText().toString() + "\n";
                    message += "Sim no.: " + simNo.getText().toString()+ "\n";
                    message += "Client ID: "+ clientID.getText().toString()+"\n";
                    message += "USER ID: " + sharedPreferences.getString(USER_ID, "") + "\n";
                    message += "Sent By Device Checker:"+ " " + System.currentTimeMillis() ;
                    commonFunction.sendAppMsg(getActivity(), message);
                }

            }
        });
        String deviceId = ((EnterDetails)getActivity()).getDeviceID();
        commonFunction.setDeviceId(simNo, imei, deviceId);
        return view;
    }
}
