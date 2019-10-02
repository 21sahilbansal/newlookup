package com.loconav.lookup.newfastag.view

import android.databinding.DataBindingUtil
import android.view.View
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.base.PubSubEvent
import com.loconav.lookup.databinding.FragmentNewfastagBinding
import com.loconav.lookup.newfastag.controller.NewFastagController
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewFastagFragment:BaseFragment() {
    private  var  newFastagController : NewFastagController? = null
    var binding : FragmentNewfastagBinding?= null

    override fun setViewId(): Int {
        return R.layout.fragment_newfastag
    }

    override fun onFragmentCreated() {
        setUpController(view!!)     }

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
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun truckNoVerified(pubSubEvent: PubSubEvent) {


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

