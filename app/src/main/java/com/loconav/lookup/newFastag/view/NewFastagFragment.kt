package com.loconav.lookup.newFastag.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentNewfastagBinding
import com.loconav.lookup.newFastag.controller.NewFastagController
import org.greenrobot.eventbus.EventBus

class NewFastagFragment : BaseFragment() {
    private  var  newFastagController : NewFastagController? = null
     var binding : FragmentNewfastagBinding?=null


    override fun setViewId(): Int {
       return R.layout.fragment_newfastag
    }

    override fun onFragmentCreated() {
       setUpController(view!!)

    }

    private fun setUpController(view: View) {
        EventBus.getDefault().register(this)
        binding?.let {
            newFastagController = NewFastagController(it,fragmentManager!!,context!!)
        }



    }

    override fun bindView(view: View?) {
     binding = DataBindingUtil.bind(view!!)
    }

    override fun getComponentFactory() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}