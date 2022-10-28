package com.test.storyappsubmission2.ui.signup

import androidx.lifecycle.ViewModel
import com.test.storyappsubmission2.data.StoryRepository

class SignupViewModel(private val repo: StoryRepository) : ViewModel() {

    fun signup(name: String, email: String, password: String) = repo.signup(name, email, password)
}