package com.test.storyappsubmission1.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.storyappsubmission1.R
import com.test.storyappsubmission1.databinding.ActivityMainBinding
import com.test.storyappsubmission1.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}