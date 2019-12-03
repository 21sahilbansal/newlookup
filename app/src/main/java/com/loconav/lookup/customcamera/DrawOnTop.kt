package com.loconav.lookup.customcamera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.View

import com.loconav.lookup.R

//  This class is used to develop overlay over the camera preview in the app
class DrawOnTop(context: Context, screenCenterX: Int, screenCenterY: Int, radius: Int) : View(context) {
     var screenCenterX = 0
     var screenCenterY = 0
     var radius = 50

    init {
        this.screenCenterX = screenCenterX
        this.screenCenterY = screenCenterY
        this.radius = radius
    }

    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = resources.getColor(R.color.white)
        paint.alpha = 130
        paint.style = Paint.Style.FILL
        canvas.drawPaint(paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawCircle(screenCenterX.toFloat(), screenCenterY.toFloat(), radius.toFloat(), paint)
        super.onDraw(canvas)
    }
}

