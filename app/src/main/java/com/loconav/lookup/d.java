package com.loconav.lookup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sejal on 12-07-2018.
 */

public class d extends titleFragment {

    @BindView(R.id.four) Button four;
    @BindView (R.id.fourTv) TextView fourTv;

    @Override
    public String getTitle() {
     return "this is 4th";
    }

    @Override
    int setViewId() {
        return R.layout.d;
    }

    @Override
    void onFragmentCreated() {
        ButterKnife.bind(this, getView());
        fourTv.setText(getTitle());
        final FragmentManager fragmentManager = getFragmentManager();
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // repair fragmentOne = new repair();

//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                FragmentControlller.printActivityFragmentList(fragmentManager);
//
//                // Get fragment one if exist.
//                repair fragmentOne = new repair();
//
//                fragmentTransaction.replace(R.id.fragmentContainer, fragmentOne, "Fragment One");
//
//                // Do not add fragment three in back stack.
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

//                FragmentBackStack fragmentBackStack= new FragmentBackStack();
//                fragmentBackStack.loadFragment(fragmentOne,getFragmentManager());
               // new FragmentControlller(getFragmentManager(),getActivity());
            }
        });
    }

    @Override
    void bindView(View view) {

    }

    @Override
    void getComponentFactory() {

    }
}
