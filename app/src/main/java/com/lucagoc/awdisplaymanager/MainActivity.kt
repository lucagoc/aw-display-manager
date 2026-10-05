package com.lucagoc.awdisplaymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.lucagoc.awdisplaymanager.screens.*
import com.lucagoc.awdisplaymanager.ui.theme.AllwinnerScreenSettingsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AllwinnerScreenSettingsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape,
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val viewModel: MainViewModel = viewModel()
    val hasRootAccess by viewModel.hasRootAccess.collectAsState()

    if (hasRootAccess == false) {
        RootDeniedScreen()
        return
    }

    TwoColumnSettingsLayout(viewModel = viewModel)
}
