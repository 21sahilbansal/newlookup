package com.loconav.lookup;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentBlankBinding;


public class BlankFragment extends BaseFragment {
    FragmentBlankBinding blankBinding;
    @Override
    public int setViewId() {
        return R.layout.fragment_blank;
    }

    @Override
    public void onFragmentCreated() {

    }

    @Override
    public void bindView(View view) {
        blankBinding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }
}
