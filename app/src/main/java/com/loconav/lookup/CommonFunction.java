package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.loconav.lookup.Constants.USER_ID;

/**
 * Created by prateek on 14/11/17.
 */

public class CommonFunction {

    public static boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                currentField.setError("Cannot Be Empty");
                currentField.requestFocus();
                return false;
            } else if(currentField.getId() == R.id.client_id) {
                if(currentField.getText().length() < 4) {
                    currentField.setError("Cannot Be Less Than 4");
                    currentField.requestFocus();
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean validateEdit(EditText fields){
            EditText currentField = fields;
            if(currentField.getText().toString().length() <= 0){
                currentField.setError("Cannot Be Empty");
                currentField.requestFocus();
                return false;
            }
        return true;
    }
    private static void setData(EditText editText) {
        editText.setEnabled(false);
        editText.clearFocus();
    }
    public static void setEditText( EditText imei, String deviceId) {
        imei.setText(deviceId);
        setData(imei);
    }
}
