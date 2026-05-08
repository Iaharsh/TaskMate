package com.example.etharaai.ui.tasks

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.data.local.entities.UserEntity
import com.example.etharaai.databinding.ActivityAddTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projectId = intent.getStringExtra("PROJECT_ID")
        val taskId = intent.getStringExtra("TASK_ID")
        val isEditMode = taskId != null
        
        viewModel.setProjectId(projectId)

        if (isEditMode) {
            binding.taskTitleEditText.setText(intent.getStringExtra("TASK_TITLE"))
            binding.taskDescEditText.setText(intent.getStringExtra("TASK_DESC"))
            binding.saveButton.text = "Update Task"
            // Spinners will be set in setupSpinners
        }

        setupSpinners(projectId, intent.getStringExtra("TASK_PRIORITY"), intent.getStringExtra("TASK_STATUS"), intent.getStringExtra("TASK_ASSIGNED_TO"))

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.taskTitleEditText.text.toString()
            val desc = binding.taskDescEditText.text.toString()
            val priority = binding.prioritySpinner.selectedItem.toString()
            val status = binding.statusSpinner.selectedItem.toString()
            
            val selectedMember = binding.assigneeSpinner.selectedItem as? UserEntity
            val assignedTo = if (selectedMember?.id == "unassigned") null else selectedMember?.id
            val assigneeName = if (selectedMember?.id == "unassigned") null else selectedMember?.name

            if (title.isNotEmpty() && desc.isNotEmpty()) {
                if (isEditMode) {
                    val updatedTask = com.example.etharaai.data.local.entities.TaskEntity(
                        id = taskId!!,
                        projectId = projectId ?: intent.getStringExtra("TASK_PROJECT_ID") ?: "general",
                        title = title,
                        description = desc,
                        assignedTo = assignedTo,
                        assigneeName = assigneeName,
                        status = status,
                        priority = priority,
                        dueDate = intent.getLongExtra("TASK_DUE_DATE", System.currentTimeMillis()),
                        ownerId = intent.getStringExtra("TASK_OWNER_ID") ?: "admin"
                    )
                    viewModel.updateTask(updatedTask)
                    Toast.makeText(this, "Task Updated!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.createTask(title, desc, priority, status, assignedTo, assigneeName)
                    Toast.makeText(this, "Task Created!", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinners(projectId: String?, initialPriority: String? = null, initialStatus: String? = null, initialAssignedTo: String? = null) {
        // Priority Spinner
        val priorities = listOf("Low", "Medium", "High")
        val priorityAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        binding.prioritySpinner.adapter = priorityAdapter
        val priorityPos = if (initialPriority != null) priorities.indexOf(initialPriority) else 1
        binding.prioritySpinner.setSelection(if (priorityPos >= 0) priorityPos else 1)

        // Status Spinner
        val statuses = listOf("Todo", "Doing", "Done")
        val statusAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statuses)
        binding.statusSpinner.adapter = statusAdapter
        val statusPos = if (initialStatus != null) statuses.indexOf(initialStatus) else 0
        binding.statusSpinner.setSelection(if (statusPos >= 0) statusPos else 0)

        // Assignee Spinner
        val unassignedUser = UserEntity("unassigned", "Unassigned", "", "", "")
        
        val usersLiveData = if (projectId != null) {
            viewModel.getProjectMembers(projectId)
        } else {
            viewModel.getAllUsers()
        }

        usersLiveData.observe(this) { members ->
            val allMembers = listOf(unassignedUser) + members
            val memberAdapter = object : android.widget.ArrayAdapter<UserEntity>(
                this, android.R.layout.simple_spinner_dropdown_item, allMembers
            ) {
                override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                    val view = super.getView(position, convertView, parent)
                    (view as android.widget.TextView).text = getItem(position)?.name ?: "Unassigned"
                    return view
                }
                override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                    val view = super.getDropDownView(position, convertView, parent)
                    (view as android.widget.TextView).text = getItem(position)?.name ?: "Unassigned"
                    return view
                }
            }
            binding.assigneeSpinner.adapter = memberAdapter
            
            // Set initial selection
            if (initialAssignedTo != null) {
                val index = allMembers.indexOfFirst { it.id == initialAssignedTo }
                if (index >= 0) binding.assigneeSpinner.setSelection(index)
            } else {
                binding.assigneeSpinner.setSelection(0) // Unassigned
            }
        }
    }
}
