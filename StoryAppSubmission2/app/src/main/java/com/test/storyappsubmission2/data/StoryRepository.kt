package com.test.storyappsubmission2.data

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.test.storyappsubmission2.data.local.UserPreferenceDatastore
import com.test.storyappsubmission2.data.remote.response.*
import com.test.storyappsubmission2.network.ApiService
import java.io.File

class StoryRepository(
    private val apiService: ApiService,
    private val pref: UserPreferenceDatastore,
    private val remoteDataSource: RemoteDataSource
) : AppDataSource {

    override  fun getUser(): LiveData<SignInResult> {
        return pref.getUser().asLiveData()
    }

    suspend fun saveUser(userName: String, userId: String, userToken: String) {
        pref.saveUser(userName,userId,userToken)
    }

    suspend fun signout() {
        pref.signout()
    }


    override  fun signin(email: String, password: String): LiveData<SignInResponse> {
        val signinResponse2 = MutableLiveData<SignInResponse>()

        remoteDataSource.signin(object : RemoteDataSource.SigninCallback{
            override fun onSignin(signinResponse: SignInResponse) {
                signinResponse2.postValue(signinResponse)
            }
        }, email, password)
        return signinResponse2
    }

    override  fun signup(name: String, email: String, password: String): LiveData<SignUpResponse> {
        val registerResponse = MutableLiveData<SignUpResponse>()

        remoteDataSource.signup(object : RemoteDataSource.SignupCallback{
            override fun onSignup(signupResponse: SignUpResponse) {
                registerResponse.postValue(signupResponse)
            }
        }, name, email, password)
        return registerResponse
    }

//    override fun getListStory(token: String): LiveData<PagingData<StoryResponse>> {
//        val storyResponse2 = MutableLiveData<StoryResponse>()
//        remoteDataSource.getListStory(object : RemoteDataSource.GetListStoryCallback{
//            override fun onStoryLoad(storyResponse: StoryResponse) {
//                storyResponse2.postValue(storyResponse)
//            }
//        }, token)
//        return storyResponse2
//    }
    override  fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            pagingSourceFactory = {
                StoryPagingSource(
                    api = apiService,
                    dataStoreRepository = pref
                )
            }
        ).liveData
    }



    override  fun postNewStory(token: String, imageFile: File, desc: String, lon: String?, lat: String?): LiveData<AddStoryResponse> {
        val uploadResponseStatus = MutableLiveData<AddStoryResponse>()

        remoteDataSource.postNewStory(object : RemoteDataSource.AddNewStoryCallback{
            override fun onAddStory(addStoryResponse: AddStoryResponse) {
                uploadResponseStatus.postValue(addStoryResponse)
            }
        }, token, imageFile, desc, lon, lat)
        return uploadResponseStatus
    }

    override fun getListMapsStory(token: String): LiveData<StoryResponse> {
        val storyResponse2 = MutableLiveData<StoryResponse>()
        remoteDataSource.getListMapsStory(object: RemoteDataSource.GetListMapsStoryCallback{
            override fun onMapsStoryLoad(storyResponse: StoryResponse) {
                storyResponse2.postValue(storyResponse)
            }

        },token)
        return storyResponse2
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: UserPreferenceDatastore,
            remoteDataSource: RemoteDataSource
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref, remoteDataSource)
            }.also { instance = it }
    }


}