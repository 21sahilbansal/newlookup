package com.loconav.lookup.ignitontest.view

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.*
import com.loconav.lookup.Constants.NEW_INSTALL
import com.loconav.lookup.adapter.IgnitionTestAdapter
import com.loconav.lookup.application.LookUpApplication
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentIgnitionTestBinding
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.ignitontest.viewModel.IgnitionTestViewModel
import com.loconav.lookup.model.PassingReason
import com.loconav.lookup.sharedetailsfragmants.CommonRepairFragment
import com.loconav.lookup.utils.AppUtils
import com.loconav.lookup.utils.TimeUtils
import com.loconav.lookup.utils.TimerCount


class IgnitionTestFragment : BaseFragment(), CountDownInterface {
    private var ignitionTestBinding: FragmentIgnitionTestBinding? = null
    private var ignitionTestAdapter: IgnitionTestAdapter? = null
    private var ignitionTestViewModel: IgnitionTestViewModel? = null
    private var ignitionTestReyclerView: RecyclerView? = null
    private var alertDialog: Dialog? = null
    private var deviceId: String? = null
    private var testStartTime: String? = null
    private var coninuteButton: Button? = null
    private var progressBar: ProgressBar? = null
    private var mRunnable: Runnable? = null
    private var timeTextView: TextView? = null
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var apiCallTime: Int = 10 * 1000
    private var countDownTimer: TimerCount? = null
    private var isFirstTime: Boolean = true
    private var passingReason: PassingReason? = null
    private var progressDialog: ProgressDialog? = null
    private var deviceidTextView: TextView? = null
    private var testStartTimeTextView: TextView? = null

    private val fragmentController = FragmentController()

    override fun setViewId(): Int {
        return R.layout.fragment_ignition_test
    }

