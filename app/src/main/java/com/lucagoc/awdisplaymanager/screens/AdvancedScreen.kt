package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AdvancedLeftPane(
    viewModel: MainViewModel,
    selectedItem: SettingsMenu,
    onSelectItem: (SettingsMenu) -> Unit,
    onConfirmItem: (SettingsMenu) -> Unit,
    onNavigateBack: () -> Unit,
    focusRequesters: Map<SettingsMenu, FocusRequester>,
    requestInitialFocus: Boolean = true,
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

    LaunchedEffect(requestInitialFocus, selectedItem) {
        if (requestInitialFocus) {
            try {
                focusRequesters[selectedItem]?.requestFocus()
            } catch (_: Exception) {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionLeft -> {
                            onNavigateBack()
                            true
                        }
                        Key.DirectionRight -> {
                            onConfirmItem(selectedItem)
                            true
                        }
                        else -> false
                    }
                } else false
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
                text = stringResource(R.string.advanced_options),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(
            title = stringResource(R.string.render_resolution),
            subtitle = renderSubtitle,
            icon = SettingsScreen,
            isSelected = (selectedItem == SettingsMenu.RENDER_RESOLUTION),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.RENDER_RESOLUTION] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.RENDER_RESOLUTION) },
            onClick = { onConfirmItem(SettingsMenu.RENDER_RESOLUTION) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem(
            title = stringResource(R.string.density),
            subtitle = densitySubtitle,
            icon = HighDensity,
            isSelected = (selectedItem == SettingsMenu.DENSITY),
            modifier = Modifier
                .focusRequester(focusRequesters[SettingsMenu.DENSITY] ?: remember { FocusRequester() })
                .onFocusChanged { if (it.isFocused) onSelectItem(SettingsMenu.DENSITY) },
            onClick = { onConfirmItem(SettingsMenu.DENSITY) },
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AdvancedSubMenuRightPane(
    viewModel: MainViewModel,
    onNavigateToSubItem: (SettingsMenu) -> Unit,
    focusRequester: FocusRequester? = null
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

    val firstItemModifier = if (focusRequester != null) {
        Modifier.focusRequester(focusRequester)
    } else Modifier

    Column(modifier = Modifier.fillMaxSize()) {
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
                text = stringResource(R.string.advanced_options),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(
            title = stringResource(R.string.render_resolution),
            subtitle = renderSubtitle,
            icon = SettingsScreen,
            onClick = { onNavigateToSubItem(SettingsMenu.RENDER_RESOLUTION) },
            modifier = firstItemModifier,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem(
            title = stringResource(R.string.density),
            subtitle = densitySubtitle,
            icon = HighDensity,
            onClick = { onNavigateToSubItem(SettingsMenu.DENSITY) }
        )
    }
}

@Composable
fun AdvancedRightPane(
    viewModel: MainViewModel,
    selectedSubItem: SettingsMenu,
    onSelectSubItem: (SettingsMenu) -> Unit,
    onNavigateToSubItem: (SettingsMenu) -> Unit,
) {
    when (selectedSubItem) {
        SettingsMenu.RENDER_RESOLUTION -> RenderResolutionRightPane(viewModel = viewModel)
        SettingsMenu.DENSITY -> DensityRightPane(viewModel = viewModel)
        else -> RenderResolutionRightPane(viewModel = viewModel)
    }
}

@Composable
fun AdvancedScreen(
    viewModel: MainViewModel,
    onNavigateToRenderResolution: () -> Unit,
    onNavigateToDensity: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    AdvancedRightPane(
        viewModel = viewModel,
        selectedSubItem = SettingsMenu.RENDER_RESOLUTION,
        onSelectSubItem = {},
        onNavigateToSubItem = {}
    )
}
