package com.lucagoc.awdisplaymanager.screens

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.UpdateState
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.Resize
import com.lucagoc.awdisplaymanager.ui.icons.UpdateIcon

private val maintainerToasts = listOf(
    "ᗜˬᗜ",
    "ᗜ˰ᗜ",
    "ദ്ദി ᗜˬᗜ✧",
    "ᗜ_ᗜ",
    "ᗜ⩊ᗜ",
    "˵ᗜ⩊ᗜ˵",
    "🍺ᗜˬᗜ"
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AboutRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val context = LocalContext.current
    val updateState by viewModel.updateState.collectAsState()
    var showQrCodeDialog by remember { mutableStateOf(false) }

    if (showQrCodeDialog) {
        val closeFocusRequester = remember { FocusRequester() }
        val qrBitmap = remember(context) {
            try {
                context.assets.open("github_qr.png").use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                }
            } catch (_: Exception) {
                null
            }
        }

        LaunchedEffect(Unit) {
            closeFocusRequester.requestFocus()
        }

        Dialog(onDismissRequest = { showQrCodeDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(28.dp)
                ) {
                    Text(
                        text = stringResource(R.string.github),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "lucagoc/aw-display-manager",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (qrBitmap != null) {
                        Image(
                            bitmap = qrBitmap,
                            contentDescription = stringResource(R.string.github),
                            modifier = Modifier.size(200.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    Button(
                        onClick = { showQrCodeDialog = false },
                        modifier = Modifier.focusRequester(closeFocusRequester),
                        colors = ButtonDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(stringResource(R.string.cancel), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }

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
                    onClick = {
                        Toast.makeText(context, maintainerToasts.random(), Toast.LENGTH_SHORT).show()
                    },
                )
            }
            item {
                SettingsItem(
                    title = stringResource(R.string.github),
                    subtitle = "lucagoc/aw-display-manager",
                    iconPainter = painterResource(R.drawable.ic_github),
                    onClick = { showQrCodeDialog = true },
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
