package com.loconav.lookup;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.loconav.lookup.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by sejal on 11-08-2018.
 */

public class DeviceDetailsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @Override
    public int setViewId() {
        return R.layout.main_activity3;
    }

    @Override
    public void onFragmentCreated() {

    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this,getView());
    }

    @Override
    public void getComponentFactory() {

    }

    @Override
    public void onRefresh() {

    }
}
