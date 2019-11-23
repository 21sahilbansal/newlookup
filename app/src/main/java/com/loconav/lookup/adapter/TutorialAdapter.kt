package com.loconav.lookup.adapter

import android.view.View
import android.view.ViewGroup
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseAdapter
import com.loconav.lookup.tutorial.Model.DataClass.TutorialData

class TutorialAdapter(var tutorialData: TutorialData) : BaseAdapter() {

    override fun getItemCount(): Int {
        return tutorialData.tutorial_list?.let { it.size }!!
    }

    override fun getDataAtPosition(position: Int): Any {
        return tutorialData?.let { it.tutorial_list?.let { it[position] } } as Any

    }

    override fun getLayoutIdForType(viewType: Int): Int {
        return R.layout.item_tutorial
    }

    override fun onItemClick(`object`: Any?, position: Int) {
    }

    override fun editHeightWidthItem(view: View?, parent: ViewGroup?) {
    }


}