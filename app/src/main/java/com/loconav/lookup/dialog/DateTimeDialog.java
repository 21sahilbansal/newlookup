package com.loconav.lookup.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

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
    private final int mYear = c.get(Calendar.YEAR);
    private final int mMonth = c.get(Calendar.MONTH);
    private final int mDay = c.get(Calendar.DAY_OF_MONTH);
    private final int mHour = c.get(Calendar.HOUR_OF_DAY);
    private final int mMinute = c.get(Calendar.MINUTE);

    public static DateTimeDialog newInstance() {
        return new DateTimeDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date_time_picker, new LinearLayout(getActivity()),
                        false);
        binding= DataBindingUtil.bind(dialogView);

        binding.date.setOnClickListener(v -> addDatepicker());

        binding.time.setOnClickListener(v -> addTimerPicker());

        binding.timeTv.setText(mHour+":"+mMinute);
        binding.dateTv.setText(mDay+"/"+mMonth+"/"+mYear);
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        binding.DateDone.setOnClickListener(view -> {
            str1=binding.dateTv.getText().toString();
            str2=binding.timeTv.getText().toString();
            dismiss();
          //  newc.displaytext();

        });
        return builder;
    }

    private void addDatepicker(){

        DatePickerDialog dp=new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> binding.dateTv.setText(dayOfMonth+"/"+monthOfYear+"/"+year),mYear,mMonth,mDay);
        dp.show();
    }

    private void addTimerPicker(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> binding.timeTv.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
        timePickerDialog.show();
    }

    public String getDateSelected(){
        return str1;
    }
    public String getTimeSelected(){
        return str2;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}
