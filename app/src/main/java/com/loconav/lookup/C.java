package com.loconav.lookup;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loconav.lookup.Utility.matchregex;

/**
 * Created by sejal on 12-07-2018.
 */

public class C extends BaseTitleFragment {

    @Override
    public int setViewId() {
        return R.layout.c;
    }

    @Override
    public void onFragmentCreated() {
        // checkAndRequestPermissions();
        checkMethod();
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void getComponentFactory() {

    }
//    @BindView (R.id.linearMain) LinearLayout linearMain;
    @BindView (R.id.et1) EditText ed1;
  //  @BindView (R.id.et2) EditText ed2;
    @BindView (R.id.tv) TextView tv;
    @BindView (R.id.b1) Button b1;
//    DateTimeDialog dateTimeDialog;
//TextView tv1,tv2;


    //
//    public void displaytext(){
//        tv1=addtext(dateTimeDialog.getDateSelected());
//        tv2=addtext(dateTimeDialog.getTimeSelected());
//    }


    //oncreated
    //        addLayout(linearMain);
//        ArrayList<String> al = new ArrayList<>();
//        al.add("yo");
//        al.add("yoooo");
//        addRadioButtons(2,al);
//        addCheckBox(2,al);
//        seekBar(60,20,10);
//        dateTimeDialog=DateTimeDialog.newInstance();
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dateTimeDialog.show(getFragmentManager(),getClass().getSimpleName());
//                displaytext();
//            }
//        });
    // addEditText();
    // checkMethod();


    void checkMethod(){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Repair2 fragmentOne = new Repair2();
//                fragmentTransaction.replace(R.id.fragmentContainer, fragmentOne, "Fragment One");
//                fragmentTransaction.addToBackStack("str");
//                fragmentTransaction.commit();


               String str=ed1.getText().toString();
                String str2="^$";
//            //   String str2="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//               //String str2="^(([\\w-]+\\\\.)+[\\\\w-]+|([typedArrayCustom-zA-Z]{1}|[\\\\w-]{2,}))@\"\n" +
////                       "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\\\.([0-1]?\"\n" +
////                       "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\\\.\"\n" +
////                       "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\\\.([0-1]?\"\n" +
////                        "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|\"\n" +
////                       "([typedArrayCustom-zA-Z]+[\\\\w-]+\\\\.)+[typedArrayCustom-zA-Z]{2,4})$";
//               // String str2="^([typedArrayCustom-zA-Z0-9_\\-\\.]+)@([typedArrayCustom-zA-Z0-9_\\-\\.]+)\\.([typedArrayCustom-zA-Z]{2,5})$";
//                    String str2="^[0-9]{9}$";
                Boolean flag=matchregex(str,str2);
                Log.e("ss",""+str+str2+flag);
                tv.setText(flag.toString());
            }
        });

    }

    @Override
    public String getTitle() {
        return "CH";
    }
}