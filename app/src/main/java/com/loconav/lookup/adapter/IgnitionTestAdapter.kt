package com.loconav.lookup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.application.LookUpApplication
import com.loconav.lookup.databinding.ItemIgnitionTestBinding
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.ignitontest.model.dataClass.TestNo

class IgnitionTestAdapter(var ignitionTestData: IgnitionTestData?) : RecyclerView.Adapter<IgnitionTestAdapter.IgnitionTestViewHolder>() {
    class IgnitionTestViewHolder : RecyclerView.ViewHolder {
        var testTitle: TextView
        var batteryStatus: TextView
        var ignitionStatus: TextView
        var overAllTestStatus: TextView
        var message: TextView
        var description: TextView

        constructor(itemIignitionTestBinding: ItemIgnitionTestBinding) : super(itemIignitionTestBinding.root) {
            testTitle = itemIignitionTestBinding.testtitleTv
            batteryStatus = itemIignitionTestBinding.batteryStatusEv
            ignitionStatus = itemIignitionTestBinding.ignitionStatusEv
            overAllTestStatus = itemIignitionTestBinding.testStatusEv
            message = itemIignitionTestBinding.messageEv
            description = itemIignitionTestBinding.descriptionTv
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IgnitionTestViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ignition_test, parent, false)
        var itemIgnitionTestBinding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
        return IgnitionTestViewHolder(itemIgnitionTestBinding as ItemIgnitionTestBinding)
    }

    override fun getItemCount(): Int {
        return ignitionTestData.let { it?.noOfTests?:0 }
    }

    override fun onBindViewHolder(holder: IgnitionTestViewHolder, position: Int) {
        bindDataToView(holder, position)
    }

    private fun bindDataToView(holder: IgnitionTestAdapter.IgnitionTestViewHolder, position: Int) {
        var testNo: TestNo? = null
        when (position) {
            0 -> testNo = ignitionTestData?.ignitionTests?.test1
            1 -> testNo = ignitionTestData?.ignitionTests?.test2
            2 -> testNo = ignitionTestData?.ignitionTests?.test3
        }
        holder.testTitle.text = testNo?.name
        holder.description.text = testNo?.description
        holder.batteryStatus.text = testNo?.battery
        holder.ignitionStatus.text = testNo?.ignition
        holder.overAllTestStatus.text = getTestStatus(testNo?.status)
        holder.message.text = testNo?.message
        setColour(holder, testNo?.status)

    }

    private fun setColour(holder: IgnitionTestAdapter.IgnitionTestViewHolder, status: Int?) {
        val greenTextColor: Int = getColor(LookUpApplication.getInstance(), R.color.lightgreen)
        val redTextColor: Int = getColor(LookUpApplication.getInstance(), R.color.red)

        if (status == 1) {
            holder.batteryStatus.setTextColor(greenTextColor)
            holder.message.setTextColor(greenTextColor)
            holder.overAllTestStatus.setTextColor(greenTextColor)
            holder.ignitionStatus.setTextColor(greenTextColor)
        } else if (status == 2) {
            holder.batteryStatus.setTextColor(redTextColor)
            holder.message.setTextColor(redTextColor)
            holder.overAllTestStatus.setTextColor(redTextColor)
            holder.ignitionStatus.setTextColor(redTextColor)
        }


    }



    private fun getTestStatus(status: Int?): CharSequence? {
        if (status == 1) {


            return "Status Ok"
        } else {
            return "Status Not Ok"
        }
    }

    fun clearData(){
        ignitionTestData = null
    }

    fun addAllNewData(data :  IgnitionTestData){
        clearData()
        ignitionTestData =  data
        notifyDataSetChanged()
    }
}