package com.example.etharaai.ui.tasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivityTaskListBinding
import dagger.hilt.android.AndroidEntryPoint

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

@AndroidEntryPoint
class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val sharedPref = getSharedPreferences("ethara_prefs", android.content.Context.MODE_PRIVATE)
        val userRole = sharedPref.getString("user_role", "Member")
        val isAdmin = userRole == "Admin"
        
        binding.userRoleTag.text = if (isAdmin) "👑 Admin" else "👤 Member"

        val projectId = intent.getStringExtra("PROJECT_ID")
        viewModel.setProjectId(projectId)

        adapter = TaskAdapter(
            tasks = emptyList(),
            onStatusUpdate = { task, newStatus ->
                viewModel.updateTaskStatus(task.id, newStatus)
            },
            onDelete = { task ->
                viewModel.deleteTask(task.id)
            },
            onReassign = { task ->
                showReassignDialog(task)
            },
            isAdmin = isAdmin
        )
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = adapter

        viewModel.tasks.observe(this) { tasks ->
            adapter.updateTasks(tasks)
            binding.emptyTasksState.visibility = if (tasks.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            
            // Update chips (Mock counts for now)
            binding.chipAll.text = "All  ${tasks.size}"
            binding.chipTodo.text = "Todo  ${tasks.count { it.status == "Todo" }}"
            binding.chipInProgress.text = "In Progress  ${tasks.count { it.status == "Doing" }}"
            binding.chipDone.text = "Done  ${tasks.count { it.status == "Done" }}"
        }

        if (!isAdmin) {
            binding.addTaskButton.visibility = android.view.View.GONE
        }

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("PROJECT_ID", projectId)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showReassignDialog(task: com.example.etharaai.data.local.entities.TaskEntity) {
        val projectId = task.projectId
        viewModel.getProjectMembers(projectId).observe(this) { members ->
            val memberNames = members.map { it.name }.toTypedArray()
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Reassign Task")
                .setItems(memberNames) { _, which ->
                    val selectedMember = members[which]
                    viewModel.reassignTask(task.id, selectedMember.id, selectedMember.name)
                    android.widget.Toast.makeText(this, "Task Reassigned to ${selectedMember.name}", android.widget.Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
