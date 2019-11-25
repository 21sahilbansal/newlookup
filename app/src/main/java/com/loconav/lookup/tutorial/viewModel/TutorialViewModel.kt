package com.loconav.lookup.tutorial.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loconav.lookup.tutorial.model.dataClass.TutorialData
import com.loconav.lookup.tutorial.model.dataClass.TutorialObject
import com.loconav.lookup.tutorial.model.repository.TutorialApiService
import com.loconav.lookup.utils.DataWrapper

class TutorialViewModel : ViewModel() {
    private val tutorialApiService : TutorialApiService ? = TutorialApiService()

    fun getAllTutorial(): MutableLiveData<DataWrapper<List<TutorialObject>>>? {
      return tutorialApiService?.let { it.getTutorialData()}

    }

}