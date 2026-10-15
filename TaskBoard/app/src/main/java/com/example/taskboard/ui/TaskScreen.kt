package com.example.taskboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskboard.TaskViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val state by viewModel.state.collectAsState()
    val visible = state.visibleTasks

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("DevOps Task Board (${state.doneCount}/${state.tasks.size})") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item { AddTaskBar(onAdd = viewModel::addTask) }
            item { SearchField(state.query, viewModel::onQueryChange) }
            item { FilterRow(state.filter, viewModel::onFilterChange) }
            item { StateDemoCard(state.viewModelCounter, viewModel::incrementCounter) }

            if (visible.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No tasks found", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else {
                items(visible, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { viewModel.toggleDone(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
    }
}
