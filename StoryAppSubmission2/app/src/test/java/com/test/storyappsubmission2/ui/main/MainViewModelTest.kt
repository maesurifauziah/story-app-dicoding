package com.test.storyappsubmission2.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.test.storyappsubmission2.DataDummy
import com.test.storyappsubmission2.MainDispatcherRule
import com.test.storyappsubmission2.data.StoryRepository
import com.test.storyappsubmission2.data.remote.response.AddStoryResponse
import com.test.storyappsubmission2.data.remote.response.ListStoryItem
import com.test.storyappsubmission2.data.remote.response.StoryResponse
import com.test.storyappsubmission2.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import com.test.storyappsubmission2.StoryPagingSource

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyStory = DataDummy.generateDummyListStoryItem()
    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUg1Q1RmdUs0Q3ZxNUZLMUIiLCJpYXQiOjE2Njc0NDcyNjZ9.53P9_qZ5Y0ZxShzNsr14EXIaCg9Qfq1sNack8U-cT0s"
    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Success`() = runTest {
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getAllStory(token)).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<ListStoryItem> = mainViewModel.getAllStory(token).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        advanceUntilIdle()
        Mockito.verify(storyRepository).getAllStory(token)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `when Post Story`() {
        val file = File("image")
        val expectedResponse = MutableLiveData<AddStoryResponse>()
        expectedResponse.postValue(dummyAddStoryResponse)
        Mockito.`when`(storyRepository.postNewStory(token, file, "description", null, null)).thenReturn(expectedResponse)
        val actualResponse = mainViewModel.postNewStory(token, file, "description", null, null).getOrAwaitValue()

        Mockito.verify(storyRepository).postNewStory(token, file, "description", null, null)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, expectedResponse.value)
    }

    @Test
    fun `when Get List Story Maps`() {
        val expectedResponse = MutableLiveData<StoryResponse>()
        expectedResponse.postValue(dummyStoryResponse)
        Mockito.`when`(storyRepository.getListMapsStory(token)).thenReturn(expectedResponse)
        val actualResponse = mainViewModel.getListMapsStory(token).getOrAwaitValue()

        Mockito.verify(storyRepository).getListMapsStory(token)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse.value, actualResponse)

    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}