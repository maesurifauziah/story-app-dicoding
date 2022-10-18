package com.test.storyappsubmission1.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.storyappsubmission1.R
import com.test.storyappsubmission1.data.ListStoryItem
import com.test.storyappsubmission1.data.UserPreferenceDatastore
import com.test.storyappsubmission1.databinding.ActivityMainBinding
import com.test.storyappsubmission1.ui.ViewModelFactory
import com.test.storyappsubmission1.ui.addstory.AddNewStoryActivity
import com.test.storyappsubmission1.ui.signin.SigninActivity
import com.test.storyappsubmission1.ui.signin.SigninViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var signViewModel: SigninViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.dashboard_story)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[MainViewModel::class.java]

        signViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[SigninViewModel::class.java]

        signViewModel.getUser().observe(this){user->
            if (user.userId.isEmpty()){
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                mainViewModel.getListStory(user.token)
            }
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_logout -> {
                signViewModel.signout()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setReviewData(listStory: List<ListStoryItem>) {
        val adapter = StoryAdapter(listStory as ArrayList<ListStoryItem>)
        binding.rvListStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}