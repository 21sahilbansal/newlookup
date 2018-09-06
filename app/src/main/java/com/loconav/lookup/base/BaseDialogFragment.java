package com.loconav.lookup.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.R;

/**
 * Created by gauravmittal on 26/12/17.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private int isSuccessful ;

    public interface OnDialogCompletionListener {
        void onComplete();
    }

    protected OnDialogCompletionListener onCompletionListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogWindowAnimation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isSuccessful = 0;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected boolean isSafe() {
        return !(this.isRemoving() || this.getActivity() == null || this.isDetached() || !this.isAdded() || this.getView() == null);
    }

    public void setOnCompletionListener(OnDialogCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    protected String getDialogName(){
        return getClass().getSimpleName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void dismiss(boolean success) {
        if(success) {
            isSuccessful = 1;
        } else {
            isSuccessful = 0;
        }
        super.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
