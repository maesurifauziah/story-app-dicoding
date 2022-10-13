package com.test.storyappsubmission1.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.storyappsubmission1.data.AddStoryResponse
import com.test.storyappsubmission1.data.ListStoryItem
import com.test.storyappsubmission1.data.StoryResponse
import com.test.storyappsubmission1.network.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel() : ViewModel() {

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    val storyList: LiveData<List<ListStoryItem>> = _storyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }


    init{
        getListStory()
    }

    private fun getListStory() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListStory(bearer = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXo1SDRTaENXOFV4b0tETmQiLCJpYXQiOjE2NjUxNTIzMDd9.E4Wp8Z-ufWG3fX0OAbAXisNyXunNXjGf5epws907C4E")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyList.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

//    fun postNewStory(token: String, imageFile: File, desc: String) {
    fun postNewStory(imageFile: File, desc: String) {
        _isLoading.value = true

        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().postNewStory(bearer = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXo1SDRTaENXOFV4b0tETmQiLCJpYXQiOjE2NjUxNTIzMDd9.E4Wp8Z-ufWG3fX0OAbAXisNyXunNXjGf5epws907C4E", imageMultipart, description)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                _isLoading.value = false
                when (response.code()) {
                    401 -> "${response.code()} : Bad Request"
                    403 -> "${response.code()} : Forbidden"
                    404 -> "${response.code()} : Not Found"
                    else -> "${response.code()} : ${response.message()}"
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


}