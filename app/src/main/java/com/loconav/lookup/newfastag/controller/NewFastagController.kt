package com.loconav.lookup.newfastag.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.CardView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.loconav.lookup.*
import com.loconav.lookup.Constants.DEVICE_ID
import com.loconav.lookup.databinding.FragmentNewfastagBinding
import com.loconav.lookup.newfastag.model.FastagRequestApiService
import com.loconav.lookup.newfastag.model.NewFastagEvent
import com.loconav.lookup.newfastag.model.VehicleDetails
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewFastagController(var binding: FragmentNewfastagBinding, var fragmentManager: android.support.v4.app.FragmentManager,var context: Context) {
    private lateinit var fragmentController: FragmentController
    private  var fastagRequestApiService: FastagRequestApiService = FastagRequestApiService()
    private lateinit var continueButton : Button
    private var truckNo : EditText = binding.fastTruckNo
    private var vehicleID : TextView = binding.vehicleIdEt
    private var axleEt : TextView = binding.axleEt
    private var colorEt : TextView = binding.colorEt
    private var fastagCview : CardView = binding.fastagCview
    private var linearLayout : LinearLayout = binding.linearLayout2
    private var imageCView : CardView = binding.optionalImageCard



    private val continueSubmiter = View.OnClickListener {
        Log.e("newfm","iamhere")
        if (truckNo.visibility == 0) {
            if (truckNo.text.isNotEmpty()) {
                fastagRequestApiService.validateTruckNo(truckNo.text.toString())
            } else {
                Toaster.makeToast("Truck no can't be empty")
            }

        }
        if (    continueButton.text.equals(R.string.scan_fastag)) {
            setScanner()
        }

        if(continueButton.text.equals(R.string.submit)){

        }
    }

    init {
        EventBus.getDefault().register(this)
        attachListeners()
        registerBroadcast()
    }


    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val scannedFastag = intent.getStringExtra(DEVICE_ID)
            validateFastag(scannedFastag)

        }
    }

    private fun validateFastag(scannedFastag : String) {
        if(scannedFastag.equals("kshad")){
            Toaster.makeToast("Selected right fastag")
            binding.btFastagContinue.setText(R.string.submit)
        }
        else{
            Toaster.makeToast("Selected wrong fastag")
        }
    }




    private fun setScanner() {
        fragmentController = FragmentController()
        val qrScannerRequest : String = Constants.NEW_SCANNED_FASTAG
        val bundle = Bundle()
        bundle.putString(Constants.KEY_FOR_QRSCANNER,qrScannerRequest)
        val qrScannerFragment = QRScannerFragment()
        fragmentController.replaceFragment(qrScannerFragment, fragmentManager, R.id.frameLayout, true)
    }

    private fun attachListeners() {
        Log.e("newfm","iaminattach")
         continueButton = binding.btFastagContinue
        continueButton.setOnClickListener(continueSubmiter)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun truckNoVerified(newFastagEvent: NewFastagEvent) {
        if (newFastagEvent.message.equals(NewFastagEvent.Truck_ID_VERIFIED)) {
          val vehicleDetails :VehicleDetails = newFastagEvent.`object`as VehicleDetails
            populateVehicleCard(vehicleDetails)
        }
    }

    private fun populateVehicleCard(vehicleDetails : VehicleDetails) {
        linearLayout.visibility = View.GONE
        vehicleID.setText(vehicleDetails.vehicleId.toString())
        axleEt.setText(vehicleDetails.axleDescription)
        colorEt.setText(vehicleDetails.color)
        fastagCview.setCardBackgroundColor(Color.parseColor(vehicleDetails.colorHex))
        fastagCview.visibility = View.VISIBLE
        imageCView.visibility = View.VISIBLE
        continueButton.setText(R.string.scan_fastag)

    }


    private fun registerBroadcast() {
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                IntentFilter(Constants.NEW_SCANNED_FASTAG))
    }
}