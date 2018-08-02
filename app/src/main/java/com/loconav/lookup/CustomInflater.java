package com.loconav.lookup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by sejal on 17-07-2018.
 */

public class CustomInflater extends LinearLayout {
    LinearLayout linearLayout;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    public CustomInflater(Context context) {
        super(context);
    }

    public CustomInflater(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Options, 0, 0);
        String titleText = a.getString(R.styleable.Options_titleText);
        a.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ci, this, true);
        linearLayout=findViewById(R.id.linearLayout);
        TextView  title= addtext(titleText);
    }

    void addLayout(LinearLayout linearLayout){
           linearLayout.setOrientation(LinearLayout.VERTICAL);
           this.linearLayout=linearLayout;
        }
     public TextView addtext(String str){
         TextView textView = new TextView(getContext());
         textView.setText(str);
       //  linearLayout.addView(textView);
         return textView;
     }

     void addRadioButtons(int noButtons, ArrayList<String> text){
         RadioGroup rg=new RadioGroup(getContext());
         for (int i =0;i<noButtons;i++) {
             RadioButton rb1 = new RadioButton(getContext());
             rb1.setText(text.get(i));
             rg.addView(rb1);
         }
         rg.setLayoutParams(params);
         linearLayout.addView(rg);
     }

    void addCheckBox(int noButtons, ArrayList<String> text){
        for (int i =0;i<noButtons;i++) {
            CheckBox cb=new CheckBox(getContext());
            cb.setLayoutParams(params);
            cb.setText(text.get(i));
            linearLayout.addView(cb);
        }
    }

    void seekBar(final int max, final int min, final int step){
        SeekBar sb = new SeekBar(getContext());
        linearLayout.addView(sb);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   if(progress<min){
                       progress=min;
                   }else if(progress>max){
                       progress=max;
                   }else {
                       progress = progress / step;
                       progress = progress * step;
                   }
                seekBar.setProgress(progress);
            }
        });
    }

   public EditText addEditText(LinearLayout linearLayout1, String str,int index){
        TextInputLayout til=new TextInputLayout(getContext());
        til.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        linearLayout1.addView(til,index);
        EditText editText = new EditText(getContext());
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextParams);

        editText.setTextSize(16);
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setHint(str);
        editText.setHintTextColor(getResources().getColor(R.color.gray));

        til.addView(editText, editTextParams);
        return  editText;
    }

}
