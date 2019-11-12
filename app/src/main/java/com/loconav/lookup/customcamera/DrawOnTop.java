package com.loconav.lookup.customcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.loconav.lookup.R;

public class DrawOnTop extends View {
    int screenCenterX = 0;
    int screenCenterY = 0;
     int radius = 50;
    public DrawOnTop(Context context, int screenCenterX, int screenCenterY) {
        super(context);
        this.screenCenterX = screenCenterX;
        this.screenCenterY = screenCenterY;
        this.radius = screenCenterY/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(getResources().getColor(R.color.grey));
        p.setStyle(Paint.Style.FILL);
        canvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
       // canvas.drawRect();
   //     canvas.drawRoundRect(circleRect, radius, radius, p);;
       canvas.drawCircle(screenCenterX, screenCenterY, radius, p);
        super.onDraw(canvas);
    }
}

