package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FullImageBinding;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class Full_image_fragment extends BaseFragment {
    FullImageBinding fullImageBinding;
    @Override
    public int setViewId() {
        return R.layout.full_image;
    }

    @Override
    public void onFragmentCreated() {
        Bundle bundle = this.getArguments();
        String imageurl = bundle.getString("imageurl");
        Picasso.get().load(imageurl).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(fullImageBinding.imageView2);
        fullImageBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post( imageurl);
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void bindView(View view) {
        fullImageBinding = DataBindingUtil.bind(view);
    }



    @Override
    public void getComponentFactory() {
    }
}
