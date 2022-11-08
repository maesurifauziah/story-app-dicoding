package com.test.storyappsubmission2.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.test.storyappsubmission2.DataDummy
import com.test.storyappsubmission2.MainDispatcherRule
import com.test.storyappsubmission2.data.local.UserPreferenceDatastore
import com.test.storyappsubmission2.data.local.database.StoryRoomDatabase
import com.test.storyappsubmission2.data.remote.response.ListStoryItem
import com.test.storyappsubmission2.network.ApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import com.test.storyappsubmission2.StoryPagingSource
import com.test.storyappsubmission2.ui.main.StoryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource
    @Mock
    private lateinit var storyRoomDatabase: StoryRoomDatabase
    @Mock
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var userPreferenceDatastore: UserPreferenceDatastore

    @Mock
    private lateinit var storyRepository: StoryRepository

    private val dummyResponseSignUp = DataDummy.generateDummyResponseSignUp()
    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()
    private val dummyResponseSignIn = DataDummy.generateDummyResponseSignInSuccess()
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()
    private val dummyStory = DataDummy.generateDummyListStoryItem()

    private val dummyParamName = "Maesuri Fauziah"
    private val dummyParamEmail = "mf77@gmail.com"
    private val dummyParamPassword = "1111111"
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUg1Q1RmdUs0Q3ZxNUZLMUIiLCJpYXQiOjE2Njc0NDcyNjZ9.53P9_qZ5Y0ZxShzNsr14EXIaCg9Qfq1sNack8U-cT0s"


    @Before
    fun setup(){
        storyRepository = StoryRepository(apiService, userPreferenceDatastore, remoteDataSource, storyRoomDatabase)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Sign Up response`() = runTest {
        val expectedResponse = dummyResponseSignUp
        storyRepository.signup(dummyParamName, dummyParamEmail, dummyParamPassword).observeForever { actualResponse ->
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(
                expectedResponse,
                actualResponse
            )
        }
    }

    @Test
    fun `when Sign in response`() = runTest {
        val expectedResponse = dummyResponseSignIn
        storyRepository.signin(dummyParamEmail, dummyParamPassword).observeForever { actualResponse ->
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(
                expectedResponse,
                actualResponse
            )
        }
    }

    @Test
    fun `when Add Story Response`() = runTest {
        val expectedResponse = dummyAddStoryResponse
        val file = File("image")
        storyRepository.postNewStory(token, file, "description", "0.0", "0.0").observeForever { actualResponse ->
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(
                expectedResponse,
                actualResponse
            )
        }
    }

    @Test
    fun `when Get List Map Story Response`() = runTest {
        val expectedResponse = dummyStoryResponse
        storyRepository.getListMapsStory(token).observeForever { actualResponse ->
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(
                expectedResponse,
                actualResponse
            )
        }
    }

    @Test
    fun `when Get List All Story Response`() = runTest {
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        storyRepository.getAllStory(token).observeForever {
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            CoroutineScope(Dispatchers.IO).launch {
                differ.submitData(it)
            }
            advanceUntilIdle()
            verify(storyRepository).getAllStory(token)
            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(differ.snapshot().size, dummyStory.size)
        }

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}

