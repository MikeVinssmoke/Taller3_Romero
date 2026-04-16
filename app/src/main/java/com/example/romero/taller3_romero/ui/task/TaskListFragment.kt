package com.example.romero.taller3_romero.ui.task

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.Task
import com.example.romero.taller3_romero.data.task.TaskRepository
import com.example.romero.taller3_romero.receiver.ReminderScheduler

class TaskListFragment : Fragment() {

    private lateinit var repository: TaskRepository
    private lateinit var adapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = TaskRepository(requireContext())

        recyclerView = view.findViewById(R.id.recyclerTasks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        etSearch = view.findViewById(R.id.etSearch)

        // 🔥 NUEVO ADAPTER CON DOS LAMBDAS
        adapter = TaskAdapter(
            tasks = repository.getAllTasks(),

            onTaskClick = { task ->
                val action = TaskListFragmentDirections
                    .actionListToView(taskId = task.id)
                findNavController().navigate(action)
            },

            onMenuClick = { task, anchorView ->
                mostrarMenu(task, anchorView)
            }
        )

        recyclerView.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }
        })

        view.findViewById<View>(R.id.btnAddTask).setOnClickListener {
            findNavController().navigate(R.id.action_list_to_detail)
        }
    }

    private fun mostrarMenu(task: Task, anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)

        popup.menu.add(0, 1, 0, "Editar")
        popup.menu.add(
            0,
            2,
            1,
            if (task.hasReminder) "Desactivar alarma" else "Activar alarma"
        )
        popup.menu.add(0, 3, 2, "Borrar")

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                1 -> {
                    val action = TaskListFragmentDirections
                        .actionListToEdit(taskId = task.id)
                    findNavController().navigate(action)
                    true
                }

                2 -> {
                    val updatedTask = task.copy(hasReminder = !task.hasReminder)
                    repository.updateTask(updatedTask)

                    if (updatedTask.hasReminder && updatedTask.reminderTime.isNotEmpty()) {
                        ReminderScheduler.schedule(
                            requireContext(),
                            updatedTask.title,
                            updatedTask.reminderTime
                        )
                        Toast.makeText(
                            requireContext(),
                            "Alarma activada para ${updatedTask.reminderTime}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Alarma desactivada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    adapter.updateTasks(repository.getAllTasks())
                    true
                }

                3 -> {
                    repository.deleteTask(task.id)
                    adapter.updateTasks(repository.getAllTasks())
                    Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        popup.show()
    }

    override fun onResume() {
        super.onResume()
        adapter.updateTasks(repository.getAllTasks())
        etSearch.setText("")
    }
}