package com.loconav.lookup.ignitontest.view

import android.app.AlertDialog
import android.app.Dialog
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.adapter.IgnitionTestAdapter
import com.loconav.lookup.application.LookUpApplication
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentIgnitionTestBinding
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.ignitontest.viewModel.IgnitionTestViewModel

class IgnitionTestFragment : BaseFragment() {
    private lateinit var ignitionTestBinding: FragmentIgnitionTestBinding
    private lateinit var ignitionTestAdapter: IgnitionTestAdapter
    private lateinit var ignitionTestViewModel: IgnitionTestViewModel
    private lateinit var ignitionTestReyclerView: RecyclerView
    private lateinit var alertDialog: Dialog
    private lateinit var deviceId: String
    private lateinit var testStartTime: String

    override fun setViewId(): Int {
        return R.layout.fragment_ignition_test
    }

    override fun onFragmentCreated() {
        deviceId = arguments?.get(getString(R.string.deviceDetail_deviceid)) as String
        ignitionTestViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(LookUpApplication()).create(IgnitionTestViewModel::class.java)
        showAlertDialog()
    }

    private fun showAlertDialog() {
        alertDialog = AlertDialog.Builder(context)
                .setTitle("Ignition Test")
                .setMessage("Press ok to start the ignition test")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    getIgnitionData()

                }
                .setNegativeButton("No") { dialogIntertface, j ->

                }
                .create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    private fun getIgnitionData() {
        testStartTime = (System.currentTimeMillis() / 1000).toString()
        ignitionTestViewModel.getIgnitionTestData(deviceId, testStartTime)?.observe(this, Observer {it.data?.let {
            setRecyclerView(it)
        }

        })
    }

    private fun setRecyclerView(ignitionTestData: IgnitionTestData) {
        var linearLayoutManager = LinearLayoutManager(context)
        ignitionTestReyclerView = ignitionTestBinding.ignitionTestRv
        ignitionTestReyclerView.layoutManager = linearLayoutManager
        ignitionTestAdapter = IgnitionTestAdapter(ignitionTestData)
        ignitionTestReyclerView.adapter = ignitionTestAdapter
    }

    override fun bindView(view: View?) {
        view?.let {
            ignitionTestBinding = DataBindingUtil.bind(it)!!
        }

    }

    override fun getComponentFactory() {
    }
}



