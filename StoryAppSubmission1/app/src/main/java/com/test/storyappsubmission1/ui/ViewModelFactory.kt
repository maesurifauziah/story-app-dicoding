package com.test.storyappsubmission1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.storyappsubmission1.data.UserPreferenceDatastore
import com.test.storyappsubmission1.ui.main.MainViewModel
import com.test.storyappsubmission1.ui.signin.SigninViewModel
import com.test.storyappsubmission1.ui.signup.SignupViewModel

class ViewModelFactory(private val pref: UserPreferenceDatastore) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}