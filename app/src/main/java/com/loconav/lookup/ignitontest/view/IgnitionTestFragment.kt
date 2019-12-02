package com.loconav.lookup.ignitontest.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.adapter.IgnitionTestAdapter
import com.loconav.lookup.application.LookUpApplication
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentIgnitionTestBinding
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.ignitontest.viewModel.IgnitionTestViewModel
import com.loconav.lookup.utils.TimerCount


class IgnitionTestFragment : BaseFragment(), CountDownInterface {


    private lateinit var ignitionTestBinding: FragmentIgnitionTestBinding
    private lateinit var ignitionTestAdapter: IgnitionTestAdapter
    private lateinit var ignitionTestViewModel: IgnitionTestViewModel
    private lateinit var ignitionTestReyclerView: RecyclerView
    private lateinit var alertDialog: Dialog
    private lateinit var deviceId: String
    private lateinit var testStartTime: String
    private lateinit var coninuteButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var mRunnable: Runnable
    private lateinit var timeTextView: TextView
    private var handler: android.os.Handler = android.os.Handler(Looper.getMainLooper())
    private var apiCallTime: Long = 10 * 1000
    private lateinit var countDownTimer: TimerCount

    override fun setViewId(): Int {
        return com.loconav.lookup.R.layout.fragment_ignition_test

    }

    override fun onFragmentCreated() {
        deviceId = arguments?.getString("deviceDetail_deviceid")!!
        ignitionTestViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(LookUpApplication()).create(IgnitionTestViewModel::class.java)
        testStartTime = (System.currentTimeMillis() / 1000).toString()
        coninuteButton = ignitionTestBinding.ignitionButton
        timeTextView = ignitionTestBinding.timerCount
        showAlertDialog()
    }

    private fun showAlertDialog() {
        alertDialog = AlertDialog.Builder(context)
                .setTitle("Ignition Test")
                .setMessage("Press ok to start the ignition test")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    progressBar = ProgressBar(context)
                    progressBar.visibility = View.VISIBLE
                    timeTextView.visibility = View.VISIBLE
                    runPeriodicTestCheck()
                }
                .setNegativeButton("No") { dialogIntertface, j ->
                }
                .create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }


    private fun runPeriodicTestCheck() {
        mRunnable = Runnable {
            startTimer()
            getIgnitionData()
            handler.postDelayed(mRunnable, apiCallTime)
        }
        mRunnable.run()
    }

    private fun startTimer() {
        countDownTimer = TimerCount(10 * 1000, 1000, this).start() as TimerCount
    }

    private fun getIgnitionData() {

        ignitionTestViewModel.getIgnitionTestData(deviceId, testStartTime)?.observe(this, Observer {
            it.data?.let {
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

    override fun getTickTime(millisUntilFinished: Long) {
        timeTextView.text = millisUntilFinished.toString()
    }

    override fun onFinish() {
    }
}

