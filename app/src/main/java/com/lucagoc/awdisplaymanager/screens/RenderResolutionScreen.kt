package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.DisplayManager
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

data class RenderOption(val size: String, val titleRes: Int)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RenderResolutionScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    val currentRenderResolution by viewModel.currentRenderResolution.collectAsState()
    val isAutoRender by viewModel.isAutoRenderResolution.collectAsState()
    val currentOutputResolution by viewModel.currentResolution.collectAsState()

    val recommendedAutoRender = remember(currentOutputResolution) {
        DisplayManager.computeAutoRenderResolution(currentOutputResolution)
    }

    val options = listOf(
        RenderOption("1280x720", R.string.hd_720p),
        RenderOption("1920x1080", R.string.fhd_1080p),
        RenderOption("3840x2160", R.string.uhd_4k),
    )

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
                Text(
                    text = stringResource(R.string.select_render_resolution),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // 1. Automatic option
                item {
                    SettingsItem(
                        title = stringResource(R.string.auto),
                        subtitle = stringResource(R.string.auto_render_subtitle, currentRenderResolution),
                        icon = SettingsScreen,
                        onClick = { viewModel.setRenderResolutionAuto() },
                        isCurrent = isAutoRender,
                    )
                }

                // 2. Manual options
                items(options) { opt ->
                    val isCurrent = !isAutoRender && currentRenderResolution == opt.size
                    val isRecommended = recommendedAutoRender == opt.size
                    val subtitle = if (isRecommended) stringResource(R.string.recommended) else null

                    SettingsItem(
                        title = stringResource(opt.titleRes),
                        subtitle = subtitle,
                        icon = getIconForResolution(opt.size),
                        onClick = { viewModel.setRenderResolutionManual(opt.size) },
                        isCurrent = isCurrent,
                    )
                }

                // 3. Back option
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

        // Right pane: Decorative
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
