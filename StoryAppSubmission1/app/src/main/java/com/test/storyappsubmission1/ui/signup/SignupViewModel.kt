package com.test.storyappsubmission1.ui.signup

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.storyappsubmission1.data.SignUpResponse
import com.test.storyappsubmission1.data.UserPreferenceDatastore
import com.test.storyappsubmission1.network.ApiConfig
import com.test.storyappsubmission1.ui.signin.SigninViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val pref: UserPreferenceDatastore) : ViewModel() {
    var loading = MutableLiveData(View.GONE)
    val error = MutableLiveData("")
    private val TAG = SigninViewModel::class.simpleName

    fun signup(name: String, email: String, password: String) {
        loading.postValue(View.VISIBLE)
        val client = ApiConfig.getApiService().doSignup(name, email, password)
        client.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                // parsing manual error code API
                Log.e(TAG, "isSuccess:  ${response.message()}")
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }
}