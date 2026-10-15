package com.example.taskboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskboard.ui.TaskScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Material 3 theme from the project template (ui/theme/Theme.kt); replace the name if yours differs.
            com.example.taskboard.ui.theme.TaskBoardTheme {
                val vm: TaskViewModel = viewModel()
                TaskScreen(vm)
            }
        }
    }
}
