package com.loconav.lookup.customcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.loconav.lookup.R;

public class DrawOnTop extends View {
    int screenCenterX = 0;
    int screenCenterY = 0;
    int radius = 50;

    public DrawOnTop(Context context, int screenCenterX, int screenCenterY, int radius) {
        super(context);
        this.screenCenterX = screenCenterX;
        this.screenCenterY = screenCenterY;
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(getResources().getColor(R.color.white));
        p.setAlpha(130);
        p.setStyle(Paint.Style.FILL);
        canvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(screenCenterX, screenCenterY, radius, p);
        super.onDraw(canvas);
    }
}

