package com.test.storyappsubmission2.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.test.storyappsubmission2.data.remote.response.*
import java.io.File

interface AppDataSource {
     fun getUser(): LiveData<SignInResult>
     fun signin(email: String, password: String): LiveData<SignInResponse>
     fun signup(name: String, email: String, password: String): LiveData<SignUpResponse>
     fun postNewStory(token: String, imageFile: File, desc: String, lon: String?, lat: String?): LiveData<AddStoryResponse>
     fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>>
     fun getListMapsStory(token: String): LiveData<StoryResponse>
}