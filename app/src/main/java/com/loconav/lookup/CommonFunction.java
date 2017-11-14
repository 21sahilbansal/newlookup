package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by prateek on 14/11/17.
 */

public class CommonFunction {

    public boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                currentField.setError("Cannot Be Empty");
                currentField.requestFocus();
                return false;
            }
        }
        return true;
    }

    public void sendAppMsg(Context context, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = message;

        // change with required  application package
        intent.setPackage("com.whatsapp");

        if (intent != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);//
            context.startActivity(Intent.createChooser(intent, text));
        } else {
            Toast.makeText(context , "Whatapp not found", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void setData(EditText editText) {
        editText.setTextColor(Color.BLACK);
        editText.setEnabled(false);
    }
    public void setDeviceId(EditText sim, EditText imei, String deviceId) {
        if(deviceId.length() == 12) {
            sim.setText(deviceId);
            setData(sim);
        } else {
            imei.setText(deviceId);
            setData(imei);
        }
    }
}
