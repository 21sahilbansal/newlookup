package com.loconav.lookup.tutorial.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loconav.lookup.tutorial.Model.DataClass.TutorialData
import com.loconav.lookup.tutorial.Model.Repository.TutorialApiService
import com.loconav.lookup.utils.DataWrapper

class TutorialViewModel : ViewModel() {
    private val tutorialApiService : TutorialApiService ? = TutorialApiService()

    fun getAllTutorial(): MutableLiveData<DataWrapper<TutorialData>>? {
      return tutorialApiService?.let { it.getTutorialData()}

    }

}