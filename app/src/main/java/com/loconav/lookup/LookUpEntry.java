package com.loconav.lookup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.application.LookUpApplication.editor;
import static com.loconav.lookup.application.LookUpApplication.sharedPreferences;

public class LookUpEntry extends AppCompatActivity {
    String Json_string;
    EditText e1;
    CustomActionBar customActionBar;
    private final String deviceId = "deviceid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lookup);
        customActionBar = new CustomActionBar();
        customActionBar.getActionBar(this, R.drawable.lookup_icon,R.string.title_activity_main_activity3,
                false);
        if(!isUserIdSet()){
            showEnterIdDialog();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        e1=(EditText)findViewById(R.id.editText4);

        Button button2 = (Button) findViewById(R.id.button2);
        if(sharedPreferences.getString("upload_url", "").equals("")){
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

    private void showEnterIdDialog() {

        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Phone Number");
        builder.setPositiveButton("OK", null);
        builder.setView(input);
        builder.setCancelable(false);

        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setTextColor(Color.BLACK);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if(!input.getText().toString().trim().equals("")) {
                            editor.putString(Constants.USER_ID ,input.getText().toString());
                            editor.commit();
                            mAlertDialog.cancel();
                        } else {
                            Toast.makeText(getBaseContext(), "User Id can't be Empty", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    private boolean isUserIdSet() {
        if(!sharedPreferences.getString(Constants.USER_ID, "").equals("")) {
            return true;
        } else
            return false;
    }


    public	void getJson(View v) {
        String no = e1.getText().toString();
        if(no.equals("")){
            Toast.makeText(getApplicationContext(), "Enter Device ID", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, MainActivity3.class);
            intent.putExtra(DEVICE_ID, e1.getText().toString());
            editor.putString(deviceId , e1.getText().toString());
            startActivity(intent);
            editor.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
