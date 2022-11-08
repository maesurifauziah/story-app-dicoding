package com.test.storyappsubmission2.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert
import com.test.storyappsubmission2.data.StoryRepository
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.test.storyappsubmission2.DataDummy
import com.test.storyappsubmission2.data.remote.response.SignUpResponse
import com.test.storyappsubmission2.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class)
class SignupViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var signupViewModel: SignupViewModel
    private val dummyResponseSuccess = DataDummy.generateDummyResponseSignUpSuccess()
    private val dummyResponseFailed = DataDummy.generateDummyResponseSignUpFailed()
    private val dummyParamName = "Maesuri Fauziah"
    private val dummyParamEmail = "mf77@gmail.com"
    private val dummyParamPassword = "1111111"
    @Before
    fun setUp() {
        signupViewModel = SignupViewModel(storyRepository)
    }

    @Test
    fun `when Register Success`() {
        val expectedResponse = MutableLiveData<SignUpResponse>()
        expectedResponse.value =  dummyResponseSuccess
        `when`(storyRepository.signup(dummyParamName,dummyParamEmail, dummyParamPassword)).thenReturn(expectedResponse)
        val actualResponse = signupViewModel.signup(dummyParamName,dummyParamEmail, dummyParamPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).signup(dummyParamName,dummyParamEmail, dummyParamPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(dummyResponseSuccess, actualResponse)
    }

    @Test
    fun `when Register Failed`() {
        val expectedResponse = MutableLiveData<SignUpResponse>()
        expectedResponse.value =  dummyResponseFailed
        `when`(storyRepository.signup(dummyParamName,dummyParamEmail, "")).thenReturn(expectedResponse)
        val actualResponse = signupViewModel.signup(dummyParamName,dummyParamEmail, "").getOrAwaitValue()
        Mockito.verify(storyRepository).signup(dummyParamName,dummyParamEmail, "")
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(dummyResponseFailed, actualResponse)
    }
}