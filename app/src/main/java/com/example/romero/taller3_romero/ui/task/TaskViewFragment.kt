package com.example.romero.taller3_romero.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.TaskRepository

class TaskViewFragment : Fragment() {

    private val args: TaskViewFragmentArgs by navArgs()
    private lateinit var repository: TaskRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = TaskRepository(requireContext())

        val task = repository.getTaskById(args.taskId)
        if (task == null) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        view.findViewById<TextView>(R.id.tvDetailTitle).text = task.title

        // Si la descripción está vacía mostramos un mensaje
        view.findViewById<TextView>(R.id.tvDetailDescription).text =
            if (task.description.isEmpty()) "Sin descripción" else task.description

        // Mostramos el estado del recordatorio
        view.findViewById<TextView>(R.id.tvDetailTime).text = when {
            task.hasReminder && task.reminderTime.isNotEmpty() -> "⏰ ${task.reminderTime} — Activo"
            task.reminderTime.isNotEmpty() -> "🕐 ${task.reminderTime} — Sin activar"
            else -> "Sin recordatorio"
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}