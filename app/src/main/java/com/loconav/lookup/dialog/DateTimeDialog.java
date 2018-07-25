package com.loconav.lookup.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

import com.loconav.lookup.C;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sejal on 20-07-2018.
 */

public class DateTimeDialog extends BaseDialogFragment {
    @BindView(R.id.date) LinearLayout date;
    @BindView(R.id.time) LinearLayout time;
    @BindView(R.id.dateTv) TextView dateTv;
    @BindView(R.id.timeTv) TextView timeTv;
    @BindView(R.id.DateDone) Button done;
     String str1,str2;
    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);

    public static DateTimeDialog newInstance() {
        DateTimeDialog dateTimeDialog = new DateTimeDialog();
        return dateTimeDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final C newc=new C();
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date_time_picker, new LinearLayout(getActivity()),
                        false);
        ButterKnife.bind(this, dialogView);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addDatepicker();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimerPicker();
            }
        });

        timeTv.setText(mHour+":"+mMinute);
        dateTv.setText(mDay+"/"+mMonth+"/"+mYear);
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str1=dateTv.getText().toString();
                str2=timeTv.getText().toString();
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
             dateTv.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
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

                        timeTv.setText(hourOfDay + ":" + minute);
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
}
