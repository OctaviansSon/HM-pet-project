package com.example.taskboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val query: String = "",
    val filter: TaskFilter = TaskFilter.ALL,
    val nextId: Int = 1,
    val viewModelCounter: Int = 0
) {
    /** Derived list: search + filter applied. */
    val visibleTasks: List<Task>
        get() = tasks
            .filter { it.title.contains(query.trim(), ignoreCase = true) }
            .filter {
                when (filter) {
                    TaskFilter.ALL -> true
                    TaskFilter.ACTIVE -> !it.done
                    TaskFilter.DONE -> it.done
                }
            }

    val doneCount: Int get() = tasks.count { it.done }
}

class TaskViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        TaskUiState(
            tasks = listOf(
                Task(1, "Set up CI pipeline", Priority.HIGH),
                Task(2, "Write Dockerfile", Priority.MEDIUM, done = true),
                Task(3, "Read Kubernetes docs", Priority.LOW)
            ),
            nextId = 4
        )
    )
    val state: StateFlow<TaskUiState> = _state.asStateFlow()

    fun addTask(title: String, priority: Priority) {
        val clean = title.trim()
        if (clean.isEmpty()) return
        _state.update { s ->
            s.copy(
                tasks = s.tasks + Task(s.nextId, clean, priority),
                nextId = s.nextId + 1
            )
        }
    }

    fun toggleDone(id: Int) = _state.update { s ->
        s.copy(tasks = s.tasks.map { if (it.id == id) it.copy(done = !it.done) else it })
    }

    fun deleteTask(id: Int) = _state.update { s ->
        s.copy(tasks = s.tasks.filterNot { it.id == id })
    }

    fun onQueryChange(query: String) = _state.update { it.copy(query = query) }

    fun onFilterChange(filter: TaskFilter) = _state.update { it.copy(filter = filter) }

    fun incrementCounter() = _state.update { it.copy(viewModelCounter = it.viewModelCounter + 1) }
}
