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
import com.lucagoc.awdisplaymanager.ui.icons.DisplaySettings

data class ColorSpaceOption(
    val id: String,
    val titleRes: Int,
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ColorSpaceRightPane(
    viewModel: MainViewModel,
    focusRequester: FocusRequester? = null
) {
    val currentPixelFormat by viewModel.currentPixelFormat.collectAsState()

    val options = remember {
        listOf(
            ColorSpaceOption(
                id = "RGB",
                titleRes = R.string.color_rgb,
            ),
            ColorSpaceOption(
                id = "YUV444",
                titleRes = R.string.color_yuv444,
            ),
            ColorSpaceOption(
                id = "YUV422",
                titleRes = R.string.color_yuv422,
            ),
            ColorSpaceOption(
                id = "YUV420",
                titleRes = R.string.color_yuv420,
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
                    icon = DisplaySettings,
                    onClick = { viewModel.setPixelFormat(opt.id) },
                    isCurrent = isCurrent,
                    modifier = itemModifier,
                )
            }
        }
    }
}
