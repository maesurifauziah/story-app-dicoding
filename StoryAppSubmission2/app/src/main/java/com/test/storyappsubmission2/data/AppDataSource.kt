package com.test.storyappsubmission2.data

import androidx.lifecycle.LiveData
import com.test.storyappsubmission2.data.remote.response.*
import java.io.File

interface AppDataSource {
    fun getUser(): LiveData<SignInResult>
    fun signin(email: String, password: String): LiveData<SignInResponse>
    fun signup(name: String, email: String, password: String): LiveData<SignUpResponse>
    fun getListStory(token: String): LiveData<StoryResponse>
    fun postNewStory(token: String, imageFile: File, desc: String, lon: String?, lat: String?): LiveData<AddStoryResponse>
    fun getListMapsStory(token: String): LiveData<StoryResponse>
}