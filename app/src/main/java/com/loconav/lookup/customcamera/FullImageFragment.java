package com.loconav.lookup.customcamera;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentFullImageBinding;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class FullImageFragment extends BaseFragment {

    private FragmentFullImageBinding fullImageBinding;
    @Override
    public int setViewId() {
        return R.layout.fragment_full_image;
    }

    @Override
    public void onFragmentCreated() {
        Bundle bundle = this.getArguments();
        String imageurl = bundle.getString("imageurl");
        Picasso.get().load(imageurl).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(fullImageBinding.image);
        fullImageBinding.delete.setOnClickListener(v -> {
            EventBus.getDefault().post( imageurl);
            getActivity().onBackPressed();
        });
        fullImageBinding.cancel.setOnClickListener(v -> getActivity().onBackPressed());
    }


    @Override
    public void bindView(View view) {
        fullImageBinding = DataBindingUtil.bind(view);
    }


    @Override
    public void getComponentFactory() {}


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fullImageBinding.unbind();
    }
}
