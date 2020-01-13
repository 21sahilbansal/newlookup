package com.loconav.lookup.ignitontest.view

interface CountDownInterface {
    fun getTickTime(millisUntilFinished: Long)
    fun onFinish()
}