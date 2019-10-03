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
import com.loconav.lookup.model.FastTagResponse
import com.loconav.lookup.newfastag.model.FastagRequestApiService
import com.loconav.lookup.newfastag.model.NewFastagEvent
import com.loconav.lookup.newfastag.model.VehicleDetails
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewFastagController(var binding: FragmentNewfastagBinding, var fragmentManager: android.support.v4.app.FragmentManager, var context: Context) {
    private val fragmentController: FragmentController = FragmentController()
    private var fastagRequestApiService: FastagRequestApiService = FastagRequestApiService()
    private lateinit var continueButton: Button
    private var truckNo: EditText = binding.fastTruckNo
    private var vehicleID: TextView = binding.vehicleIdEt
    private var axleEt: TextView = binding.axleEt
    private var colorEt: TextView = binding.colorEt
    private var fastagCview: CardView = binding.fastagCview
    private var linearLayout: LinearLayout = binding.linearLayout2
    private var imageCView: CardView = binding.optionalImageCard
    private lateinit var vehicleDetails: VehicleDetails
    private var verifiedTruckNumber: String = ""
    private lateinit var messageReceiver : BroadcastReceiver



    private val continueSubmiter = View.OnClickListener {
        if (truckNo.visibility == 0) {
            if (truckNo.text.isNotEmpty()) {
                verifiedTruckNumber = truckNo.text.toString()
                fastagRequestApiService.validateTruckNo(truckNo.text.toString())

            } else {
                Toaster.makeToast("Truck no can't be empty")
                verifiedTruckNumber = ""
            }
        } else if (continueButton.text.equals(context.resources.getString(R.string.scan_fastag))) {
              setScanner()
        } else if (continueButton.text.equals(context.resources.getString(R.string.submit))) {
            getDataForPhotosFrag()

        } else {
            Toaster.makeToast("These is some other issue")
        }
    }

    private fun getDataForPhotosFrag() {
        fastagRequestApiService.getdataAfterFastagcreation(verifiedTruckNumber)
    }

    private fun openFastagPhotosFragment(fastTagResponse: FastTagResponse) {
        val fastTagPhotosFragment = FastTagPhotosFragment()
        val bundle = Bundle()
        bundle.putString("Truck_No", fastTagResponse.getTruckNumber())
        bundle.putString("FastTag_Serial_No", fastTagResponse.getFastagSerialNumber())
        bundle.putInt("Installation_Id", fastTagResponse.getId()!!)
        fastTagPhotosFragment.arguments = bundle
        fragmentController.replaceFragment(fastTagPhotosFragment, fragmentManager, R.id.frameLayout, true)
    }


    private fun validateFastag(scannedFastag: String) {
        var id : Int = vehicleDetails.vehicleId!!
        fastagRequestApiService.validateScannedFastag(vehicleDetails.vehicleId!!, scannedFastag)
    }


    private fun setScanner() {
        val qrScannerRequest: String = Constants.NEW_SCANNED_FASTAG
        val bundle = Bundle()
        bundle.putString(Constants.KEY_FOR_QRSCANNER, qrScannerRequest)
        val qrScannerFragment = QRScannerFragment()
        qrScannerFragment.arguments = bundle
        val trasction: android.support.v4.app.FragmentTransaction? = fragmentManager.beginTransaction()
        trasction?.replace(R.id.frameLayout, qrScannerFragment)
        trasction?.commit()
    }

    private fun attachListeners() {
        continueButton.setOnClickListener(continueSubmiter)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun truckNoVerified(newFastagEvent: NewFastagEvent) {
        if (newFastagEvent.message.equals(NewFastagEvent.Truck_ID_VERIFIED)) {
            vehicleDetails = newFastagEvent.`object` as VehicleDetails
            populateVehicleCard(vehicleDetails)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scannedFastag(newFastagEvent: NewFastagEvent) {
        if (newFastagEvent.message.equals(NewFastagEvent.Scanned_Correct_Fastag)) {
            continueButton.setText(R.string.submit)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun fastagPhotosData(newFastagEvent: NewFastagEvent) {
        if (newFastagEvent.message.equals(NewFastagEvent.Got_data_for_fastag_photos)) {
            openFastagPhotosFragment(newFastagEvent.`object` as FastTagResponse)
        }
    }

    private fun populateVehicleCard(vehicleDetails: VehicleDetails) {
        truckNo.visibility = View.GONE
        vehicleID.setText(vehicleDetails.vehicleId.toString())
        axleEt.setText(vehicleDetails.axleDescription)
        colorEt.setText(vehicleDetails.color)
        fastagCview.setCardBackgroundColor(Color.parseColor(vehicleDetails.colorHex))
        fastagCview.visibility = View.VISIBLE
        imageCView.visibility = View.VISIBLE
        continueButton.setText(R.string.scan_fastag)

    }


    private fun registerBroadcast() {
        LocalBroadcastManager.getInstance(context).registerReceiver(messageReceiver,
                IntentFilter(Constants.NEW_SCANNED_FASTAG))
    }

    init {
        EventBus.getDefault().register(this)
        essentialInitilaizers()
        attachListeners()
        registerBroadcast()
    }

    private fun essentialInitilaizers() {
        continueButton = binding.btFastagContinue
        messageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("receiver1", "Got message: ");
                val scannedFastag = intent.getStringExtra(DEVICE_ID)
                validateFastag(scannedFastag)
            }
        }
    }
}