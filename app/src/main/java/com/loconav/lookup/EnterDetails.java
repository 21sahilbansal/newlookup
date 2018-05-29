package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loconav.lookup.model.Client;
import com.loconav.lookup.sharedetailsfragmants.DeviceChange;
import com.loconav.lookup.sharedetailsfragmants.NewInstallation;
import com.loconav.lookup.sharedetailsfragmants.SimChange;
import com.loconav.lookup.sharedetailsfragmants.VehicleChange;

public class EnterDetails extends AppCompatActivity {
    CustomActionBar customActionBar ;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private String deviceID;
    private Client client;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);
        customActionBar = new CustomActionBar();
        customActionBar.getActionBar(this, R.drawable.leftarrow, R.string.enter_details, true);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        Bundle bundle = getIntent().getExtras();
        deviceID = getIntent().getStringExtra(Constants.DEVICE_ID);
        client = (Client)getIntent().getSerializableExtra("client");
        if(bundle != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            String type = bundle.getString("type");
            switch (type) {
                case "new_installation": {
                    NewInstallation f1 = new NewInstallation();
                    fragmentTransaction.replace(android.R.id.content, f1);

                    break;
                }
                case "sim_change": {
                    SimChange f1 = new SimChange();
                    fragmentTransaction.replace(android.R.id.content, f1);

                    break;
                }
                case "device_change": {
                    DeviceChange f1 = new DeviceChange();
                    fragmentTransaction.replace(android.R.id.content, f1);

                    break;
                }
                case "vehicle_change": {
                    VehicleChange f1 = new VehicleChange();
                    fragmentTransaction.replace(android.R.id.content, f1);
                    break;
                }
            }
            fragmentTransaction.commit();
        }
    }

    public String getDeviceID(){
        return deviceID;
    }

    public Client getClient() { return client; }

}
