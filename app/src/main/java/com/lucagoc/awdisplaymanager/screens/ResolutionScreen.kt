package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

@Composable
fun getIconForResolution(name: String): ImageVector {
    val upperName = name.uppercase()
    return when {
        "4K" in upperName -> FourK
        "1080" in upperName -> FullHd
        "720" in upperName -> Hd
        else -> Sd
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ResolutionScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    val resolutions by viewModel.supportedResolutions.collectAsState()
    val currentResolution by viewModel.currentResolution.collectAsState()
    val countdown by viewModel.countdown.collectAsState()

    if (countdown != null) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF1E1E1E), MaterialTheme.shapes.medium)
                    .padding(32.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.keep_this_resolution), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.reverting_in_seconds, countdown!!), color = Color.Red, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = { viewModel.confirmResolution() },
                            colors = ButtonDefaults.colors(containerColor = Color.White, contentColor = Color.Black),
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.confirm))
                        }
                        Button(onClick = { viewModel.rollbackResolution() }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.cancel))
                        }
                    }
                }
            }
        }
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
                    imageVector = SettingsScreen,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(R.string.select_resolution), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(resolutions) { mode ->
                    val isCurrent = currentResolution == mode.name
                    SettingsItem(
                        title = mode.displayName,
                        icon = getIconForResolution(mode.displayName),
                        onClick = { viewModel.applyResolutionTemporarily(mode) },
                        isCurrent = isCurrent,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingsItem(
                        title = stringResource(R.string.go_back),
                        icon = Icons.Default.Close,
                        onClick = onNavigateBack,
                    )
                }
            }
        }
        // Right pane
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = SettingsScreen,
                contentDescription = null,
                modifier = Modifier.size(160.dp),
                tint = Color(0xFF333333),
            )
        }
    }
}
