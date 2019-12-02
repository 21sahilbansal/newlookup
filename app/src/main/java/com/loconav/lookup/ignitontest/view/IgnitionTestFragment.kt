package com.loconav.lookup.ignitontest.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
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
import java.util.logging.Handler

class IgnitionTestFragment : BaseFragment() {
    private lateinit var ignitionTestBinding: FragmentIgnitionTestBinding
    private lateinit var ignitionTestAdapter: IgnitionTestAdapter
    private lateinit var ignitionTestViewModel: IgnitionTestViewModel
    private lateinit var ignitionTestReyclerView: RecyclerView
    private lateinit var alertDialog: Dialog
    private lateinit var deviceId: String
    private lateinit var testStartTime: String
    private lateinit var coninuteButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var mRunnable : Runnable
    private var handler : Handler = Handler(Looper.getMainLooper())
    private var apiCallTime : Int = 10 * 1000

    override fun setViewId(): Int {
        return R.layout.fragment_ignition_test
    }

    override fun onFragmentCreated() {
        deviceId = arguments?.getString("deviceDetail_deviceid")!!
        ignitionTestViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(LookUpApplication()).create(IgnitionTestViewModel::class.java)
        testStartTime = (System.currentTimeMillis() / 1000).toString()
        coninuteButton = ignitionTestBinding.ignitionButton
        progressBar = ignitionTestBinding.progressbar
        showAlertDialog()
    }

    private fun showAlertDialog() {
        alertDialog = AlertDialog.Builder(context)
                .setTitle("Ignition Test")
                .setMessage("Press ok to start the ignition test")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    runPeriodicTestCheck()
                   progressBar.visibility= View.VISIBLE
                }
                .setNegativeButton("No") { dialogIntertface, j ->
                }
                .create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    private fun runPeriodicTestCheck() {
      mRunnable =  Runnable {


         handler.
      }
        mRunnable.run()
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
        }


private fun runPeriodicCheck() {
    mRunnable = Runnable {
        counter = counter + 1
        if (counter <= 5
        ) {
            progressBar?.setProgress(counter * 5)
            progressTextView?.text = (counter * 5).toString() + "/" + progressBar?.max
            progressBar?.visibility = View.VISIBLE
            progressTextView?.visibility = View.VISIBLE
            observeData()
        }
        if (counter == 5) {
            testTimeOver = true
        }
        handler.postDelayed(mRunnable, 30 * 1000)
    }
    mRunnable.run()

}