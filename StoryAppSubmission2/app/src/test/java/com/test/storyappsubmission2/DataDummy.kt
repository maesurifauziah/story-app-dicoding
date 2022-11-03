package com.test.storyappsubmission2

import com.test.storyappsubmission2.data.remote.response.ListStoryItem
import com.test.storyappsubmission2.data.remote.response.SignInResponse
import com.test.storyappsubmission2.data.remote.response.SignInResult
import com.test.storyappsubmission2.data.remote.response.SignUpResponse

object DataDummy {

    fun generateDummyListStoryItem(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "Mae $i",
                "ini adalah Uni testing $i",
                "2022-01-08T06:34:18.598Z",
                -10.212,
                -10.212
            )
            items.add(quote)
        }
        return items
    }

//    {
//        "error": false,
//        "message": "success",
//        "loginResult": {
//            "userId": "user-H5CTfuK4Cvq5FK1B",
//            "name": "Maesuri Fauziah",
//            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUg1Q1RmdUs0Q3ZxNUZLMUIiLCJpYXQiOjE2Njc0NDcyNjZ9.53P9_qZ5Y0ZxShzNsr14EXIaCg9Qfq1sNack8U-cT0s"
//        }
//    }
//
    fun generateDummyResponseSignInSuccess(): SignInResponse {
        val loginresult = SignInResult(
            "user-H5CTfuK4Cvq5FK1B",
            "Maesuri Fauziah",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUg1Q1RmdUs0Q3ZxNUZLMUIiLCJpYXQiOjE2Njc0NDcyNjZ9.53P9_qZ5Y0ZxShzNsr14EXIaCg9Qfq1sNack8U-cT0s"
        )
        val signIn = SignInResponse(
            loginresult,
            false,
            "success"
        )
        return signIn
    }

    fun generateDummyResponseSignInFailed(): SignInResponse {
       return SignInResponse(
           null,
           false,
           "success"
       )
    }

    fun generateDummyResponseSignUpSuccess(): SignUpResponse {
        return SignUpResponse(
            false,
            "User created"
        )
    }

    fun generateDummyResponseSignUpFailed(): SignUpResponse {
        return SignUpResponse(
            true,
            "Email is already taken"
        )
    }
}