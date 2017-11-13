package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by prateek on 10/11/17.
 */

public class CustomActionBar {
    public ActionBar getActionBar(final AppCompatActivity activity, int homeImage, int title, final boolean clickAble){

        ActionBar bar = activity.getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(activity.
//                getResources().getColor(R.color.colorPrimaryDark)));
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(R.layout.action);
        TextView textView = (TextView) activity.findViewById(R.id.textView1);
        textView.setText(title);
        ImageView imageView = (ImageView)activity.findViewById(R.id.home);
        imageView.setImageResource(homeImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(clickAble) {
                       activity.onBackPressed();
                   }
            }
        });
        return bar;
    }
}
