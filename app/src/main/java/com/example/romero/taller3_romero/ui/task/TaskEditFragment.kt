package com.example.romero.taller3_romero.ui.task

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.Task
import com.example.romero.taller3_romero.data.task.TaskRepository
import com.example.romero.taller3_romero.receiver.ReminderScheduler
import java.util.Calendar

class TaskEditFragment : Fragment() {

    private val args: TaskEditFragmentArgs by navArgs()
    private lateinit var repository: TaskRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = TaskRepository(requireContext())

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etTime = view.findViewById<EditText>(R.id.etReminderTime)
        val btnSave = view.findViewById<Button>(R.id.btnSaveTask)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        val task = repository.getTaskById(args.taskId)
        if (task == null) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        etTitle.setText(task.title)
        etDescription.setText(task.description)
        etTime.setText(task.reminderTime)

        // Al tocar el campo de hora abre el reloj nativo
        etTime.setOnClickListener {
            abrirTimePicker(etTime)
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val time = etTime.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTask = Task(
                id = task.id,
                title = title,
                description = description,
                reminderTime = time,
                hasReminder = task.hasReminder
            )

            repository.updateTask(updatedTask)

            // Si tiene hora configurada y la alarma está activa, reprogramamos
            if (updatedTask.hasReminder && time.isNotEmpty()) {
                ReminderScheduler.schedule(requireContext(), title, time)
                Toast.makeText(requireContext(), "Recordatorio reprogramado para las $time", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun abrirTimePicker(etTime: EditText) {
        val calendar = Calendar.getInstance()
        val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
        val minutoActual = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val timeFormatted = String.format("%02d:%02d", hour, minute)
                etTime.setText(timeFormatted)
            },
            horaActual,
            minutoActual,
            true
        )
        timePicker.show()
    }
}