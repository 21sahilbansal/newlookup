package com.loconav.lookup.tutorial.View

import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.adapter.TutorialAdapter
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentTutorialBinding
import com.loconav.lookup.tutorial.ViewModel.TutorialViewModel

class TutorialFragment : BaseFragment() {
    private var fragmentTutorialBinding :FragmentTutorialBinding ? = null
    private  lateinit  var tutorialViewModel : TutorialViewModel
    private var tutorialRecyclerView : RecyclerView?= null
    public var tutorialAdapter : TutorialAdapter ? =null

    override fun setViewId(): Int {
        return  R.layout.fragment_tutorial
    }

    override fun onFragmentCreated() {
        getDataForTutorial()
        setUpRecyclerView()
    }

    private fun getDataForTutorial() {
    }

    private fun setUpRecyclerView() {
    }

    override fun bindView(view: View?) {
        view?.let { fragmentTutorialBinding = DataBindingUtil.bind(it) }
    }

    override fun getComponentFactory() {
    }
}