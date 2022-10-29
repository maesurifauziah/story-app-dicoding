package com.test.storyappsubmission2.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.storyappsubmission2.data.StoryRepository
import com.test.storyappsubmission2.data.remote.response.AddStoryResponse
import com.test.storyappsubmission2.data.remote.response.ListStoryItem
import com.test.storyappsubmission2.data.remote.response.StoryResponse
import com.test.storyappsubmission2.data.local.UserPreferenceDatastore
import com.test.storyappsubmission2.network.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel(private val repo: StoryRepository) : ViewModel() {

    fun getListStory(token: String) = repo.getListStory(token)

    fun postNewStory(token: String, imageFile: File, desc: String) = repo.postNewStory(token, imageFile, desc)

    fun getListMapsStory(token: String) = repo.getListMapsStory(token)

}