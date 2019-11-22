package com.loconav.lookup.tutorial.View

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.adapter.TutorialAdapter
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.databinding.FragmentTutorialBinding
import com.loconav.lookup.tutorial.Model.DataClass.TutorialData
import com.loconav.lookup.tutorial.ViewModel.TutorialViewModel
import com.loconav.lookup.utils.DataWrapper

class TutorialFragment : BaseFragment() {
    private var fragmentTutorialBinding :FragmentTutorialBinding ? = null
    private  lateinit  var tutorialViewModel : TutorialViewModel
    private var tutorialRecyclerView : RecyclerView?= null
    public var tutorialAdapter : TutorialAdapter ? =null

    override fun setViewId(): Int {
        return  R.layout.fragment_tutorial
    }

    override fun onFragmentCreated() {

        tutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel::class.java)
        getDataForTutorial()
    }

    private fun getDataForTutorial() {
        tutorialViewModel.getAllTutorial()?.observe(this, Observer {it?.data?.let {
         setUpRecyclerView()

        }})
    }

    private fun setUpRecyclerView() {
        var linearLayoutManager : LinearLayoutManager = LinearLayoutManager(context)
        fragmentTutorialBinding?.let{it.tutorialRv.layoutManager = linearLayoutManager
        it.tutorialRv.adapter = tutorialAdapter}


    }

    override fun bindView(view: View?) {
        view?.let { fragmentTutorialBinding = DataBindingUtil.bind(it) }
    }

    override fun getComponentFactory() {
    }
}