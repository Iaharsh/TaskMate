package com.example.etharaai.ui.projects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivityProjectListBinding
import dagger.hilt.android.AndroidEntryPoint

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.etharaai.ui.tasks.TaskListActivity

@AndroidEntryPoint
class ProjectListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectListBinding
    private val viewModel: ProjectViewModel by viewModels()
    private lateinit var adapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val sharedPref = getSharedPreferences("ethara_prefs", android.content.Context.MODE_PRIVATE)
        val userRole = sharedPref.getString("user_role", "Member") ?: "Member"
        val userId = sharedPref.getString("user_id", "") ?: ""
        val isAdmin = userRole == "Admin"
        
        viewModel.setUserContext(userId, userRole)
        binding.userRoleTag.text = if (isAdmin) "👑 Admin" else "👤 Member"

        adapter = ProjectAdapter(
            projects = emptyList(),
            onViewTasks = { projectWithMembers ->
                val intent = Intent(this, TaskListActivity::class.java)
                intent.putExtra("PROJECT_ID", projectWithMembers.project.id)
                startActivity(intent)
            },
            onDelete = { projectWithMembers ->
                viewModel.deleteProject(projectWithMembers.project.id)
                android.widget.Toast.makeText(this, "Project Deleted", android.widget.Toast.LENGTH_SHORT).show()
            },
            onAddMember = { projectWithMembers, email ->
                viewModel.addMemberByEmail(projectWithMembers.project.id, email) { success, message ->
                    android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            onRemoveMember = { projectWithMembers, userId ->
                viewModel.removeMember(projectWithMembers.project.id, userId)
            },
            isAdmin = isAdmin
        )
        binding.projectRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.projectRecyclerView.adapter = adapter

        viewModel.projects.observe(this) { projects ->
            adapter.updateProjects(projects)
            binding.emptyState.visibility = if (projects.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        if (!isAdmin) {
            binding.addProjectFab.visibility = android.view.View.GONE
        }

        binding.addProjectFab.setOnClickListener {
            startActivity(Intent(this, AddProjectActivity::class.java))
        }
    }
}
