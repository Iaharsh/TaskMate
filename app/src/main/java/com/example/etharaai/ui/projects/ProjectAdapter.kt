package com.example.etharaai.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.etharaai.data.local.entities.ProjectWithMembers
import com.example.etharaai.databinding.ItemProjectBinding

class ProjectAdapter(
    private var projects: List<ProjectWithMembers>,
    private val onViewTasks: (ProjectWithMembers) -> Unit,
    private val onDelete: (ProjectWithMembers) -> Unit,
    private val onAddMember: (ProjectWithMembers, String) -> Unit,
    private val onRemoveMember: (ProjectWithMembers, String) -> Unit,
    private val isAdmin: Boolean = true
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val projectWithMembers = projects[position]
        val project = projectWithMembers.project
        val members = projectWithMembers.members

        holder.binding.projectNameText.text = project.name
        holder.binding.projectOwnerText.text = "Owner: ${project.ownerId}"
        
        // Populate Members
        holder.binding.memberChipGroup.removeAllViews()
        members.forEach { member ->
            val chip = com.google.android.material.chip.Chip(holder.itemView.context).apply {
                text = member.name
                isCloseIconVisible = false // admin removing off
                setOnCloseIconClickListener {
                    onRemoveMember(projectWithMembers, member.id)
                }
            }
            holder.binding.memberChipGroup.addView(chip)
        }

        // Role based visibility
        if (!isAdmin) {
            holder.binding.deleteButton.visibility = View.GONE
            holder.binding.addMemberButton.visibility = View.GONE
        } else {
            holder.binding.deleteButton.visibility = View.VISIBLE
            holder.binding.addMemberButton.visibility = View.VISIBLE
        }

        holder.binding.viewTasksButton.setOnClickListener {
            onViewTasks(projectWithMembers)
        }

        holder.binding.deleteButton.setOnClickListener {
            onDelete(projectWithMembers)
        }

        holder.binding.addMemberButton.setOnClickListener {
            holder.binding.addMemberSection.visibility = View.VISIBLE
        }

        holder.binding.closeAddMemberButton.setOnClickListener {
            holder.binding.addMemberSection.visibility = View.GONE
        }

        holder.binding.confirmAddMemberButton.setOnClickListener {
            val email = holder.binding.memberEmailInput.text.toString()
            if (email.isNotEmpty()) {
                onAddMember(projectWithMembers, email)
                holder.binding.memberEmailInput.setText("")
                holder.binding.addMemberSection.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = projects.size

    fun updateProjects(newProjects: List<ProjectWithMembers>) {
        projects = newProjects
        notifyDataSetChanged()
    }
}
