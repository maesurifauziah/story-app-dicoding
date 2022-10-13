package com.test.storyappsubmission1.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.storyappsubmission1.R
import com.test.storyappsubmission1.StoryAdapter
import com.test.storyappsubmission1.data.ListStoryItem
import com.test.storyappsubmission1.databinding.ActivityMainBinding
import com.test.storyappsubmission1.ui.addstory.AddNewStoryActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.dashboard_story)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListStory.addItemDecoration(itemDecoration)

        mainViewModel.storyList.observe(this) { listStory ->
            setReviewData(listStory)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnAddStory.setOnClickListener {
            val i = Intent(this@MainActivity, AddNewStoryActivity::class.java)
            startActivity(i)
        }
    }

    private fun setReviewData(listStory: List<ListStoryItem>) {
        val adapter = StoryAdapter(listStory as ArrayList<ListStoryItem>)
        binding.rvListStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}