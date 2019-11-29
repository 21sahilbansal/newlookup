package com.loconav.lookup.ignitontest.view

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.adapter.IgnitionTestAdapter
import com.loconav.lookup.application.LookUpApplication
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentIgnitionTestBinding
import com.loconav.lookup.ignitontest.viewModel.IgnitionTestViewModel

class IgnitionTestFragment : BaseFragment() {
    private lateinit var ignitionTestBinding: FragmentIgnitionTestBinding
    private lateinit var ignitionTestAdapter: IgnitionTestAdapter
    private lateinit var ignitionTestViewModel: IgnitionTestViewModel
    private lateinit var ignitionTestReyclerView : RecyclerView

    override fun setViewId(): Int {
        return R.layout.fragment_ignition_test
    }

    override fun onFragmentCreated() {
        ignitionTestViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(LookUpApplication()).create(IgnitionTestViewModel ::class.java)

    }

    override fun bindView(view: View?) {
        view?.let {
            ignitionTestBinding = DataBindingUtil.bind(it)!!
        }

    }

    override fun getComponentFactory() {
    }
}