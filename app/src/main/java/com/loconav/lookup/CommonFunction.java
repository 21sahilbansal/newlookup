package com.loconav.lookup;

import android.widget.EditText;

/**
 * Created by prateek on 14/11/17.
 */

public class CommonFunction {

    public static boolean validate(EditText[] fields){
        for (EditText currentField : fields) {
            if (currentField.getText().toString().length() <= 0) {
                currentField.setError("Cannot Be Empty");
                currentField.requestFocus();
                return false;
            } else if (currentField.getId() == R.id.client_id) {
                if (currentField.getText().length() < 4) {
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
