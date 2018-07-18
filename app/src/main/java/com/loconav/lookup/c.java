package com.loconav.lookup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loconav.lookup.Util.matchregex;

/**
 * Created by sejal on 12-07-2018.
 */

public class c extends customInflater {

    @BindView (R.id.linearMain) LinearLayout linearMain;
//    @BindView (R.id.et) EditText ed1;
//    @BindView (R.id.et2) EditText ed2;
//    @BindView (R.id.threeTv) TextView tv;
//    @BindView (R.id.three) Button b1;

    @Override
    int setViewId() {
        return R.layout.c;
    }

    @Override
    void onFragmentCreated() {
        addLayout(linearMain);
        addtext();
        ArrayList<String> al = new ArrayList<>();
        al.add("yo");
        al.add("yoooo");
        addRadioButtons(2,al);
        addCheckBox(2,al);
        seekBar(60,20,10);
        // checkMethod();
    }

    @Override
    void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    void getComponentFactory() {

    }
//    void checkMethod(){
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String str=ed1.getText().toString();
//                String str2=ed2.getText().toString();
//                Boolean flag=matchregex(str,str2);
//                Log.e("ss",""+str+str2+flag);
//                tv.setText(flag.toString());
//            }
//        });
//
//    }

}
