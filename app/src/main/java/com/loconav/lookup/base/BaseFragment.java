package com.loconav.lookup.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by prateek on 4/1/18.
 */

public abstract class BaseFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(setViewId(), container, false);
        bindView(view);
        getComponentFactory();
        onFragmentCreated();
        return view;
    }

    protected abstract int setViewId();

    protected abstract void onFragmentCreated();

    protected abstract void bindView(View view);

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    protected abstract void getComponentFactory();

}
