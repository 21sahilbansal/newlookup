package com.loconav.lookup.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseAdapter
import com.loconav.lookup.base.BaseArrayAdapter
import com.loconav.lookup.tutorial.Model.DataClass.TutorialData
import com.loconav.lookup.tutorial.Model.DataClass.TutorialObject

class TutorialAdapter(var tutorialData: TutorialData) : BaseArrayAdapter<TutorialData> {
     constructor()

    override fun setData(view: View?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}