package com.loconav.lookup.tutorial.view

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.lookup.R
import com.loconav.lookup.Toaster
import com.loconav.lookup.adapter.TutorialAdapter
import com.loconav.lookup.application.LookUpApplication
import com.loconav.lookup.base.BaseFragment
import com.loconav.lookup.customcamera.Callback
import com.loconav.lookup.databinding.FragmentTutorialBinding
import com.loconav.lookup.tutorial.model.dataClass.TutorialObject
import com.loconav.lookup.tutorial.viewModel.TutorialViewModel

class TutorialFragment : BaseFragment()  {

    private var fragmentTutorialBinding: FragmentTutorialBinding? = null
    private lateinit var tutorialViewModel: TutorialViewModel
    private var tutorialRecyclerView: RecyclerView? = null
    lateinit var tutorialAdapter: TutorialAdapter
    lateinit var tutorialObject: TutorialObject

    override fun setViewId(): Int {
        return R.layout.fragment_tutorial
    }

    override fun onFragmentCreated() {
        tutorialViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(LookUpApplication()).create(TutorialViewModel::class.java)
        getDataForTutorial()
    }

    private fun getDataForTutorial() {
        tutorialViewModel.getAllTutorial()?.observe(this, Observer {
            it?.data?.let {

                setUpRecyclerView(it)
            }
        })?:run {
            Toaster.makeToast("Some error occured")
        }
    }

    private fun setUpRecyclerView(tutoriallist: List<TutorialObject>) {
        var linearLayoutManager = LinearLayoutManager(context)
        fragmentTutorialBinding?.let {
            tutorialRecyclerView = it.tutorialRv
            tutorialRecyclerView?.layoutManager = linearLayoutManager
            tutorialAdapter = TutorialAdapter(
                    tutoriallist, Callback {
                tutorialObject = it as TutorialObject
                tutorialObject.url?.let {
                    val intent = Intent(Intent.ACTION_VIEW);
                    intent.data = Uri.parse(it)
                    startActivity(intent)
                }


            }
            )
            tutorialRecyclerView?.adapter = tutorialAdapter

        }
    }

    override fun bindView(view: View?) {
        view?.let { fragmentTutorialBinding = DataBindingUtil.bind(it) }
    }

    override fun getComponentFactory() {
    }
}