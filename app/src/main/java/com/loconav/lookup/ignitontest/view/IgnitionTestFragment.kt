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
import com.loconav.lookup.utils.TimerCount


class IgnitionTestFragment : BaseFragment(), CountDownInterface {
    private lateinit var ignitionTestBinding: FragmentIgnitionTestBinding
    private var ignitionTestAdapter: IgnitionTestAdapter? = null
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
    private var apiCallTime: Int = 10 * 1000
    private lateinit var countDownTimer: TimerCount
    private var isFirstTime: Boolean = true
    private lateinit var passingReason: PassingReason
    private lateinit var progressDialog: ProgressDialog

    private val fragmentController = FragmentController()

    override fun setViewId(): Int {
        return com.loconav.lookup.R.layout.fragment_ignition_test
    }

    override fun onFragmentCreated() {
        deviceId = arguments?.getString("deviceDetail_deviceid")!!
        ignitionTestViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(LookUpApplication()).create(IgnitionTestViewModel::class.java)
        testStartTime = (System.currentTimeMillis() / 1000).toString()
        coninuteButton = ignitionTestBinding.ignitionButton
        passingReason = (activity as LookupSubActivity).passingReason;
        timeTextView = ignitionTestBinding.timerCount
        progressBar = ignitionTestBinding.progressbar
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Your test data is getting refreshed")
        progressDialog.setTitle("Refreshing Data")
        coninuteButton.setOnClickListener(continueButtonClick)
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
        if (coninuteButton.text == getString(R.string.ignition_Restart_Test)) {
            showAlertDialog()
        } else if (coninuteButton.text == getString(R.string.ignition_Test_Continue)) {
            moveToNextFragment()
        }
    }

    private fun moveToNextFragment() {
        if (passingReason.userChoice == NEW_INSTALL) {
            val fetchClientFragment = FetchClientFragment()
            (activity as LookupSubActivity).passingReason = passingReason
            fragmentController.loadFragment(fetchClientFragment, fragmentManager!!, R.id.frameLayout, true)
        } else {
            (activity as LookupSubActivity).passingReason = passingReason
            val commonRepairFragment = CommonRepairFragment()
            fragmentController.loadFragment(commonRepairFragment, fragmentManager, R.id.frameLayout, true)
        }

    }


    private fun showAlertDialog() {
        alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.ignition_TestDialog_Title))
                .setMessage(getString(R.string.ignition_TestDialog_Message))
                .setPositiveButton("Yes") { dialogInterface, i ->
                    coninuteButton.visibility = View.GONE
                    rePlacefragment()
                }
                .setNegativeButton(
                        "No") { dialogIntertface, j ->
                    restartTest()
                }
                .create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    private fun rePlacefragment() {
        val ignitionTestFragment = IgnitionTestFragment()
        val bundle = Bundle()
        bundle.putString("deviceDetail_deviceid", deviceId)
        ignitionTestFragment.arguments = bundle
        fragmentController.replaceFragment(ignitionTestFragment, fragmentManager, R.id.frameLayout, false)
    }


    private fun runPeriodicTestCheck() {
        mRunnable.run()
    }

    private fun startTimer() {
        countDownTimer = TimerCount(apiCallTime.toLong(), 1000, this).start() as TimerCount
        progressBar.visibility = View.VISIBLE
        timeTextView.visibility = View.VISIBLE
    }

    private fun getIgnitionData() {
        progressDialog.show()
        if (AppUtils.isNetworkAvailable()) {
            ignitionTestViewModel.getIgnitionTestData(deviceId, testStartTime)?.observe(this, Observer {
                it.data?.let {
                    setRecyclerView(it)
                }
            })
        } else {
            progressDialog.dismiss()
            Toaster.makeToast(getString(R.string.internet_not_available))
        }
    }


    private fun setRecyclerView(ignitionTestData: IgnitionTestData) {
          dismissProgressDialog()
        apiCallTime = ((ignitionTestData.timeToCallApi)?.let { it * 1000 }!!)
        if (ignitionTestAdapter == null) {
            val linearLayoutManager = LinearLayoutManager(context)
            ignitionTestReyclerView = ignitionTestBinding.ignitionTestRv
            ignitionTestReyclerView.layoutManager = linearLayoutManager
            ignitionTestAdapter = IgnitionTestAdapter(ignitionTestData)
            ignitionTestReyclerView.adapter = ignitionTestAdapter
            ignitionTestAdapter?.notifyDataSetChanged()
        } else {
            ignitionTestAdapter?.addAllNewData(ignitionTestData)
        }
        isFirstTime = false
        if (ignitionTestData.apiResult?.status == 1) {
            handler.removeCallbacks(mRunnable)
            progressBar.visibility = View.GONE
            timeTextView.visibility = View.GONE
            coninuteButton.text = getString(R.string.ignition_Test_Continue)
            coninuteButton.visibility = View.VISIBLE
        } else if (ignitionTestData.restartTest!!) {
            restartTest()
        }
    }

    private fun dismissProgressDialog() {
        val progressHandler = Handler()
        progressHandler.postDelayed(Runnable { progressDialog.dismiss() }, 1000)
    }

    private fun restartTest() {
        isFirstTime = true
        handler.removeCallbacks(mRunnable)
        progressBar.visibility = View.GONE
        timeTextView.visibility = View.GONE
        coninuteButton.text = getString(R.string.ignition_Restart_Test)
        coninuteButton.visibility = View.VISIBLE
    }

    override fun bindView(view: View?) {
        view?.let {
            ignitionTestBinding = DataBindingUtil.bind(it)!!
        }
    }

    override fun getComponentFactory() {
    }

    override fun getTickTime(millisUntilFinished: Long) {
        timeTextView.text = (millisUntilFinished / 1000).toString()
    }

    override fun onFinish() {
        progressBar.visibility = View.GONE
        timeTextView.visibility = View.GONE
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

