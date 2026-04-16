package com.example.romero.taller3_romero.ui.task



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.Task
import com.example.romero.taller3_romero.data.task.TaskRepository
import com.example.romero.taller3_romero.receiver.ReminderScheduler

class TaskEditFragment : Fragment() {

    // navArgs lee el argumento "taskId" que definimos en nav_graph.xml
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

        // Cargar los datos de la tarea actual
        val task = repository.getTaskById(args.taskId)
        if (task == null) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        // Pre-llenar los campos con los datos existentes
        etTitle.setText(task.title)
        etDescription.setText(task.description)
        etTime.setText(task.reminderTime)


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
                hasReminder = task.hasReminder  // conserva el valor que ya tenía
            )

            repository.updateTask(updatedTask)



            Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}