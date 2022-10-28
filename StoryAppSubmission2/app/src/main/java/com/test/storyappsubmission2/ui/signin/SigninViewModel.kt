package com.test.storyappsubmission2.ui.signin

import androidx.lifecycle.*
import com.test.storyappsubmission2.data.StoryRepository
import kotlinx.coroutines.launch

class SigninViewModel(private val repo: StoryRepository) : ViewModel() {

    fun signin(email: String, password: String) = repo.signin(email, password)

    fun getUser() = repo.getUser()

    fun saveUser(userName: String, userId: String, userToken: String) {
        viewModelScope.launch {
            repo.saveUser(userName,userId,userToken)
        }
    }

    fun signout() {
        viewModelScope.launch {
            repo.signout()
        }
    }

}