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
import com.lucagoc.awdisplaymanager.ui.icons.palette

data class ColorSpaceOption(
    val id: String,
    val titleRes: Int,
    val subtitleRes: Int? = null,
    val enabled: Boolean = true,
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ColorSpaceRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val currentPixelFormat by viewModel.currentPixelFormat.collectAsState()
    val supportedPixelFormats by viewModel.supportedPixelFormats.collectAsState()

    val options = remember(supportedPixelFormats) {
        listOf(
            ColorSpaceOption(
                id = "RGB",
                titleRes = R.string.color_rgb,
                enabled = supportedPixelFormats.contains("RGB"),
                subtitleRes = if (!supportedPixelFormats.contains("RGB")) R.string.hdr_not_supported else null,
            ),
            ColorSpaceOption(
                id = "YUV444",
                titleRes = R.string.color_yuv444,
                enabled = supportedPixelFormats.contains("YUV444"),
                subtitleRes = if (!supportedPixelFormats.contains("YUV444")) R.string.hdr_not_supported else null,
            ),
            ColorSpaceOption(
                id = "YUV422",
                titleRes = R.string.color_yuv422,
                enabled = supportedPixelFormats.contains("YUV422"),
                subtitleRes = if (!supportedPixelFormats.contains("YUV422")) R.string.hdr_not_supported else null,
            ),
            ColorSpaceOption(
                id = "YUV420",
                titleRes = R.string.color_yuv420,
                enabled = supportedPixelFormats.contains("YUV420"),
                subtitleRes = if (!supportedPixelFormats.contains("YUV420")) R.string.hdr_not_supported else null,
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.select_color_space),
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
                val isCurrent = currentPixelFormat.equals(opt.id, ignoreCase = true)
                val itemModifier = if (opt.id == currentPixelFormat && focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier

                SettingsItem(
                    title = stringResource(opt.titleRes),
                    subtitle = opt.subtitleRes?.let { stringResource(it) },
                    icon = palette,
                    onClick = { viewModel.setPixelFormat(opt.id) },
                    isCurrent = isCurrent,
                    enabled = opt.enabled,
                    modifier = itemModifier,
                )
            }
        }
    }
}
