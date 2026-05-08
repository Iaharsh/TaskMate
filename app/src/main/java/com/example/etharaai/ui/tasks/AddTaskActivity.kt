package com.example.etharaai.ui.tasks

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        viewModel.setProjectId(projectId)

        setupSpinners(projectId)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.taskTitleEditText.text.toString()
            val desc = binding.taskDescEditText.text.toString()
            val priority = binding.prioritySpinner.selectedItem.toString()
            val status = binding.statusSpinner.selectedItem.toString()
            
            val selectedMember = binding.assigneeSpinner.selectedItem as? com.example.etharaai.data.local.entities.UserEntity
            val assignedTo = selectedMember?.id
            val assigneeName = selectedMember?.name

            if (title.isNotEmpty() && desc.isNotEmpty()) {
                viewModel.createTask(title, desc, priority, status, assignedTo, assigneeName)
                Toast.makeText(this, "Task Created!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinners(projectId: String?) {
        // Priority Spinner
        val priorities = listOf("Low", "Medium", "High")
        val priorityAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        binding.prioritySpinner.adapter = priorityAdapter
        binding.prioritySpinner.setSelection(1) // Medium

        // Status Spinner
        val statuses = listOf("Todo", "Doing", "Done")
        val statusAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statuses)
        binding.statusSpinner.adapter = statusAdapter

        // Assignee Spinner
        if (projectId != null) {
            viewModel.getProjectMembers(projectId).observe(this) { members ->
                val memberAdapter = object : android.widget.ArrayAdapter<com.example.etharaai.data.local.entities.UserEntity>(
                    this, android.R.layout.simple_spinner_dropdown_item, members
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
            }
        }
    }
}
