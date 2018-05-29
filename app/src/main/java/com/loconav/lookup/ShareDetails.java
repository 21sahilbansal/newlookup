package com.loconav.lookup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loconav.lookup.model.Client;

public class ShareDetails extends AppCompatActivity {

    int selectedId;
    RadioGroup radioSexGroup;
    private String deviceId;
    private Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_details);
        client = (Client) getIntent().getSerializableExtra("client");
        deviceId = getIntent().getStringExtra(Constants.DEVICE_ID);
        radioSexGroup=(RadioGroup)findViewById(R.id.radioGroup1);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedId = radioSexGroup.getCheckedRadioButtonId();
                if(selectedId!=-1) {
                    Intent intent = new Intent(ShareDetails.this, EnterDetails.class);
                    switch (selectedId){
                        case R.id.radio0:
                            intent.putExtra("type","new_installation");
                            break;
                        case R.id.radio1:
                            intent.putExtra("type","sim_change");
                            break;
                        case R.id.radio2:
                            intent.putExtra("type","device_change");
                            break;
                        case R.id.radio3:
                            intent.putExtra("type","vehicle_change");
                            break;
                    }
                    intent.putExtra(Constants.DEVICE_ID, deviceId);
                    intent.putExtra("client", client);
                    startActivity(intent);
                } else
                    Toast.makeText(getBaseContext(), "Select one of the above options..", Toast.LENGTH_LONG).show();

            }
        });
    }
}
