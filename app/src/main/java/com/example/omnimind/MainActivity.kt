package com.example.omnimind

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnimind.presentation.navigation.OmniMindNavigation
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel
import com.example.omnimind.ui.theme.OmniMindTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: OmniMindViewModel = viewModel()
            val selectedTheme by viewModel.selectedTheme.collectAsState()

            OmniMindTheme(appTheme = selectedTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OmniMindNavigation()
                }
            }
        }
    }
}
