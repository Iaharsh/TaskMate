package com.example.etharaai.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivityLoginBinding
import com.example.etharaai.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("ethara_prefs", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPref.getBoolean("is_first_launch", true)

        if (isFirstLaunch) {
            binding.welcomeTitle.text = "Welcome"
            binding.welcomeSubtitle.text = "Create an account to get started"
        } else {
            binding.welcomeTitle.text = "Welcome Back"
            binding.welcomeSubtitle.text = "Sign in to continue"
        }

        viewModel.authState.observe(this) { result ->
            when (result) {
                is AuthResult.Success -> {
                    sharedPref.edit()
                        .putBoolean("is_first_launch", false)
                        .putString("user_name", result.user.name)
                        .putString("user_role", result.user.role)
                        .putString("user_id", result.user.id)
                        .apply()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                is AuthResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val role = if (binding.radioAdmin.isChecked) "Admin" else "Member"
                viewModel.login(email, password, role)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupTextView.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
