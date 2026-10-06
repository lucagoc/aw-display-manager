package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.block
import com.lucagoc.awdisplaymanager.ui.icons.hdr_auto
import com.lucagoc.awdisplaymanager.ui.icons.hdr_on

data class HdrOption(
    val id: String,
    val titleRes: Int,
    val icon: ImageVector,
    val subtitleRes: Int? = null,
    val enabled: Boolean = true,
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HdrRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val currentHdrMode by viewModel.currentHdrMode.collectAsState()
    val isHdrSupported by viewModel.isHdrSupported.collectAsState()

    val options = remember(isHdrSupported) {
        listOf(
            HdrOption(
                id = "AUTO",
                titleRes = R.string.hdr_auto,
                icon = hdr_auto,
            ),
            HdrOption(
                id = "HDR",
                titleRes = R.string.hdr_force_hdr,
                icon = hdr_on,
                subtitleRes = if (!isHdrSupported) R.string.hdr_not_supported else null,
                enabled = isHdrSupported,
            ),
            HdrOption(
                id = "SDR",
                titleRes = R.string.hdr_force_sdr,
                icon = block,
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.select_hdr),
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
            items(options) { opt ->
                val isCurrent = currentHdrMode.equals(opt.id, ignoreCase = true)
                val itemModifier = if (opt.id == currentHdrMode && focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier

                SettingsItem(
                    title = stringResource(opt.titleRes),
                    subtitle = opt.subtitleRes?.let { stringResource(it) },
                    icon = opt.icon,
                    onClick = { viewModel.setHdrMode(opt.id) },
                    isCurrent = isCurrent,
                    enabled = opt.enabled,
                    modifier = itemModifier,
                )
            }
        }
    }
}
