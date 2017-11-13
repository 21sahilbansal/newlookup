package com.loconav.lookup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String Json_string;
    EditText e1;
    CustomActionBar customActionBar;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    private final String deviceId = "deviceid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customActionBar = new CustomActionBar();
        customActionBar.getActionBar(this, R.drawable.lookup_icon,R.string.title_activity_main_activity3,
                false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
//        ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
//        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        bar.setCustomView(R.layout.action);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        e1=(EditText)findViewById(R.id.editText4);

        Button button2 = (Button) findViewById(R.id.button2);
        if(sharedpreferences.getString("upload_url", "").equals("")){
            button2.setVisibility(View.GONE);
        }else{
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ShareAndUpload.class);
                    startActivity(intent);
                }
            });
        }
    }


    public	void getJson(View v) {
        String no = e1.getText().toString();
        if(no.equals("")){
            Toast.makeText(getApplicationContext(), "Enter Device ID", Toast.LENGTH_LONG).show();
        }else{
            if (no.length() == 10) {
                Intent intent = new Intent(this, MainActivity3.class);
                intent.putExtra("message", "00"+e1.getText().toString());
                editor.putString(deviceId , "00"+e1.getText().toString());
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, MainActivity3.class);
                intent.putExtra("message", "0" + e1.getText().toString());
                editor.putString(deviceId, "0" + e1.getText().toString());
                startActivity(intent);
            }
            editor.commit();
        }
    }

}
