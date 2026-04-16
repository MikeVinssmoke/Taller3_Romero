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
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.TaskRepository
import com.example.romero.taller3_romero.receiver.ReminderScheduler

class TaskDetailFragment : Fragment() {

    private lateinit var repository: TaskRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = TaskRepository(requireContext())

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etTime = view.findViewById<EditText>(R.id.etReminderTime)

        val btnSave = view.findViewById<Button>(R.id.btnSaveTask)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val time = etTime.text.toString().trim()

            // Validación mínima
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            repository.addTask(title, description, time, false)

            Toast.makeText(requireContext(), "Tarea guardada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}