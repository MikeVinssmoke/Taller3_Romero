package com.example.romero.taller3_romero.data.task



import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "tasks_prefs"
        private const val KEY_TASK_LIST = "task_list"
        private const val KEY_NEXT_ID = "next_id"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    // Lista en memoria — es con la que trabajamos mientras la app está abierta
    private var tasksInMemory: MutableList<Task> = loadTasksFromPrefs()

    // Contador para generar IDs únicos
    private var nextId: Int = prefs.getInt(KEY_NEXT_ID, 1)

    // ── Operaciones públicas ──────────────────────────────────────────────────

    fun getAllTasks(): List<Task> = tasksInMemory.toList()

    fun addTask(title: String, description: String, reminderTime: String, hasReminder: Boolean) {
        val task = Task(
            id = nextId,
            title = title,
            description = description,
            reminderTime = reminderTime,
            hasReminder = hasReminder
        )
        tasksInMemory.add(task)
        nextId++
        saveAll()
    }

    fun updateTask(updated: Task) {
        val index = tasksInMemory.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            tasksInMemory[index] = updated
            saveAll()
        }
    }

    fun deleteTask(taskId: Int) {
        tasksInMemory.removeAll { it.id == taskId }
        saveAll()
    }

    fun getTaskById(id: Int): Task? = tasksInMemory.find { it.id == id }

    // ── Persistencia privada ──────────────────────────────────────────────────

    private fun saveAll() {
        prefs.edit().apply {
            putString(KEY_TASK_LIST, gson.toJson(tasksInMemory))
            putInt(KEY_NEXT_ID, nextId)
            apply()
        }
    }

    private fun loadTasksFromPrefs(): MutableList<Task> {
        val json = prefs.getString(KEY_TASK_LIST, null) ?: return mutableListOf()
        return try {
            val type = object : TypeToken<List<Task>>() {}.type
            val list: List<Task> = gson.fromJson(json, type)
            list.toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}