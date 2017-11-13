package com.loconav.lookup.sharedetailsfragmants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.CustomActionBar;
import com.loconav.lookup.R;

/**
 * Created by prateek on 13/11/17.
 */

public class NewInstallation extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        CustomActionBar customActionBar = new CustomActionBar();
        customActionBar.getActionBar((AppCompatActivity)getActivity(),R.drawable.leftarrow,R.string.device_change,true);

        View view = inflater.inflate(R.layout.newinstallation, vg, false);


        return view;
    }
}
