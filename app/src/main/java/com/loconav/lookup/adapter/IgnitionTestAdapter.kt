package com.loconav.lookup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.databinding.ItemIgnitionTestBinding
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.ignitontest.model.dataClass.TestNo

class IgnitionTestAdapter(var ignitionTestData: IgnitionTestData) : RecyclerView.Adapter<IgnitionTestAdapter.IgnitionTestViewHolder>() {
    class IgnitionTestViewHolder : RecyclerView.ViewHolder {
        val testTitle: TextView
        val batteryStatus: TextView
        val ignitionStatus: TextView
        val overAllTestStatus: TextView
        val message: TextView
        constructor(itemIignitionTestBinding: ItemIgnitionTestBinding) : super(itemIignitionTestBinding.root) {
            testTitle = itemIignitionTestBinding.testtitleTv
            batteryStatus = itemIignitionTestBinding.batteryStatusEv
            ignitionStatus = itemIignitionTestBinding.ignitionStatusEv
            overAllTestStatus = itemIignitionTestBinding.testStatusEv
            message = itemIignitionTestBinding.messageEv
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IgnitionTestViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ignition_test, parent, false)
        var itemIgnitionTestBinding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
        return IgnitionTestViewHolder(itemIgnitionTestBinding as ItemIgnitionTestBinding)
    }

    override fun getItemCount(): Int {
        return ignitionTestData.let { it.noOfTests!! }
    }

    override fun onBindViewHolder(holder: IgnitionTestViewHolder, position: Int) {
        bindDataToView(holder, position)
    }
    private fun bindDataToView(holder: IgnitionTestAdapter.IgnitionTestViewHolder, position: Int) {
        var testNo: TestNo? = null
        when (position) {
            0 -> testNo = ignitionTestData.ignitionTests?.test1
            1 -> testNo = ignitionTestData.ignitionTests?.test2
            2 -> testNo = ignitionTestData.ignitionTests?.test3
        }
        holder.testTitle.text = testNo?.name
        holder.batteryStatus.text = testNo?.battery
        holder.ignitionStatus.text = testNo?.ignition
        holder.overAllTestStatus.text = getTestStatus(testNo?.status)
        holder.message.text = testNo?.message

    }

    private fun getTestStatus(status: Int?): CharSequence? {
        if (status == 1) {
            return "Status Ok"
        } else {
            return "Status Not Ok"
        }
    }
}