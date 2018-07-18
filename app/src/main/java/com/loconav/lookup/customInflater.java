package com.loconav.lookup;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sejal on 17-07-2018.
 */

public class customInflater extends BaseFragment {
    LinearLayout linearLayout;
    LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    @Override
    int setViewId() {
        return 0;
    }

    @Override
    void onFragmentCreated() {

    }

    @Override
    void bindView(View view) {

    }

    @Override
    void getComponentFactory() {

    }

     void addLayout(LinearLayout linearLayout){
           linearLayout.setOrientation(LinearLayout.VERTICAL);
           this.linearLayout=linearLayout;
        }
     void addtext(){
         TextView textView = new TextView(getContext());
         textView.setText("hi");
         linearLayout.addView(textView);
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

}
