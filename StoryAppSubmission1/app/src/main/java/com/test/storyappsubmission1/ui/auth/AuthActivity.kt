package com.test.storyappsubmission1.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.storyappsubmission1.R
import com.test.storyappsubmission1.databinding.ActivityAuthBinding
import com.test.storyappsubmission1.databinding.ActivityMainBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}