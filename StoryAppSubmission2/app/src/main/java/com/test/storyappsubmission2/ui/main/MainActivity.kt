package com.test.storyappsubmission2.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.storyappsubmission2.R
import com.test.storyappsubmission2.data.remote.response.ListStoryItem
import com.test.storyappsubmission2.databinding.ActivityMainBinding
import com.test.storyappsubmission2.ui.ViewModelFactory
import com.test.storyappsubmission2.ui.addstory.AddNewStoryActivity
import com.test.storyappsubmission2.ui.signin.SigninActivity
import com.test.storyappsubmission2.ui.signin.SigninViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val signinViewModel: SigninViewModel by viewModels {
        factory
    }
    private val mainViewModel: MainViewModel by viewModels {
        factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.dashboard_story)


        signinViewModel.getUser().observe(this){user->
            if (user.userId.isEmpty()){
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                mainViewModel.getListStory(user.token).observe(this){
                    it.listStory?.let { it1 -> setReviewData(it1) }
                }
            }
        }

//        mainViewModel.getListStory("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWhNVXNpNXg0WDJSWWduMUwiLCJpYXQiOjE2NjY5NDE5NTR9.dVXJVjp4qEZcAi1muwBKlcK19TkgG8sJPK0s7RqoKIE").observe(this){
//            setReviewData(it.listStory)
//        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListStory.addItemDecoration(itemDecoration)



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
                signinViewModel.signout()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setReviewData(listStory: List<ListStoryItem>?) {
        val adapter = StoryAdapter(listStory as ArrayList<ListStoryItem>)
        binding.rvListStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}