package com.loconav.lookup.sharedetailsfragmants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.CustomActionBar;
import com.loconav.lookup.R;
import com.loconav.lookup.ShareAndUpload;

/**
 * Created by prateek on 13/11/17.
 */

public class NewInstallation extends Fragment {
    CommonFunction commonFunction;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        CustomActionBar customActionBar = new CustomActionBar();
        customActionBar.getActionBar((AppCompatActivity)getActivity(),
                R.drawable.leftarrow,R.string.new_installation,true);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        commonFunction = new CommonFunction();
        View view = inflater.inflate(R.layout.newinstallation, vg, false);
        final EditText dealer_name = (EditText)view.findViewById(R.id.dealer_name);
        final EditText ownerName = (EditText)view.findViewById(R.id.owner_name);
        final EditText contact_no = (EditText)view.findViewById(R.id.contact_no);
        final EditText location = (EditText)view.findViewById(R.id.location);
        final EditText registration_no = (EditText)view.findViewById(R.id.registration_no);
        final EditText chassis_no = (EditText) view.findViewById(R.id.chassis_no);
        final EditText manufacture = (EditText) view.findViewById(R.id.manufacture);
        final EditText model = (EditText) view.findViewById(R.id.model);
        final EditText type_of_goods = (EditText) view.findViewById(R.id.type_of_goods);
        final EditText odometer_reading = (EditText) view.findViewById(R.id.odometer_reading);
        final EditText sim_no = (EditText) view.findViewById(R.id.sim_no);
        final EditText imei = (EditText) view.findViewById(R.id.imei);
        final EditText device_model = (EditText) view.findViewById(R.id.device_model);
        final RadioGroup radioSexGroup = (RadioGroup)view.findViewById(R.id.radioGroup1);
        final Button share = (Button) view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                if(selectedId != R.id.new_customer && selectedId != R.id.old_customer){
                    Toast.makeText(getContext(), "Please Select Customer New or Old",Toast.LENGTH_LONG).show();
                    radioSexGroup.setFocusable(true);
                }
                if(commonFunction.validate(new EditText[]{dealer_name, ownerName,
                        contact_no, location, registration_no, chassis_no, manufacture,
                        model, type_of_goods, odometer_reading, sim_no, imei, device_model })&& selectedId!=-1) {

                    String message = "";
                    if(selectedId == R.id.new_customer){
                        message += "New Customer" + "\n";
                    }else{
                        message += "Old Customer" + "\n";
                    }
                    message += "Dealer's name: "+dealer_name.getText().toString() + "\n";
                    message += "Owner's name: "+ownerName.getText().toString() + "\n";
                    message += "Contact no: "+contact_no.getText().toString() + "\n";
                    message += "Location: "+location.getText().toString() + "\n";
                    message += "Registration no.: "+registration_no.getText().toString()+ "\n";
                    message += "Chassis no: "+chassis_no.getText().toString() + "\n";
                    message += "Manufacture: "+manufacture.getText().toString() + "\n";
                    message += "Model: "+model.getText().toString() + "\n";
                    message += "Type of goods: "+type_of_goods.getText().toString() + "\n";
                    message += "Odometer Reading: "+odometer_reading.getText().toString() + "\n";
                    message += "Sim No: "+sim_no.getText().toString() + "\n";
                    message += "IMEI: "+imei.getText().toString() + "\n";
                    message += "Device Model: "+device_model.getText().toString()+"\n";
                    message += "Sent By Device Checker";
                    String url = "http://www.loconav.com/?type=new_vehicle&model="+model.getText().toString()+"&manufacturer="+manufacture.getText().toString()+"&deviceid="+sharedpreferences.getString("deviceid","");
                    editor.putString("message", message);
                    editor.putString("upload_url", url);
                    editor.commit();
                    Intent intent = new Intent(getContext(), ShareAndUpload.class);
                    startActivity(intent);
                }

            }
        });

        String deviceId = sharedpreferences.getString("deviceid","");
        commonFunction.setDeviceId(sim_no, imei, deviceId);
        return view;
    }
}
