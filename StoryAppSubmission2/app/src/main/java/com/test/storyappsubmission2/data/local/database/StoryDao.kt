package com.test.storyappsubmission2.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.storyappsubmission2.data.remote.response.ListStoryItem

//@Dao
interface StoryDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(story: List<ListStoryItem>)
//    @Query("DELETE from tstory")
//    fun delete(story: ListStoryItem)
//    @Query("SELECT * from tstory ORDER BY id ASC")
//    fun getAllNotes(): LiveData<List<ListStoryItem>>
}