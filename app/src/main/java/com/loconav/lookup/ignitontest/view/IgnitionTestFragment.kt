package com.loconav.lookup.ignitontest.view

import android.view.View
import androidx.databinding.DataBindingUtil
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentIgnitionTestBinding

class IgnitionTestFragment  : BaseFragment() {
    private  lateinit var ignitionTestBinding : FragmentIgnitionTestBinding
    override fun setViewId(): Int {
        return  R.layout.fragment_ignition_test
    }

    override fun onFragmentCreated() {
    }

    override fun bindView(view: View?) {
       view?.let {
           ignitionTestBinding = DataBindingUtil.bind(it)!!
       }

    }

    override fun getComponentFactory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}