package com.loconav.lookup.newfastag.view

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentNewfastagBinding
import com.loconav.lookup.newfastag.controller.NewFastagController

class NewFastagFragment : BaseFragment() {
    private var newFastagController: NewFastagController? = null
    var binding: FragmentNewfastagBinding? = null
    var receivedBundle: Bundle? = null


    override fun setViewId(): Int {
        return R.layout.fragment_newfastag
    }

    override fun onFragmentCreated() {
        (activity as AppCompatActivity).supportActionBar?.title = "New Fastag Installation"
        receivedBundle = arguments
        view?.let {
            setUpController(it)
        }
    }

    private fun setUpController(view: View) {
        binding?.let {
            newFastagController = NewFastagController(it, fragmentManager!!, context!!,receivedBundle!!)
        }
    }

    override fun bindView(view: View?) {
        binding = DataBindingUtil.bind(view!!)
    }

    override fun getComponentFactory() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newFastagController?.let {
            it.onDestroy()
        }
    }
}

