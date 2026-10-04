package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AdvancedScreen(
    viewModel: MainViewModel,
    onNavigateToRenderResolution: () -> Unit,
    onNavigateToDensity: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val currentRenderResolution by viewModel.currentRenderResolution.collectAsState()
    val isAutoRender by viewModel.isAutoRenderResolution.collectAsState()
    val currentDensity by viewModel.currentDensity.collectAsState()
    val isAutoDensity by viewModel.isAutoDensity.collectAsState()

    val renderSubtitle = if (isAutoRender) {
        "${stringResource(R.string.auto)} ($currentRenderResolution)"
    } else {
        currentRenderResolution
    }

    val densitySubtitle = if (isAutoDensity) {
        "${stringResource(R.string.auto)} (${stringResource(R.string.dpi_format, currentDensity)})"
    } else {
        stringResource(R.string.dpi_format, currentDensity)
    }

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F0F))) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 56.dp, top = 48.dp, end = 32.dp, bottom = 48.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = DisplaySettings,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.advanced_options),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            SettingsItem(
                title = stringResource(R.string.render_resolution),
                subtitle = renderSubtitle,
                icon = SettingsScreen,
                onClick = onNavigateToRenderResolution,
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(
                title = stringResource(R.string.density),
                subtitle = densitySubtitle,
                icon = HighDensity,
                onClick = onNavigateToDensity,
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsItem(
                title = stringResource(R.string.go_back),
                icon = Icons.Default.Close,
                onClick = onNavigateBack,
            )
        }
        // Right pane: Decorative
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = DisplaySettings,
                contentDescription = null,
                modifier = Modifier.size(160.dp),
                tint = Color(0xFF333333),
            )
        }
    }
}
