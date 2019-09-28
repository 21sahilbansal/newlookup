package com.loconav.lookup.newFastag.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.View
import android.widget.Button
import com.loconav.lookup.*
import com.loconav.lookup.Constants.DEVICE_ID
import com.loconav.lookup.base.PubSubEvent
import com.loconav.lookup.databinding.FragmentNewfastagBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewFastagController(var binding: FragmentNewfastagBinding, var fragmentManager: android.support.v4.app.FragmentManager,var context: Context) {
    private lateinit var fragmentController: FragmentController
    private val ContinueButton : Button = binding.btFastagContinue

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val scannedFastag = intent.getStringExtra(DEVICE_ID)
            Log.d("receiver", "Got message: $message")
            validateFastag(scannedFastag)
            binding.etDeviceId.setText(message)
            binding.etDeviceId.setSelection(binding.etDeviceId.getText().length)
        }
    }

    private fun validateFastag(scannedFastag : String) {
        if(scannedFastag.equals()){
            Toaster.makeToast("Selected right fastag")
            binding.btFastagContinue.setText(R.string.submit)
        }
        else{
            Toaster.makeToast("Selected wrong fastag")
        }
    }

    init {
        EventBus.getDefault().register(this)
        attachListeners()
        registerBroadcast()
    }

    private var continueButton = View.OnClickListener { v ->
        if (binding.fastTruckNo.visibility == 0) {
            if (binding.fastTruckNo.text.isNotEmpty()) {


            } else {
                Toaster.makeToast("Truck no can't be empty")
            }

        }
        if (    ContinueButton.text.equals(R.string.scan_fastag)) {
            setScanner()
        }

        if(ContinueButton.text.equals(R.string.submit)){

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
      ContinueButton.setOnClickListener { continueButton }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun truckNoVerified(pubSubEvent: PubSubEvent) {
        if (pubSubEvent.message.equals(true)) {
            populateVehicleCard()

        } else {

        }

    }


    private fun populateVehicleCard() {
        binding.fastTruckNo.visibility = View.INVISIBLE
        binding.vehicleIdEt.setText("gvuygu")
        binding.axleEt.setText("AXLENO")
        binding.colorEt.setText("colour")
        binding.colorTv.setBackgroundColor(123)
        binding.fastagSnoEt.setText("fasttagno")
        ContinueButton.setText(R.string.scan_fastag)
        binding.fastagCview.visibility = View.VISIBLE

    }


    private fun registerBroadcast() {
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                IntentFilter(Constants.NEW_SCANNED_FASTAG))
    }




}