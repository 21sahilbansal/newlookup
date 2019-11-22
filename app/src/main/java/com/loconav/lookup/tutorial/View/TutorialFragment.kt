package com.loconav.lookup.tutorial.View

import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentTutorialBinding
import com.loconav.lookup.tutorial.ViewModel.TutorialViewModel

class TutorialFragment : BaseFragment() {
    private var fragmentTutorialBinding :FragmentTutorialBinding ? = null
    private  lateinit  var tutorialViewModel : TutorialViewModel
    private var tutorialRecyclerView : RecyclerView?= null

    override fun setViewId(): Int {
        return  R.layout.fragment_tutorial
    }

    override fun onFragmentCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindView(view: View?) {
        view?.let { fragmentTutorialBinding = DataBindingUtil.bind(it) }
    }

    override fun getComponentFactory() {
    }
}