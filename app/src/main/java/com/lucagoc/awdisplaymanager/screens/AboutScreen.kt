package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.UpdateState
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.Resize
import com.lucagoc.awdisplaymanager.ui.icons.UpdateIcon

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AboutRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val updateState by viewModel.updateState.collectAsState()

    val versionTitle = "${stringResource(R.string.version)} ${viewModel.appVersionName}"

    val updateSubtitle = when (val state = updateState) {
        is UpdateState.Idle -> stringResource(R.string.check_for_updates)
        is UpdateState.Checking -> stringResource(R.string.checking_updates)
        is UpdateState.UpToDate -> stringResource(R.string.app_up_to_date)
        is UpdateState.UpdateAvailable -> stringResource(R.string.update_available, state.latestVersion)
        is UpdateState.Downloading -> stringResource(R.string.downloading_update, state.progress)
        is UpdateState.Installing -> stringResource(R.string.installing_update)
        is UpdateState.Error -> stringResource(R.string.update_failed, state.message)
    }

    val itemModifier = if (focusRequester != null) {
        Modifier.focusRequester(focusRequester)
    } else Modifier

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SettingsItem(
                    title = stringResource(R.string.maintainer),
                    subtitle = "lucagoc",
                    iconPainter = painterResource(R.drawable.ic_person),
                    onClick = {},
                )
            }
            item {
                SettingsItem(
                    title = stringResource(R.string.github),
                    subtitle = "lucagoc/aw-display-manager",
                    iconPainter = painterResource(R.drawable.ic_github),
                    onClick = {},
                )
            }
            item {
                SettingsItem(
                    title = versionTitle,
                    subtitle = updateSubtitle,
                    icon = UpdateIcon,
                    onClick = {
                        if (updateState is UpdateState.UpdateAvailable) {
                            viewModel.startUpdate()
                        } else if (updateState !is UpdateState.Downloading && updateState !is UpdateState.Installing && updateState !is UpdateState.Checking) {
                            viewModel.checkForUpdates()
                        }
                    },
                    modifier = itemModifier,
                )
            }
        }
    }
}

@Composable
fun OverscanRightPane() {
    DecorativeRightPane(icon = Resize)
}

@Composable
fun AboutScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    AboutRightPane(viewModel = viewModel)
}
