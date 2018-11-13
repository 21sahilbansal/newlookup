package com.loconav.lookup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
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

     public TextView addtext(String str,LinearLayout linearLayout1, Input input, int index){
         TextView textView = new TextView(getContext());
         textView.setText(" "+str);
         textView.setTag(input);
         textView.setTextColor(getResources().getColor(R.color.black));
         linearLayout1.addView(textView,index);
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

   public EditText addEditText(LinearLayout linearLayout1, Input str,int index){
        TextInputLayout textInputLayout=new TextInputLayout(getContext());
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        linearLayout1.addView(textInputLayout,index);
        EditText editText = new EditText(getContext());
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextParams);
        editText.setTextSize(16);
        //for next line at enter button
        editText.getImeOptions();
        editText.setSingleLine();
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setHint(str.getName());
        editText.setHintTextColor(getResources().getColor(R.color.gray));
        textInputLayout.addView(editText, editTextParams);
       editText.setTag(str);
       textInputLayout.setTag(str);
        return  editText;
    }

    public void addSpinner(LinearLayout linearLayout, ArrayList<String> spinnerList, int index, Input input) {
        Spinner spinner=new Spinner(getContext());
        LinearLayout.LayoutParams spinnerTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        spinnerTextParams.setMargins(0,40,0,40);
        spinner.setLayoutParams(spinnerTextParams);
        spinner.setPadding(23,23,23,23);
        spinner.setTag(input);
        //Border for spinner
        spinner.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_border_rectangle) );
        linearLayout.addView(spinner,index);
        setSpinner(spinnerList,spinner);
    }

    public void setSpinner(ArrayList<String> categories, Spinner spinnerRep ) {
        spinnerRep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRep.setAdapter(dataAdapter);
    }

    public void addImagePicker(LinearLayout linearLayout, int index, Input input, String titleText,int limit1,String idText1) {
        CustomImagePicker customImagePicker=new CustomImagePicker(getContext(),titleText,limit1,idText1);
        LinearLayout.LayoutParams imagePickereditTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imagePickereditTextParams.setMargins(0,40,0,40);
        customImagePicker.setLayoutParams(imagePickereditTextParams);
        customImagePicker.setTag(input);
        linearLayout.addView(customImagePicker,index);
    }
}
