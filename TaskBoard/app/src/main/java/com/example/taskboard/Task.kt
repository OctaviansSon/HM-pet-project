package com.example.taskboard

enum class Priority(val label: String) { LOW("Low"), MEDIUM("Medium"), HIGH("High") }

enum class TaskFilter(val label: String) { ALL("All"), ACTIVE("Active"), DONE("Done") }

data class Task(
    val id: Int,
    val title: String,
    val priority: Priority,
    val done: Boolean = false
)
