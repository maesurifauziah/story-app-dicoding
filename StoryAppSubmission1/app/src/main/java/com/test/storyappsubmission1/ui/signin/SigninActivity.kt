package com.test.storyappsubmission1.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.test.storyappsubmission1.data.UserPreferenceDatastore
import com.test.storyappsubmission1.databinding.ActivitySigninBinding
import com.test.storyappsubmission1.ui.ViewModelFactory
import com.test.storyappsubmission1.ui.main.MainActivity
import com.test.storyappsubmission1.ui.signup.SignupActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var signinViewModel: SigninViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()

        binding.haveAccountTextView.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        signinViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[SigninViewModel::class.java]

        signinViewModel.let { vmsignin ->
            vmsignin.signinResult.observe(this) { signin ->
                // success signin process triggered -> save preferences
                vmsignin.saveUser(
                    signin.loginResult.name,
                    signin.loginResult.userId,
                    signin.loginResult.token                )
            }
        }
    }

    private fun setupAction() {
        binding.signinButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmailLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edLoginPasswordLayout.error = "Masukkan password"
                }
                password.length <= 6 -> {
                    binding.edLoginPasswordLayout.error = "Password tidak boleh kurang dari 6"
                }

                else -> {
                    signinViewModel.signin(email, password)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 5000)
                }
            }
        }
    }
}