package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.UpdateState
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreenLeftPane(
    viewModel: MainViewModel,
    selectedItem: SettingsMenu,
    onSelectItem: (SettingsMenu) -> Unit,
    onConfirmItem: (SettingsMenu) -> Unit,
    focusRequesters: Map<SettingsMenu, FocusRequester>,
    isLeftFocused: Boolean = true,
) {
    val currentResolution by viewModel.formattedCurrentResolution.collectAsState()
    val currentRenderResolution by viewModel.currentRenderResolution.collectAsState()
    val currentDensity by viewModel.currentDensity.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    val aboutSubtitle = when (val state = updateState) {
        is UpdateState.UpdateAvailable -> stringResource(R.string.update_available, state.latestVersion)
        else -> null
    }

    LaunchedEffect(isLeftFocused, selectedItem) {
        if (isLeftFocused) {
            try {
                focusRequesters[selectedItem]?.requestFocus()
            } catch (_: Exception) {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown && event.key == Key.DirectionRight) {
                    onConfirmItem(selectedItem)
                    true
                } else {
                    false
                }
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier.padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = DisplaySettings,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = stringResource(R.string.display_manager),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(
            title = stringResource(R.string.resolution),
            subtitle = currentResolution,
            icon = SettingsScreen,
            isSelected = (selectedItem == SettingsMenu.RESOLUTION),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.RESOLUTION] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.RESOLUTION) },
            onClick = { onConfirmItem(SettingsMenu.RESOLUTION) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem(
            title = stringResource(R.string.hdr),
            subtitle = when (viewModel.currentHdrMode.collectAsState().value.uppercase()) {
                "HDR" -> stringResource(R.string.hdr_force_hdr)
                "SDR" -> stringResource(R.string.hdr_force_sdr)
                else -> stringResource(R.string.hdr_auto)
            },
            icon = when (viewModel.currentHdrMode.collectAsState().value.uppercase()) {
                "HDR" -> hdr_on
                "SDR" -> block
                else -> hdr_auto
            },
            isSelected = (selectedItem == SettingsMenu.HDR),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.HDR] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.HDR) },
            onClick = { onConfirmItem(SettingsMenu.HDR) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem(
            title = stringResource(R.string.color_space),
            subtitle = when (viewModel.currentPixelFormat.collectAsState().value.uppercase()) {
                "RGB" -> stringResource(R.string.color_rgb)
                "YUV444" -> stringResource(R.string.color_yuv444)
                "YUV422" -> stringResource(R.string.color_yuv422)
                "YUV420" -> stringResource(R.string.color_yuv420)
                else -> viewModel.currentPixelFormat.collectAsState().value.ifEmpty { stringResource(R.string.color_rgb) }
            },
            icon = palette,
            isSelected = (selectedItem == SettingsMenu.COLOR_SPACE),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.COLOR_SPACE] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.COLOR_SPACE) },
            onClick = { onConfirmItem(SettingsMenu.COLOR_SPACE) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem(
            title = stringResource(R.string.overscan),
            subtitle = stringResource(R.string.adjust_screen_margins),
            icon = Resize,
            isSelected = (selectedItem == SettingsMenu.OVERSCAN),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.OVERSCAN] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.OVERSCAN) },
            onClick = { onConfirmItem(SettingsMenu.OVERSCAN) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem(
            title = stringResource(R.string.advanced_options),
            subtitle = stringResource(R.string.advanced_subtitle, currentRenderResolution, currentDensity),
            icon = DisplaySettings,
            isSelected = (selectedItem == SettingsMenu.ADVANCED),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.ADVANCED] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.ADVANCED) },
            onClick = { onConfirmItem(SettingsMenu.ADVANCED) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem(
            title = stringResource(R.string.about),
            subtitle = aboutSubtitle,
            icon = Icons.Default.Info,
            isSelected = (selectedItem == SettingsMenu.ABOUT),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.ABOUT] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.ABOUT) },
            onClick = { onConfirmItem(SettingsMenu.ABOUT) },
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DecorativeRightPane(
    icon: ImageVector = DisplaySettings
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(110.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
        )
    }
}
