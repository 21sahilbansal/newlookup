package com.loconav.lookup.utils

import android.os.CountDownTimer
import com.loconav.lookup.ignitontest.view.CountDownInterface
import com.loconav.lookup.ignitontest.view.IgnitionTestFragment

class TimerCount : CountDownTimer {
    var igntionTimer :CountDownInterface
    constructor(totalTimer : Long,timelap : Long,ignitionTestFragment: IgnitionTestFragment) : super(totalTimer,timelap){
        igntionTimer = ignitionTestFragment
    }

    override fun onFinish() {
        igntionTimer.onFinish()
    }

    override fun onTick(millisUntilFinished: Long) {
      igntionTimer.getTickTime(millisUntilFinished)
    }

}