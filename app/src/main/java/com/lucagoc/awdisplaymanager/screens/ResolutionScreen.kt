package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*
import com.lucagoc.awdisplaymanager.ui.theme.TvSurfaceContainerHigh

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
fun ResolutionRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val resolutions by viewModel.supportedResolutions.collectAsState()
    val currentResolution by viewModel.currentResolution.collectAsState()
    val countdown by viewModel.countdown.collectAsState()

    if (countdown != null) {
        val cancelFocusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            cancelFocusRequester.requestFocus()
        }

        Dialog(onDismissRequest = { }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.keep_this_resolution),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.reverting_in_seconds, countdown!!),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = { viewModel.confirmResolution() },
                            colors = ButtonDefaults.colors(
                                containerColor = TvSurfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.confirm), style = MaterialTheme.typography.labelLarge)
                        }
                        Button(
                            onClick = { viewModel.rollbackResolution() },
                            modifier = Modifier.focusRequester(cancelFocusRequester),
                            colors = ButtonDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.cancel), style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }

    val listState = rememberLazyListState()

    val activeIndex = remember(resolutions, currentResolution) {
        if (resolutions.isEmpty()) -1
        else {
            val idx = resolutions.indexOfFirst { it.name == currentResolution }
            if (idx >= 0) idx else 0
        }
    }

    LaunchedEffect(activeIndex, resolutions) {
        if (activeIndex >= 0 && activeIndex < resolutions.size) {
            try {
                listState.scrollToItem(activeIndex)
            } catch (_: Exception) {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.select_resolution),
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
            itemsIndexed(resolutions) { index, mode ->
                val isCurrent = currentResolution == mode.name
                val itemModifier = if (index == activeIndex && focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier

                SettingsItem(
                    title = mode.displayName,
                    icon = getIconForResolution(mode.displayName),
                    onClick = { viewModel.applyResolutionTemporarily(mode) },
                    isCurrent = isCurrent,
                    modifier = itemModifier,
                )
            }
        }
    }
}

@Composable
fun ResolutionScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    ResolutionRightPane(viewModel = viewModel)
}
