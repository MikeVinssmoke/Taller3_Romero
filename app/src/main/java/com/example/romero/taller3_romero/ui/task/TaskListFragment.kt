package com.example.romero.taller3_romero.ui.task



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.romero.taller3_romero.R
import com.example.romero.taller3_romero.data.task.TaskRepository

class TaskListFragment : Fragment() {

    private lateinit var repository: TaskRepository
    private lateinit var adapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = TaskRepository(requireContext())

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerTasks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TaskAdapter(repository.getAllTasks()) { task ->
            // Cuando pulsan editar, navegamos al fragment de edición pasando el ID
            val action = TaskListFragmentDirections
                .actionListToEdit(taskId = task.id)
            findNavController().navigate(action)
        }

        recyclerView.adapter = adapter

        // Botón agregar nueva tarea
        view.findViewById<View>(R.id.btnAddTask).setOnClickListener {
            findNavController().navigate(R.id.action_list_to_detail)
        }
    }

    // onResume se llama cada vez que volvemos a este Fragment
    // Así actualizamos la lista cuando regresamos de agregar/editar
    override fun onResume() {
        super.onResume()
        adapter.updateTasks(repository.getAllTasks())
    }
}