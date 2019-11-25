package com.loconav.lookup.tutorial

import android.view.View

interface TutorialCallBack {
     fun onEventDone(`object`: Any)

    fun onExpendEvent(`object`: Any,view: View)
}