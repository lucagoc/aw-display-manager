package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToResolution: () -> Unit,
    onNavigateToOverscan: () -> Unit,
    onNavigateToDensity: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    val currentResolution by viewModel.formattedCurrentResolution.collectAsState()
    val currentDensity by viewModel.currentDensity.collectAsState()

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F0F))) {
        // Left pane: Options
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
                    text = stringResource(R.string.display_manager),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(48.dp))

            SettingsItem(
                title = stringResource(R.string.resolution),
                subtitle = currentResolution,
                icon = SettingsScreen,
                onClick = onNavigateToResolution,
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(
                title = stringResource(R.string.density),
                subtitle = stringResource(R.string.dpi_format, currentDensity),
                icon = HighDensity,
                onClick = onNavigateToDensity,
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(
                title = stringResource(R.string.overscan),
                subtitle = stringResource(R.string.adjust_screen_margins),
                icon = Resize,
                onClick = onNavigateToOverscan,
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(
                title = stringResource(R.string.about),
                icon = Icons.Default.Info,
                onClick = onNavigateToAbout,
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
