package com.loconav.lookup;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;

import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.Utility.isStringEmptyOrNull;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
    EditText userID;
    Button submit;
    SharedPrefHelper sharedPrefHelper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userID = (EditText) findViewById(R.id.user_id1);
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
        userID.setText(SharedPrefHelper.getInstance(getBaseContext()).getStringData(USER_ID));
    }

    @Override
    public void onClick(View view) {
        if(isStringEmptyOrNull(userID.getText().toString())) {
            Toast.makeText(getBaseContext(), getString(R.string.enter_user_id), Toast.LENGTH_LONG).show();
        } else {
          //  editor.putString(USER_ID, userID.getText().toString());
            sharedPrefHelper.setStringData(USER_ID, userID.getText().toString());
           // editor.commit();
            finish();
        }
    }
}
