package com.example.etharaai.ui.tasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivityTaskListBinding
import dagger.hilt.android.AndroidEntryPoint

import android.content.Intent
import androidx.activity.viewModels
import com.example.etharaai.data.local.entities.UserEntity
import com.example.etharaai.data.local.entities.TaskEntity
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
        val userRole = sharedPref.getString("user_role", "Member") ?: "Member"
        val userId = sharedPref.getString("user_id", "") ?: ""
        val isAdmin = userRole == "Admin"
        
        binding.userRoleTag.text = if (isAdmin) "👑 Admin" else "👤 Member"

        val projectId = intent.getStringExtra("PROJECT_ID")
        viewModel.setProjectId(projectId)
        viewModel.setUserContext(userId, userRole)

        adapter = TaskAdapter(
            tasks = emptyList(),
            onStatusUpdate = { task, newStatus ->
                viewModel.updateTaskStatus(task.id, newStatus)
            },
            onDelete = { task ->
                viewModel.deleteTask(task.id)
            },
            onEdit = { task ->
                val intent = Intent(this, AddTaskActivity::class.java)
                intent.putExtra("PROJECT_ID", projectId)
                intent.putExtra("TASK_ID", task.id)
                intent.putExtra("TASK_TITLE", task.title)
                intent.putExtra("TASK_DESC", task.description)
                intent.putExtra("TASK_PRIORITY", task.priority)
                intent.putExtra("TASK_STATUS", task.status)
                intent.putExtra("TASK_ASSIGNED_TO", task.assignedTo)
                intent.putExtra("TASK_PROJECT_ID", task.projectId)
                intent.putExtra("TASK_DUE_DATE", task.dueDate)
                intent.putExtra("TASK_OWNER_ID", task.ownerId)
                startActivity(intent)
            },
            isAdmin = isAdmin
        )
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = adapter

        var allTasks = emptyList<TaskEntity>()
        
        viewModel.tasks.observe(this) { tasks ->
            allTasks = tasks
            filterTasks(tasks, binding.filterChipGroup.checkedChipId)
            
            // Update chips (Mock counts for now)
            binding.chipAll.text = "All  ${tasks.size}"
            binding.chipTodo.text = "Todo  ${tasks.count { it.status == "Todo" }}"
            binding.chipInProgress.text = "In Progress  ${tasks.count { it.status == "Doing" }}"
            binding.chipDone.text = "Done  ${tasks.count { it.status == "Done" }}"
        }

        binding.filterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            filterTasks(allTasks, checkedId)
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

    private fun filterTasks(tasks: List<TaskEntity>, checkedId: Int) {
        val filtered = when (checkedId) {
            binding.chipTodo.id -> tasks.filter { it.status == "Todo" }
            binding.chipInProgress.id -> tasks.filter { it.status == "Doing" }
            binding.chipDone.id -> tasks.filter { it.status == "Done" }
            else -> tasks
        }
        adapter.updateTasks(filtered)
        binding.emptyTasksState.visibility = if (filtered.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
    }
}
