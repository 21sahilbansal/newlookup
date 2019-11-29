package com.loconav.lookup.adapter

import android.view.View
import android.view.ViewGroup
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseAdapter
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData

class IgnitionTestAdapter(var ignitionTestData: IgnitionTestData) : BaseAdapter() {
    override fun getItemCount(): Int {
        return ignitionTestData.no_of_test_headers
    }

    override fun getDataAtPosition(position: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutIdForType(viewType: Int): Int {
        return  R.layout.item_ignition_test
    }

    override fun onItemClick(`object`: Any?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editHeightWidthItem(view: View?, parent: ViewGroup?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}