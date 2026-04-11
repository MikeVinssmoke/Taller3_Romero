package com.example.romero.taller3_romero.data.task

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val reminderTime: String,   // formato "HH:mm", ej: "14:30"
    val hasReminder: Boolean
)