    override fun onFragmentCreated() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.ignition_test_title)
        deviceId = arguments?.getString(getString(R.string.devicedetail_deviceid))
        ignitionTestViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(LookUpApplication()).create(IgnitionTestViewModel::class.java)
        testStartTime = (System.currentTimeMillis() / 1000).toString()
        coninuteButton = ignitionTestBinding?.ignitionButton
        timeTextView = ignitionTestBinding?.timerCount
        deviceidTextView = ignitionTestBinding?.deviceidTextview
        deviceidTextView?.text = deviceId
        testStartTimeTextView = ignitionTestBinding?.teststartTimeTv
        testStartTimeTextView?.text = TimeUtils.getTimeFromEpochTime(testStartTime)
        progressBar = ignitionTestBinding?.progressbar
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(Constants.YOUR_TEST_DATA_IS_GETTING_REFRESHED)
        progressDialog?.setTitle(Constants.REFRESHING_DATA)
        coninuteButton?.setOnClickListener(continueButtonClick)
        mRunnable = Runnable {
            if (!isFirstTime) {
                startTimer()
            }
            getIgnitionData()
            handler.postDelayed(mRunnable, apiCallTime.toLong())
        }
        runPeriodicTestCheck()
    }

    private val continueButtonClick = View.OnClickListener {
        if (coninuteButton?.text == getString(R.string.ignition_restart_test)) {
            showAlertDialog()
        } else if (coninuteButton?.text == getString(R.string.ignition_test_continue)) {
            moveToNextFragment()
        }
    }

    private fun moveToNextFragment() {
        if (activity is LookupSubActivity) {
            if (passingReason?.userChoice == NEW_INSTALL) {
                val fetchClientFragment = FetchClientFragment()
                (activity as LookupSubActivity).passingReason = passingReason
                fragmentController.loadFragment(fetchClientFragment, fragmentManager, R.id.frameLayout, true)
            } else {
                (activity as LookupSubActivity).passingReason = passingReason
                val commonRepairFragment = CommonRepairFragment()
                fragmentController.loadFragment(commonRepairFragment, fragmentManager, R.id.frameLayout, true)
            }
        }
    }


    private fun showAlertDialog() {
        alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.ignition_testdialog_title))
                .setMessage(getString(R.string.ignition_testdialog_message))
                .setPositiveButton(getString(R.string.igtest_alert_dialog_yes)) { dialogInterface, i ->
                    coninuteButton?.visibility = View.GONE
                    rePlacefragment()
                }
                .setNegativeButton(getString(R.string.igtest_alert_dialog_no)) { dialogIntertface, j ->
                    restartTest()
                }
                .create()
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.show()
    }

    private fun rePlacefragment() {
        val ignitionTestFragment = IgnitionTestFragment()
        val bundle = Bundle()
        bundle.putString(getString(R.string.devicedetail_deviceid), deviceId)
        ignitionTestFragment.arguments = bundle
        fragmentController.replaceFragment(ignitionTestFragment, fragmentManager, R.id.frameLayout, false)
    }


    private fun runPeriodicTestCheck() {
        mRunnable?.run()
    }

    private fun startTimer() {
        countDownTimer = TimerCount(apiCallTime.toLong(), 1000, this).start() as TimerCount
        progressBar?.visibility = View.VISIBLE
        timeTextView?.visibility = View.VISIBLE
    }

    private fun getIgnitionData() {
        progressDialog?.show()
        if (AppUtils.isNetworkAvailable()) {
            if (!deviceId.isNullOrBlank() && !testStartTime.isNullOrBlank()) {
                ignitionTestViewModel?.getIgnitionTestData(deviceId!!, testStartTime!!)?.observe(this, Observer {
                    it.data?.let {
                        setRecyclerView(it)
                    }
                })
            }
        } else {
            progressDialog?.dismiss()
            Toaster.makeToast(getString(R.string.internet_not_available))
        }
    }


    private fun setRecyclerView(ignitionTestData: IgnitionTestData) {
        dismissProgressDialog()
        apiCallTime = ((ignitionTestData.timeToCallApi)?.let { it * 1000 }) ?: (10 * 1000)
        if (ignitionTestAdapter == null) {
            val linearLayoutManager = LinearLayoutManager(context)
            ignitionTestReyclerView = ignitionTestBinding?.ignitionTestRv
            ignitionTestReyclerView?.layoutManager = linearLayoutManager
            ignitionTestAdapter = IgnitionTestAdapter(ignitionTestData)
            ignitionTestReyclerView?.adapter = ignitionTestAdapter
            ignitionTestAdapter?.notifyDataSetChanged()
        } else {
            ignitionTestAdapter?.addAllNewData(ignitionTestData)
        }
        isFirstTime = false
        if (ignitionTestData.apiResult?.status == 1) {
            handler.removeCallbacks(mRunnable)
            progressBar?.visibility = View.GONE
            timeTextView?.visibility = View.GONE
            coninuteButton?.text = getString(R.string.ignition_test_continue)
            coninuteButton?.visibility = View.VISIBLE
        } else if (ignitionTestData.restartTest == true) {
            restartTest()
        }
    }

    private fun dismissProgressDialog() {
        val progressHandler = Handler()
        progressHandler.postDelayed(Runnable { progressDialog?.dismiss() }, 1000)
    }

    private fun restartTest() {
        isFirstTime = true
        handler.removeCallbacks(mRunnable)
        progressBar?.visibility = View.GONE
        timeTextView?.visibility = View.GONE
        coninuteButton?.text = getString(R.string.ignition_restart_test)
        coninuteButton?.visibility = View.VISIBLE
    }

    override fun bindView(view: View?) {
        view?.let {
            ignitionTestBinding = DataBindingUtil.bind(it)
        }
    }

    override fun getComponentFactory() {
    }

    override fun getTickTime(millisUntilFinished: Long) {
        timeTextView?.text = (millisUntilFinished / 1000).toString()
    }

    override fun onFinish() {
        progressBar?.visibility = View.GONE
        timeTextView?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(mRunnable)
    }

    fun removeHandler(removeTimer: Boolean) {
        if (removeTimer) {
            handler.removeCallbacksAndMessages(mRunnable)
        }
    }
}

