package com.example.taskboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.taskboard.Priority
import com.example.taskboard.Task
import com.example.taskboard.TaskFilter

/** Reusable #1: colored priority badge (Box). */
@Composable
fun PriorityBadge(priority: Priority, modifier: Modifier = Modifier) {
    val color = when (priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.errorContainer
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiaryContainer
        Priority.LOW -> MaterialTheme.colorScheme.secondaryContainer
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(priority.label, style = MaterialTheme.typography.labelSmall)
    }
}

/** Reusable #2: one task card (Card + Row + Checkbox + Text + IconButton). */
@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.done, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.done) TextDecoration.LineThrough else null
                )
                Spacer(Modifier.height(4.dp))
                PriorityBadge(task.priority)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete task")
            }
        }
    }
}

/**
 * Reusable #3: input bar. The draft text and the selected priority are UI-local state
 * -> rememberSaveable (survives rotation, not needed in the ViewModel).
 */
@Composable
fun AddTaskBar(onAdd: (String, Priority) -> Unit, modifier: Modifier = Modifier) {
    var draft by rememberSaveable { mutableStateOf("") }
    var priority by rememberSaveable { mutableStateOf(Priority.MEDIUM) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                label = { Text("New task") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { onAdd(draft, priority); draft = "" },
                enabled = draft.isNotBlank()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Add")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Priority.values().forEach { p ->
                FilterChip(selected = priority == p, onClick = { priority = p }, label = { Text(p.label) })
            }
        }
    }
}

/** Reusable #4: search field. */
@Composable
fun SearchField(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

/** Reusable #5: All / Active / Done filter. */
@Composable
fun FilterRow(selected: TaskFilter, onSelect: (TaskFilter) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TaskFilter.values().forEach { f ->
            FilterChip(selected = selected == f, onClick = { onSelect(f) }, label = { Text(f.label) })
        }
    }
}

/**
 * Reusable #6: demo of remember vs rememberSaveable vs ViewModel.
 * Rotate the device and see which counter survives.
 */
@Composable
fun StateDemoCard(viewModelCounter: Int, onViewModelIncrement: () -> Unit, modifier: Modifier = Modifier) {
    var rememberCounter by remember { mutableIntStateOf(0) }
    var saveableCounter by rememberSaveable { mutableIntStateOf(0) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("State demo: rotate the screen", style = MaterialTheme.typography.titleSmall)
            CounterRow("remember (resets)", rememberCounter) { rememberCounter++ }
            CounterRow("rememberSaveable (survives)", saveableCounter) { saveableCounter++ }
            CounterRow("ViewModel (survives)", viewModelCounter, onViewModelIncrement)
        }
    }
}

@Composable
private fun CounterRow(label: String, value: Int, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("$label: $value", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        OutlinedButton(onClick = onClick) { Text("+1") }
    }
}
