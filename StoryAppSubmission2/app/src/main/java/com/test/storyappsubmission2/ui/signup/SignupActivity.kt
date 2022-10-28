package com.test.storyappsubmission2.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.test.storyappsubmission2.R
import com.test.storyappsubmission2.databinding.ActivitySignupBinding
import com.test.storyappsubmission2.ui.ViewModelFactory
import com.test.storyappsubmission2.ui.signin.SigninActivity


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val signupViewModel: SignupViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        val tvSignup = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(500)
        val ivSignup = ObjectAnimator.ofFloat(binding.ivSignup, View.ALPHA, 1f).setDuration(500)
        val tvRegisterName = ObjectAnimator.ofFloat(binding.tvRegisterName, View.ALPHA, 1f).setDuration(500)
        val edRegisterName = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val tvRegisterEmail = ObjectAnimator.ofFloat(binding.tvRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val edRegisterEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val tvRegisterPassword = ObjectAnimator.ofFloat(binding.tvRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val edRegisterPassword = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val copyrightTextView = ObjectAnimator.ofFloat(binding.copyrightTextView, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tvSignup, ivSignup, tvRegisterName, edRegisterName, tvRegisterEmail, edRegisterEmail, tvRegisterPassword, edRegisterPassword, signup, copyrightTextView)
            startDelay = 500
        }.start()
    }




    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBarRegister.visibility = View.VISIBLE
        } else {
            binding.progressBarRegister.visibility = View.GONE
        }
    }


    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.edRegisterName.error = getString(R.string.input_name)
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error = getString(R.string.input_email)
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.error = getString(R.string.input_password)
                }
                password.length < 6 -> {
                    binding.edRegisterPassword.error = getString(R.string.label_validation_password)
                }
                else -> {
                    signupViewModel.signup(name, email, password).observe(this) { result ->
                        if (result.message == "201") {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(R.string.info)
                            builder.setMessage(R.string.validate_register_success)
                            builder.setIcon(R.drawable.ic_baseline_check_green_24)
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                alertDialog.dismiss()
                                val intent = Intent(this, SigninActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 2000)
                        }
                        if (result.message == "400") {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(R.string.info)
                            builder.setMessage(R.string.validate_register_failed)
                            builder.setIcon(R.drawable.ic_baseline_close_red_24)
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                alertDialog.dismiss()
                            }, 2000)
                        }

                        if (result.message == "") {
                            showLoading(true)
                        } else {
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }
}