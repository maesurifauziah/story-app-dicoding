package com.test.storyappsubmission1.ui.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        playAnimation()

        binding.haveAccountTextView.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        val haveAccountTextView = ObjectAnimator.ofFloat(binding.haveAccountTextView, View.ALPHA, 1f).setDuration(500)
        val tvSignin = ObjectAnimator.ofFloat(binding.tvSignin, View.ALPHA, 1f).setDuration(500)
        val ivSignin = ObjectAnimator.ofFloat(binding.ivSignin, View.ALPHA, 1f).setDuration(500)
        val tvLoginEmail = ObjectAnimator.ofFloat(binding.tvLoginEmail, View.ALPHA, 1f).setDuration(500)
        val edLoginEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val tvEdLoginPassword = ObjectAnimator.ofFloat(binding.tvEdLoginPassword, View.ALPHA, 1f).setDuration(500)
        val edLoginPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val signin = ObjectAnimator.ofFloat(binding.signinButton, View.ALPHA, 1f).setDuration(500)
        val copyrightTextView = ObjectAnimator.ofFloat(binding.copyrightTextView, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tvSignin, ivSignin, tvLoginEmail, edLoginEmail, tvEdLoginPassword, edLoginPassword, haveAccountTextView, signin, copyrightTextView)
            startDelay = 500
        }.start()
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

    private fun showLoading(isLoading: Boolean) {
//        binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE

        if (isLoading) {
            binding.progressBarLogin.visibility = View.VISIBLE
        } else {
            binding.progressBarLogin.visibility = View.GONE

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun setupAction() {
        binding.signinButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = "Masukkan password"
                }
                password.length <= 6 -> {
                    binding.edLoginPassword.error = "Password tidak boleh kurang dari 6"
                }

                else -> {
                    signinViewModel.signin(email, password)
                    signinViewModel.isLoading.observe(this) {
                        showLoading(it)
                    }
                }
            }
        }
    }

}