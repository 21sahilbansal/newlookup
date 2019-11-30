package com.loconav.lookup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.databinding.ItemIgnitionTestBinding
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.ignitontest.model.dataClass.TestNo

class IgnitionTestAdapter(var ignitionTestData: IgnitionTestData) : RecyclerView.Adapter<IgnitionTestAdapter.IgnitionTestViewHolder>() {
    class IgnitionTestViewHolder : RecyclerView.ViewHolder {
        var testTitle: TextView
        var batteryStatus: TextView
        var ignitionStatus: TextView
        var overAllTestStatus: TextView

        constructor(itemIignitionTestBinding: ItemIgnitionTestBinding) : super(itemIignitionTestBinding.root) {

            testTitle = itemIignitionTestBinding.testtitleTv
            batteryStatus = itemIignitionTestBinding.batteryStatusEv
            ignitionStatus = itemIignitionTestBinding.ignitionStatusEv
            overAllTestStatus = itemIignitionTestBinding.testStatusEv
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IgnitionTestViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ignition_test, parent, false)
        var itemIgnitionTestBinding: ItemIgnitionTestBinding = DataBindingUtil.bind(itemView)!!
          return IgnitionTestViewHolder(itemIgnitionTestBinding)
    }

    override fun getItemCount(): Int {
        return ignitionTestData.let { it.noOfTests!! }

    }

    override fun onBindViewHolder(holder: IgnitionTestViewHolder, position: Int) {
       bindDataToView(holder,position)
    }

    private fun bindDataToView(holder: IgnitionTestAdapter.IgnitionTestViewHolder, position: Int) {
       var testNo : TestNo ?=null
        when(position){
          1->testNo = ignitionTestData.ignitionTests?.test1
          2->testNo = ignitionTestData.ignitionTests?.test2
          3->testNo = ignitionTestData.ignitionTests?.test3
      }
        holder.testTitle.text = testNo?.name
        holder.batteryStatus.text = testNo?.battery
        holder.ignitionStatus.text = testNo?.ignition
        holder.overAllTestStatus.text = testNo?.status as String
    }


}