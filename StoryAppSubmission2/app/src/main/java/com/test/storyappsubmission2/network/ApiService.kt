package com.test.storyappsubmission2.network

import com.test.storyappsubmission2.data.AddStoryResponse
import com.test.storyappsubmission2.data.SignInResponse
import com.test.storyappsubmission2.data.SignUpResponse
//import com.test.storyappsubmission2.data.LoginResponse
//import com.test.storyappsubmission2.data.RegisterResponse
import com.test.storyappsubmission2.data.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("v1/stories")
    fun getListStory(
        @Header("Authorization") bearer: String?
    ): Call<StoryResponse>

    @Multipart
    @POST("/v1/stories")
    fun postNewStory(
        @Header("Authorization") bearer: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
    ): Call<AddStoryResponse>

    @FormUrlEncoded
    @POST("/v1/register")
    fun doSignup(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun doSignin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<SignInResponse>
}