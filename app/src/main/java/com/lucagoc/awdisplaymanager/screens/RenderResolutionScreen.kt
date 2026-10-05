package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.DisplayManager
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

data class RenderOption(val size: String, val titleRes: Int)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RenderResolutionRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val currentRenderResolution by viewModel.currentRenderResolution.collectAsState()
    val isAutoRender by viewModel.isAutoRenderResolution.collectAsState()
    val currentOutputResolution by viewModel.currentResolution.collectAsState()

    val recommendedAutoRender = remember(currentOutputResolution) {
        DisplayManager.computeAutoRenderResolution(currentOutputResolution)
    }

    val options = remember {
        listOf(
            RenderOption("1280x720", R.string.hd_720p),
            RenderOption("1920x1080", R.string.fhd_1080p),
        )
    }

    val listState = rememberLazyListState()

    val activeOptionIndex = remember(isAutoRender, currentRenderResolution, options) {
        if (isAutoRender) 0
        else {
            val idx = options.indexOfFirst { it.size == currentRenderResolution }
            if (idx >= 0) idx + 1 else 0
        }
    }

    LaunchedEffect(activeOptionIndex) {
        if (activeOptionIndex >= 0) {
            try {
                listState.scrollToItem(activeOptionIndex)
            } catch (_: Exception) {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.render_resolution),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 1. Automatic option
            item {
                val autoModifier = if (activeOptionIndex == 0 && focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier

                SettingsItem(
                    title = stringResource(R.string.auto),
                    subtitle = stringResource(R.string.auto_render_subtitle, currentRenderResolution),
                    icon = SettingsScreen,
                    onClick = { viewModel.setRenderResolutionAuto() },
                    isCurrent = isAutoRender,
                    modifier = autoModifier,
                )
            }

            // 2. Manual options (720p and 1080p)
            itemsIndexed(options) { index, opt ->
                val isCurrent = !isAutoRender && currentRenderResolution == opt.size
                val isRecommended = recommendedAutoRender == opt.size
                val subtitle = if (isRecommended) stringResource(R.string.recommended) else null

                val itemModifier = if ((index + 1) == activeOptionIndex && focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier

                SettingsItem(
                    title = stringResource(opt.titleRes),
                    subtitle = subtitle,
                    icon = getIconForResolution(opt.size),
                    onClick = { viewModel.setRenderResolutionManual(opt.size) },
                    isCurrent = isCurrent,
                    modifier = itemModifier,
                )
            }
        }
    }
}

@Composable
fun RenderResolutionScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    RenderResolutionRightPane(viewModel = viewModel)
}
