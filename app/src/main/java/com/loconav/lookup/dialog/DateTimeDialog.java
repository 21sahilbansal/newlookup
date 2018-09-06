package com.loconav.lookup.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.databinding.DialogDateTimePickerBinding;

import java.util.Calendar;

/**
 * Created by sejal on 20-07-2018.
 */

public class DateTimeDialog extends BaseDialogFragment {
    private DialogDateTimePickerBinding binding;
    private String str1,str2;
    private final Calendar c = Calendar.getInstance();
    private int mYear = c.get(Calendar.YEAR);
    private int mMonth = c.get(Calendar.MONTH);
    private int mDay = c.get(Calendar.DAY_OF_MONTH);
    private int mHour = c.get(Calendar.HOUR_OF_DAY);
    private int mMinute = c.get(Calendar.MINUTE);

    public static DateTimeDialog newInstance() {
        DateTimeDialog dateTimeDialog = new DateTimeDialog();
        return dateTimeDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date_time_picker, new LinearLayout(getActivity()),
                        false);
        binding= DataBindingUtil.bind(dialogView);

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addDatepicker();
            }
        });

        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimerPicker();
            }
        });

        binding.timeTv.setText(mHour+":"+mMinute);
        binding.dateTv.setText(mDay+"/"+mMonth+"/"+mYear);
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        binding.DateDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str1=binding.dateTv.getText().toString();
                str2=binding.timeTv.getText().toString();
                dismiss();
              //  newc.displaytext();

            }
        });
        return builder;
    }

    private void addDatepicker(){

        DatePickerDialog dp=new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
             binding.dateTv.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
            }
        },mYear,mMonth,mDay);
        dp.show();
    }

    private void addTimerPicker(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        binding.timeTv.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public String getDateSelected(){
        return str1;
    }
    public String getTimeSelected(){
        return str2;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}
