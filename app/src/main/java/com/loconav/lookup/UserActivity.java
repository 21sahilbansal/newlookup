package com.loconav.lookup;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;

import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.UserPrefs.phoneNumber;
import static com.loconav.lookup.Utility.isStringEmptyOrNull;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText userID;
    private Button submit;
    SharedPrefHelper sharedPrefHelper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userID = (EditText) findViewById(R.id.user_id);
        submit = (Button) findViewById(R.id.submit);
        initSharedPf();
        attachClickListener();
        fillUserId();
    }

    private void initSharedPf() {
      sharedPrefHelper=SharedPrefHelper.getInstance(getBaseContext());
    }


    private void attachClickListener() {
        submit.setOnClickListener(this);
    }

    private void fillUserId() {
        CommonFunction.setEditText(userID,SharedPrefHelper.getInstance(getBaseContext()).getStringData(phoneNumber));
    }

    @Override
    public void onClick(View view) {
            finish();
    }
}
