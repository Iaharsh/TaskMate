package com.example.etharaai.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.etharaai.data.local.entities.TaskEntity
import com.example.etharaai.databinding.ItemTaskBinding

class TaskAdapter(
    private var tasks: List<TaskEntity>,
    private val onStatusUpdate: (TaskEntity, String) -> Unit,
    private val onDelete: (TaskEntity) -> Unit,
    private val onEdit: (TaskEntity) -> Unit,
    private val isAdmin: Boolean = true
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.taskTitleText.text = task.title
        holder.binding.taskDescText.text = task.description
        holder.binding.taskStatusText.text = task.status
        holder.binding.taskPriorityText.text = task.priority
        holder.binding.taskAssigneeText.text = if (task.assigneeName != null) "👤 ${task.assigneeName}" else "👤 Unassigned"

        // Manage tasks: Everyone can mark as done and delete
        holder.binding.markDoneButton.visibility = if (task.status == "Done") View.GONE else View.VISIBLE
        holder.binding.deleteTaskButton.visibility = View.VISIBLE
        
        // Edit button for admin
        holder.binding.editTaskButton.visibility = if (isAdmin) View.VISIBLE else View.GONE
        holder.binding.editTaskButton.setOnClickListener {
            onEdit(task)
        }

        holder.binding.markDoneButton.setOnClickListener {
            onStatusUpdate(task, "Done")
        }

        holder.binding.deleteTaskButton.setOnClickListener {
            onDelete(task)
        }
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
