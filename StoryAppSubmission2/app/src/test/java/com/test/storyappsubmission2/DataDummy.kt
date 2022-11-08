package com.test.storyappsubmission2

import com.test.storyappsubmission2.data.remote.response.*

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

    fun generateDummyStoryResponse(): StoryResponse {
        return StoryResponse(
            generateDummyListStoryItem(),
            false,
            "Stories fetched successfully"
        )
    }

    fun generateDummyAddStoryResponse(): AddStoryResponse {
        return AddStoryResponse(
            false,
            "success",
        )
    }


    fun generateDummyResponseSignInSuccess(): SignInResponse {
        val loginResult = SignInResult(
            "user-H5CTfuK4Cvq5FK1B",
            "Maesuri Fauziah",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUg1Q1RmdUs0Q3ZxNUZLMUIiLCJpYXQiOjE2Njc0NDcyNjZ9.53P9_qZ5Y0ZxShzNsr14EXIaCg9Qfq1sNack8U-cT0s"
        )
        return SignInResponse(
            loginResult,
            error = false,
            message = "200"
        )
    }

    fun generateDummyResponseSignInErrorInvalidEmailFormat(): SignInResponse {
        return SignInResponse(
            null,
            true,
            "400"
        )
    }

    fun generateDummyResponseSignInErrorUserNotFound(): SignInResponse {
        return SignInResponse(
            null,
            true,
            "401"
        )
    }

    fun generateDummySignInResult(): SignInResult {
        return SignInResult(
            "Maesuri Fauziah",
            "mf77@gmail.com",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUg1Q1RmdUs0Q3ZxNUZLMUIiLCJpYXQiOjE2Njc0NDcyNjZ9.53P9_qZ5Y0ZxShzNsr14EXIaCg9Qfq1sNack8U-cT0s"
        )
    }

    fun generateDummySignInResultEmpty(): SignInResult {
        return SignInResult(
            "",
            "",
            ""
        )
    }

    fun generateDummyResponseSignUp(): SignUpResponse {
        return SignUpResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyResponseSignUpSuccess(): SignUpResponse {
        return SignUpResponse(
            error = false,
            message = "200"
        )
    }

    fun generateDummyResponseSignUpFailed(): SignUpResponse {
        return SignUpResponse(
            true,
            "400"
        )
    }
}