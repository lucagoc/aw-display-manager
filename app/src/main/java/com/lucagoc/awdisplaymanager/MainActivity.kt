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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.lucagoc.awdisplaymanager.screens.AboutScreen
import com.lucagoc.awdisplaymanager.screens.DensityScreen
import com.lucagoc.awdisplaymanager.screens.HomeScreen
import com.lucagoc.awdisplaymanager.screens.OverscanScreen
import com.lucagoc.awdisplaymanager.screens.ResolutionScreen
import com.lucagoc.awdisplaymanager.screens.RootDeniedScreen
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
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    val hasRootAccess by viewModel.hasRootAccess.collectAsState()

    if (hasRootAccess == false) {
        RootDeniedScreen()
        return
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToResolution = { navController.navigate("resolution") },
                onNavigateToOverscan = { navController.navigate("overscan") },
                onNavigateToDensity = { navController.navigate("density") },
            ) { navController.navigate("about") }
        }
        composable("density") {
            DensityScreen(viewModel = viewModel) {
                navController.popBackStack()
            }
        }
        composable("resolution") {
            ResolutionScreen(viewModel = viewModel) {
                navController.popBackStack()
            }
        }
        composable("overscan") {
            OverscanScreen(viewModel = viewModel) {
                navController.popBackStack()
            }
        }
        composable("about") {
            AboutScreen {
                navController.popBackStack()
            }
        }
    }
}
