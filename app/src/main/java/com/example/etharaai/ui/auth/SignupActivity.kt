package com.example.etharaai.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivitySignupBinding
import com.example.etharaai.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

import androidx.activity.viewModels

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.authState.observe(this) { result ->
            when (result) {
                is AuthResult.Success -> {
                    Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show()
                    finish() // Returns to LoginActivity
                }
                is AuthResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val role = if (binding.radioAdmin.isChecked) "Admin" else "Member"
                viewModel.signup(name, email, password, role)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
