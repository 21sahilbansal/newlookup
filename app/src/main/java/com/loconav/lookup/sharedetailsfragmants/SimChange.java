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

import com.loconav.lookup.CustomActionBar;
import com.loconav.lookup.R;
import com.loconav.lookup.CommonFunction;

/**
 * Created by prateek on 13/11/17.
 */

public class SimChange extends Fragment {
    EditText ownerName,vehicleNo, oldSimNo, newSimNo, imei;
    Button share;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    CommonFunction commonFunction = new CommonFunction();
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simchange, vg, false);
        CustomActionBar customActionBar = new CustomActionBar();
        customActionBar.getActionBar((AppCompatActivity)getActivity(),R.drawable.leftarrow,
                R.string.sim_change,true);
        ownerName = (EditText)view.findViewById(R.id.owner_name);
        vehicleNo = (EditText)view.findViewById(R.id.vehicle_no);
        oldSimNo = (EditText)view.findViewById(R.id.old_sim_no);
        newSimNo = (EditText)view.findViewById(R.id.new_sim_no);
        imei = (EditText)view.findViewById(R.id.imei);
        share = (Button)view.findViewById(R.id.share);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commonFunction.validate(new EditText[]{ownerName, vehicleNo,
                        oldSimNo, newSimNo, imei})) {
                    String message = "Sim Change" + "\n";
                    message += "Owner's name: "+ ownerName.getText().toString() + "\n";
                    message += "Vehicle  no.: "+ vehicleNo.getText().toString() + "\n";
                    message += "Old Sim No: "+ oldSimNo.getText().toString() + "\n";
                    message += "New Sim No: "+ newSimNo.getText().toString() + "\n";
                    message += "IMEI: " + imei.getText().toString();
                    commonFunction.sendAppMsg(getContext(), message);
//                }
                }

            }
        });
        String deviceId = sharedpreferences.getString("deviceid","");
        commonFunction.setDeviceId(newSimNo, imei, deviceId);
        return view;
    }

}
