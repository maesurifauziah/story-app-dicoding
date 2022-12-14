package com.test.storyappsubmission2.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.storyappsubmission2.R
import com.test.storyappsubmission2.adapter.LoadingStateAdapter
import com.test.storyappsubmission2.databinding.ActivityMainBinding
import com.test.storyappsubmission2.ui.ViewModelFactory
import com.test.storyappsubmission2.ui.addstory.AddNewStoryActivity
import com.test.storyappsubmission2.ui.map.MapsActivity
import com.test.storyappsubmission2.ui.signin.SigninActivity
import com.test.storyappsubmission2.ui.signin.SigninViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
//    private val signinViewModel: SigninViewModel by viewModels {
//        factory
//    }
//    private val mainViewModel: MainViewModel by viewModels {
//        factory
//    }
    private lateinit var signinViewModel: SigninViewModel
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.dashboard_story)

        val factory = ViewModelFactory.getInstance(this)
        signinViewModel = ViewModelProvider(this, factory)[SigninViewModel::class.java]
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val adapter = StoryAdapter()

        showLoading(true)

        signinViewModel.getUser().observe(this){user->
            if (user.userId.isEmpty()){
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                binding.rvListStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getAllStory(user.token).observe(this) {
                    Log.e("List", it.toString())
                    adapter.submitData(lifecycle, it)
                    showLoading(false)
                }
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListStory.addItemDecoration(itemDecoration)



        binding.btnAddStory.setOnClickListener {
            val i = Intent(this@MainActivity, AddNewStoryActivity::class.java)
            startActivity(i)
        }
        binding.btnGotoMap.setOnClickListener {
            val i = Intent(this@MainActivity, MapsActivity::class.java)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}