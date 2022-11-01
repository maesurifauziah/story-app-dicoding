package com.test.storyappsubmission2.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.storyappsubmission2.data.remote.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertStory(quote: List<ListStoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM story")
     fun deleteAllStory()
}