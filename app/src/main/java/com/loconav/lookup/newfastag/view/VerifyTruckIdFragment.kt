package com.loconav.lookup.newfastag.view

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.loconav.lookup.FragmentController
import com.loconav.lookup.R
import com.loconav.lookup.Toaster
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentNewfastagBinding
import com.loconav.lookup.databinding.FragmentVerifyTrucknoBinding
import com.loconav.lookup.newfastag.controller.NewFastagController
import com.loconav.lookup.newfastag.model.FastagRequestApiService
import com.loconav.lookup.newfastag.model.NewFastagEvent
import com.loconav.lookup.newfastag.model.VehicleDetails
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class VerifyTruckIdFragment : BaseFragment() {
    private var fastagRequestApiService: FastagRequestApiService = FastagRequestApiService()
    private lateinit var vehicleDetails: VehicleDetails
    var binding: FragmentVerifyTrucknoBinding? = null
    private val fragmentController: FragmentController = FragmentController()
    private var contiueButton : Button ?= null
    private var trucknoEt: EditText ?= null
    lateinit var truckNumber : String
    private val continueSubmitter =View.OnClickListener {

        if(trucknoEt?.text!!.isEmpty()) {
            truckNumber = ""
            Toaster.makeToast("Truck no can't be empty")
        }
        else{
            truckNumber = trucknoEt?.text.toString().toUpperCase()
            fastagRequestApiService.validateTruckNo(truckNumber)
        }
    }

    override fun setViewId(): Int {
        return R.layout.fragment_verify_truckno
    }

    override fun onFragmentCreated() {
        (activity as AppCompatActivity).supportActionBar!!.title = "New Fastag Installation"
        contiueButton = binding?.fastagContinueBt
        trucknoEt = binding?.truckNo
        attachListeners()

    }

    private fun attachListeners() {
        contiueButton?.setOnClickListener(continueSubmitter)

    }

    override fun bindView(view: View?) {
        binding = DataBindingUtil.bind(view!!)

    }

    override fun getComponentFactory() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scannedFastag(newFastagEvent: NewFastagEvent) {
        when (newFastagEvent.message) {
            NewFastagEvent.TRUCK_ID_NOT_VERIFIED -> {
                val message: String = newFastagEvent.`object` as String
                Toaster.makeToast(message)
            }
            NewFastagEvent.Truck_ID_VERIFIED -> {
                vehicleDetails = newFastagEvent.`object` as VehicleDetails
                openNewFastagFragemnt(vehicleDetails)
            }
        }
    }

    private fun openNewFastagFragemnt(vehicleDetails: VehicleDetails) {
        var bundle: Bundle = Bundle()
        bundle.putString("truckNo",truckNumber)
        bundle.putSerializable("vehicleDetail",vehicleDetails)
        val newFastagFragment : NewFastagFragment = NewFastagFragment()
        newFastagFragment.arguments  = bundle
        fragmentController.loadFragment(newFastagFragment,fragmentManager,R.id.frameLayout,true)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}