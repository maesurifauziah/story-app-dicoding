package com.test.storyappsubmission2.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.test.storyappsubmission2.data.RemoteDataSource
import com.test.storyappsubmission2.data.StoryRepository
import com.test.storyappsubmission2.data.local.UserPreferenceDatastore
import com.test.storyappsubmission2.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val userPreferenceDatastore = UserPreferenceDatastore.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        return StoryRepository.getInstance(apiService, userPreferenceDatastore, remoteDataSource)
    }
}