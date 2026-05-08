package com.example.etharaai.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivityDashboardBinding
import com.example.etharaai.ui.auth.LoginActivity
import com.example.etharaai.ui.projects.ProjectListActivity
import com.example.etharaai.ui.projects.AddProjectActivity
import com.example.etharaai.ui.tasks.TaskListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("ethara_prefs", android.content.Context.MODE_PRIVATE)
        val userRole = sharedPref.getString("user_role", "Member") ?: "Member"
        val userId = sharedPref.getString("user_id", "") ?: ""
        
        viewModel.setUserContext(userId, userRole)
        updateGreeting()

        viewModel.totalTasks.observe(this) { total ->
            binding.tasksCountText.text = (total ?: 0).toString()
        }

        viewModel.completedCount.observe(this) { completed ->
            binding.completedCountText.text = (completed ?: 0).toString()
        }

        viewModel.overdueCount.observe(this) { overdue ->
            binding.overdueCountText.text = (overdue ?: 0).toString()
        }

        viewModel.projectCount.observe(this) { count ->
            binding.projectsCountText.text = (count ?: 0).toString()
        }

        binding.viewProjectsButton.setOnClickListener {
            startActivity(Intent(this, ProjectListActivity::class.java))
        }

        binding.createProjectButton.setOnClickListener {
            startActivity(Intent(this, AddProjectActivity::class.java))
        }

        // Card Clicks
        binding.projectsCard.setOnClickListener {
            startActivity(Intent(this, ProjectListActivity::class.java))
        }

        binding.totalTasksCard.setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
        }

        binding.completedTasksCard.setOnClickListener {
            // Optional: Pass filter for "Done" status
            startActivity(Intent(this, TaskListActivity::class.java))
        }

        binding.overdueTasksCard.setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
        }
        

        if (userRole == "Member") {
            binding.createProjectButton.visibility = android.view.View.GONE
        }

        binding.logoutButton.setOnClickListener {
            val sharedPref = getSharedPreferences("ethara_prefs", android.content.Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun updateGreeting() {
        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        
        val greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Good night"
        }
        
        val sharedPref = getSharedPreferences("ethara_prefs", android.content.Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "User")
        val userRole = sharedPref.getString("user_role", "Member")
        
        binding.greetingText.text = "$greeting, $userName"
        binding.userRoleTag.text = if (userRole == "Admin") "👑 Admin" else "👤 Member"
    }
}
