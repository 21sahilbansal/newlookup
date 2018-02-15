package com.loconav.lookup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
        setContentView(R.layout.activity_main_lookup);
        customActionBar = new CustomActionBar();
        customActionBar.getActionBar(this, R.drawable.lookup_icon,R.string.title_activity_main_activity3,
                false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        if(!isUserIdSet()){
            showEnterIdDialog();
        } 

//        ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
//        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        bar.setCustomView(R.layout.lookup_action);
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

    private void showEnterIdDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter User Id");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().trim().equals("")) {
                    editor.putString("user_id",input.getText().toString());
                    dialog.cancel();
                } else {
                    Toast.makeText(getBaseContext(), "User Id can't be Empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setCancelable(false);

        builder.show();
    }

    private boolean isUserIdSet() {
        if(!sharedpreferences.getString("user_id", "").equals("")) {
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
            intent.putExtra("message", e1.getText().toString());
            editor.putString(deviceId , e1.getText().toString());
            startActivity(intent);
            editor.commit();
        }
    }

}
