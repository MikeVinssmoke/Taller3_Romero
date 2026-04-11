package com.example.romero.taller3_romero.ui.task


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val onEditClick: (Task) -> Unit   // lambda que se llama al pulsar editar
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val tvTime: TextView = itemView.findViewById(R.id.tvTaskTime)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
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
        holder.btnEdit.setOnClickListener { onEditClick(task) }
    }

    override fun getItemCount(): Int = tasks.size

    // Llamar a esto para actualizar la lista cuando cambie
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}