package com.test.storyappsubmission1.ui.signin

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.test.storyappsubmission1.data.SignInResponse
import com.test.storyappsubmission1.data.SignInResult
import com.test.storyappsubmission1.data.UserPreferenceDatastore
import com.test.storyappsubmission1.network.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninViewModel(private val pref: UserPreferenceDatastore) : ViewModel() {

    var loading = MutableLiveData(View.GONE)
    val error = MutableLiveData("")
    val tempEmail = MutableLiveData("")
    private val TAG = SigninViewModel::class.simpleName


    val signinResult = MutableLiveData<SignInResponse>()

    fun getUser(): LiveData<SignInResult> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(userName: String, userId: String, userToken: String) {
        viewModelScope.launch {
            pref.saveUser(userName,userId,userToken)
        }
    }

    fun signout() {
        viewModelScope.launch {
            pref.signout()
        }
    }

    fun signin(email: String, password: String) {
        tempEmail.postValue(email) // temporary hold email for save user preferences
        loading.postValue(View.VISIBLE)
        val client = ApiConfig.getApiService().doSignin(email, password)
        client.enqueue(object : Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                // parsing manual error code API
                when (response.code()) {
                    200 -> signinResult.postValue(response.body())
                    else -> error.postValue("ERROR ${response.code()} : ${response.message()}")
                }

                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }

}