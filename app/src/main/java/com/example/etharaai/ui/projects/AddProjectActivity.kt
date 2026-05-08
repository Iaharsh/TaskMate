package com.example.etharaai.ui.projects

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivityAddProjectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProjectBinding
    private val viewModel: ProjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("ethara_prefs", android.content.Context.MODE_PRIVATE)
        val userRole = sharedPref.getString("user_role", "Member") ?: "Member"
        val userId = sharedPref.getString("user_id", "") ?: ""
        viewModel.setUserContext(userId, userRole)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.confirmCreateButton.setOnClickListener {
            val name = binding.projectNameEditText.text.toString()
            val desc = binding.projectDescEditText.text.toString()

            if (name.isNotEmpty()) {
                viewModel.createProject(name, desc)
                Toast.makeText(this, "Project Created Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter a project name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
