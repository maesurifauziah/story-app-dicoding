package com.test.storyappsubmission2.ui.signin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.test.storyappsubmission2.DataDummy
import com.test.storyappsubmission2.data.StoryRepository
import com.test.storyappsubmission2.data.remote.response.SignInResponse
import com.test.storyappsubmission2.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SigninViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var signinViewModel: SigninViewModel
    private val dummyResponseSuccess = DataDummy.generateDummyResponseSignInSuccess()
    private val dummyResponseErrorInvalidEmailFormat = DataDummy.generateDummyResponseSignInErrorInvalidEmailFormat()
    private val dummyResponseErrorUserNotFound = DataDummy.generateDummyResponseSignInErrorUserNotFound()
//    private val dummySignInResult = DataDummy.generateDummySignInResult()
//    private val dummySignInResultEmpty = DataDummy.generateDummySignInResultEmpty()
    private val dummyParamEmail = "mf77@gmail.com"
    private val dummyParamPassword = "1111111"
    @Before
    fun setUp() {
        signinViewModel = SigninViewModel(storyRepository)
    }

    @Test
    fun `when Login Success`() {
        val expectedResponse = MutableLiveData<SignInResponse>()
        expectedResponse.value = dummyResponseSuccess
        Mockito.`when`(storyRepository.signin(dummyParamEmail, dummyParamPassword)).thenReturn(expectedResponse)
        val actualResponse = signinViewModel.signin(dummyParamEmail, dummyParamPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).signin(dummyParamEmail, dummyParamPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse.value, actualResponse)
    }

    @Test
    fun `when Error Invalid Email Format`() {
        val expectedResponse = MutableLiveData<SignInResponse>()
        expectedResponse.value = dummyResponseErrorInvalidEmailFormat
        Mockito.`when`(storyRepository.signin("mf77@gmail", dummyParamPassword)).thenReturn(expectedResponse)
        val actualResponse = signinViewModel.signin("mf77@gmail", dummyParamPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).signin("mf77@gmail", dummyParamPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, expectedResponse.value)
    }

    @Test
    fun `when Error User Not Found`() {
        val expectedResponse = MutableLiveData<SignInResponse>()
        expectedResponse.value = dummyResponseErrorUserNotFound
        Mockito.`when`(storyRepository.signin("xx", "xx")).thenReturn(expectedResponse)
        val actualResponse = signinViewModel.signin("xx", "xx").getOrAwaitValue()
        Mockito.verify(storyRepository).signin("xx", "xx")
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, expectedResponse.value)
    }

//    @Test
//    fun `when Get User`() {
//        val expectedResponse = MutableLiveData<SignInResponse>()
//        expectedResponse.value = dummyResponseErrorUserNotFound
//        Mockito.`when`(storyRepository.signin("xx", "xx")).thenReturn(expectedResponse)
//        val actualResponse = signinViewModel.signin("xx", "xx").getOrAwaitValue()
//        Mockito.verify(storyRepository).signin("xx", "xx")
//       Assert.assertNotNull(actualResponse)
//       Assert.assertEquals(actualResponse, expectedResponse.value)
//    }





}