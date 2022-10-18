package com.test.storyappsubmission1.ui.signin

import android.util.Log
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData("")
    val message = MutableLiveData("")
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
        _isLoading.value = true
        val client = ApiConfig.getApiService().doSignin(email, password)
        client.enqueue(object : Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                // parsing manual error code API
                when (response.code()) {
                    200 -> {
                        signinResult.postValue(response.body())
                        message.postValue("200")
                    }
                    400 -> error.postValue("400")
                    401 -> error.postValue("401")
                    else -> error.postValue("ERROR ${response.code()} : ${response.message()}")
                }

                _isLoading.value = false
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }

}