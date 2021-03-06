package com.loconav.lookup.newfastag.controller

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.loconav.lookup.*
import com.loconav.lookup.customcamera.CustomImagePicker
import com.loconav.lookup.databinding.FragmentNewfastagBinding
import com.loconav.lookup.model.FastTagResponse
import com.loconav.lookup.newfastag.model.FastagRequestApiService
import com.loconav.lookup.newfastag.model.NewFastagEvent
import com.loconav.lookup.newfastag.model.VehicleDetails
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewFastagController(var binding: FragmentNewfastagBinding, var fragmentManager: androidx.fragment.app.FragmentManager, var context: Context, var receivedbundle: Bundle) {
    private val fragmentController: FragmentController = FragmentController()
    private var fastagRequestApiService: FastagRequestApiService = FastagRequestApiService()
    private lateinit var continueButton: Button
    private var vehicleID: TextView = binding.vehicleIdEt
    private var axleEt: TextView = binding.axleEt
    private lateinit var vehicleDetails: VehicleDetails
    private var colorEt: TextView = binding.colorEt
    private var fastagCview: androidx.cardview.widget.CardView = binding.fastagCview
    private var imageCView: androidx.cardview.widget.CardView = binding.optionalImageCard
    private var customImagePicker: CustomImagePicker = binding.installImage
    private var newScannedFastagTview : TextView =  binding.newScannedFastaTv
    private var verifiedTruckNumber: String = ""
    private lateinit var   newScannedFastag: String

    private val continueSubmiter = View.OnClickListener {
        if (continueButton.text.equals(context?.resources?.getString(R.string.scan_fastag))) {
            setScanner()
        } else if (continueButton.text.equals(context?.resources?.getString(R.string.submit))) {
            validateFastag(newScannedFastag)
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
        if (customImagePicker.getimagesList().size == 1) {
            var uri: Uri = customImagePicker.getimagesList()[0].uri
            bundle.putString("Image_URI", uri.toString())
        }
        fastTagPhotosFragment.arguments = bundle
        fragmentController.loadFragment(fastTagPhotosFragment, fragmentManager, R.id.frameLayout, true)
    }


    private fun validateFastag(scannedFastag: String) {
        fastagRequestApiService.validateScannedFastag(vehicleDetails.vehicleId!!, scannedFastag)
    }


    private fun setScanner() {
        val qrScannerRequest: String = Constants.NEW_SCANNED_FASTAG
        val bundle = Bundle()
        bundle.putString(Constants.KEY_FOR_QRSCANNER, qrScannerRequest)
        val qrScannerFragment = QRScannerFragment()
        qrScannerFragment.arguments = bundle
        fragmentController.loadFragment(qrScannerFragment, fragmentManager, R.id.frameLayout, true)
    }

    private fun attachListeners() {
        continueButton.setOnClickListener(continueSubmiter)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scannedFastag(newFastagEvent: NewFastagEvent) {
        when (newFastagEvent.message) {
            NewFastagEvent.SCANNED_CORRECT_FASTAG -> {
                Toaster.makeToast("Fastag attached successfully")
                getDataForPhotosFrag()
            }
            NewFastagEvent.SCANNED_WRONG_FASTAG -> {
                val message: String = newFastagEvent.`object` as String
                Toaster.makeToast(message)
                continueButton.setText(R.string.scan_fastag)
            }
            NewFastagEvent.DATA_FOR_FASTAG_NOT_FOUND -> {
                val message: String = newFastagEvent.`object` as String
                Toaster.makeToast(message)
            }
            NewFastagEvent.GOT_DATA_FOR_FASTAG_PHOTOS -> {
                openFastagPhotosFragment(newFastagEvent.`object` as FastTagResponse)
            }
            NewFastagEvent.SCANNED_FASTAG -> {
                newScannedFastag = newFastagEvent.`object` as String
                newScannedFastagTview.text = newScannedFastag
                newScannedFastagTview.visibility = View.VISIBLE
                continueButton.setText(R.string.submit)
            }
        }
    }


    private fun populateVehicleCard(vehicleDetails: VehicleDetails) {
        Log.e("vehicleid", vehicleDetails.vehicleId.toString())
        vehicleID.setText(verifiedTruckNumber)
        axleEt.setText(vehicleDetails.axleDescription)
        colorEt.setText(vehicleDetails.color)
        fastagCview.setCardBackgroundColor(Color.parseColor(vehicleDetails.colorHex))
        continueButton.setText(R.string.scan_fastag)
    }


    init {
        EventBus.getDefault().register(this)
        retrieveBundle()
        essentialInitilaizers()
        attachListeners()
        populateVehicleCard(vehicleDetails)
    }


    fun essentialInitilaizers() {
        continueButton = binding.btFastagContinue
    }


    private fun retrieveBundle() {
        vehicleDetails = receivedbundle.getSerializable("vehicleDetail") as VehicleDetails
        verifiedTruckNumber = receivedbundle.getString("truckNo")
    }

    fun onDestroy() {
        EventBus.getDefault().unregister(this)
    }
}


