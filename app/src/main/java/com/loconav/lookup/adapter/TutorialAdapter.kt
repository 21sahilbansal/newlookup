package com.loconav.lookup.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.loconav.lookup.base.BaseAdapter
import com.loconav.lookup.customcamera.Callback
import com.loconav.lookup.tutorial.model.dataClass.TutorialObject
import android.view.animation.AnimationUtils
import com.loconav.lookup.R


class TutorialAdapter(var tutorialList: List<TutorialObject>, var callback: Callback) : BaseAdapter() {

    override fun getItemCount(): Int {
        return tutorialList.size
    }

    override fun getDataAtPosition(position: Int): Any {
        return tutorialList[position]

    }

    override fun getLayoutIdForType(viewType: Int): Int {
        return R.layout.item_tutorial
    }

    override fun onItemClick(`object`: Any, position: Int, view: View) {

       if( view.findViewById<View>(R.id.tutorial_description_layout)?.visibility == View.VISIBLE){
          val slideUp = AnimationUtils.loadAnimation(view.context,R.anim.slide_up)
           view.findViewById<View>(R.id.tutorial_description_layout)?.visibility = View.GONE
           view.findViewById<View>(R.id.tutorial_description_layout)?.startAnimation(slideUp)
           view.findViewById<ImageView>(R.id.tutorial_slide_iv)?.setImageDrawable(view.context.resources.getDrawable(R.drawable.ic_expand_more_black_24dp))
       }
        else{
           val slideDown = AnimationUtils.loadAnimation(view.context,R.anim.slide_down)
           view.findViewById<View>(R.id.tutorial_description_layout)?.visibility = View.VISIBLE
           view.findViewById<View>(R.id.tutorial_description_layout)?.startAnimation(slideDown)
           view.findViewById<ImageView>(R.id.tutorial_slide_iv)?.setImageDrawable(view.context.resources.getDrawable(R.drawable.ic_expand_less_black_24dp))
       }
        view.findViewById<Button>(R.id.tutorial_kmore_button).setOnClickListener({
            callback.onEventDone(`object`)
        })
    }

    override fun editHeightWidthItem(view: View?, parent: ViewGroup?) {
    }
}