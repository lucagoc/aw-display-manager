package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
fun DensityRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val currentDensity by viewModel.currentDensity.collectAsState()
    val isAutoDensity by viewModel.isAutoDensity.collectAsState()
    val recommendedDensity by viewModel.recommendedDensity.collectAsState()

    val densities = listOf(160, 213, 240, 280, 320, 360, 400, 480, 640)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.density),
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
            // 1. Automatic option
            item {
                val autoModifier = if (isAutoDensity && focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier

                SettingsItem(
                    title = stringResource(R.string.auto),
                    subtitle = stringResource(R.string.auto_density_subtitle, recommendedDensity),
                    icon = HighDensity,
                    onClick = { viewModel.setDensityAuto() },
                    isCurrent = isAutoDensity,
                    modifier = autoModifier,
                )
            }

            // 2. Manual options
            items(densities) { dpi ->
                val isCurrent = !isAutoDensity && currentDensity == dpi
                val isRecommended = dpi == recommendedDensity
                val subtitle = if (isRecommended) stringResource(R.string.recommended) else null

                val itemModifier = if (isCurrent && focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier

                SettingsItem(
                    title = stringResource(R.string.dpi_format, dpi),
                    subtitle = subtitle,
                    icon = null,
                    onClick = { viewModel.setDensityManual(dpi) },
                    isCurrent = isCurrent,
                    modifier = itemModifier,
                )
            }
        }
    }
}

@Composable
fun DensityScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    DensityRightPane(viewModel = viewModel)
}
