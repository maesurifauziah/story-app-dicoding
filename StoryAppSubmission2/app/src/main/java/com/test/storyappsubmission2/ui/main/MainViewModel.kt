package com.test.storyappsubmission2.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.storyappsubmission2.data.StoryRepository
import java.io.File

class MainViewModel(private val repo: StoryRepository) : ViewModel() {

    val coordLat = MutableLiveData(0.0)
    val coordLon = MutableLiveData(0.0)
    val isLocation = MutableLiveData(false) // init for location new story not selected

    fun getListStory(token: String) = repo.getListStory(token)

    fun postNewStory(token: String, imageFile: File, desc: String, lon: String?, lat: String?) = repo.postNewStory(token, imageFile, desc, lon, lat)

    fun getListMapsStory(token: String) = repo.getListMapsStory(token)

}