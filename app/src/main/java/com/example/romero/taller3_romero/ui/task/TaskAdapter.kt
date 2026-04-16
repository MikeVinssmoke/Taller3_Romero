package com.example.romero.taller3_romero.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClick: (Task) -> Unit,      // clic en la tarea → ver detalle
    private val onMenuClick: (Task, View) -> Unit  // clic en 3 puntos → menú
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var allTasks: List<Task> = tasks

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val tvTime: TextView = itemView.findViewById(R.id.tvTaskTime)
        val btnMenu: ImageButton = itemView.findViewById(R.id.btnMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvTitle.text = task.title
        holder.tvDescription.text = task.description
        holder.tvTime.text = if (task.hasReminder) "⏰ ${task.reminderTime}" else ""

        // Clic en cualquier parte de la tarjeta → ver detalle
        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }

        // Clic en los 3 puntos → menú
        holder.btnMenu.setOnClickListener { view ->
            onMenuClick(task, view)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        allTasks = newTasks
        tasks = newTasks
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        tasks = if (query.isEmpty()) {
            allTasks
        } else {
            allTasks.filter { task ->
                task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}