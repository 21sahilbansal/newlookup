package com.loconav.lookup.adapter

import android.view.View
import android.view.ViewGroup
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseAdapter
import com.loconav.lookup.tutorial.model.dataClass.TutorialObject
import com.loconav.lookup.tutorial.TutorialCallBack


class TutorialAdapter(var tutorialList: List<TutorialObject>, var callback: TutorialCallBack) : BaseAdapter() {
    override fun onExpendableItemClick(`object`: Any?, position: Int, view: View?) {
        if (`object` != null && view != null) {
            callback.onExpendEvent(`object`,view)
        }
    }

    override fun exapandOnClick(): Boolean {
        return true
    }

    override fun getItemCount(): Int {
        return tutorialList?.let { it.size }
    }

    override fun getDataAtPosition(position: Int): Any {
        return tutorialList?.let { it[position] }

    }

    override fun getLayoutIdForType(viewType: Int): Int {
        return R.layout.item_tutorial
    }

    override fun onItemClick(`object`: Any?, position: Int) {
        callback.onEventDone(`object`!!)

    }

    override fun editHeightWidthItem(view: View?, parent: ViewGroup?) {
    }


}