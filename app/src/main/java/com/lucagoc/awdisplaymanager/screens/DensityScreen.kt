package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem
import com.lucagoc.awdisplaymanager.ui.icons.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DensityScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    val currentDensity by viewModel.currentDensity.collectAsState()
    val densities = listOf(160, 213, 240, 320, 400, 480, 640)

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F0F))) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 56.dp, top = 48.dp, end = 32.dp, bottom = 48.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = HighDensity,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(R.string.select_density), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(densities) { dpi ->
                    val isCurrent = currentDensity == dpi
                    SettingsItem(
                        title = stringResource(R.string.dpi_format, dpi),
                        subtitle = null,
                        icon = null,
                        onClick = { viewModel.setDensity(dpi) },
                        isCurrent = isCurrent,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingsItem(
                        title = stringResource(R.string.go_back),
                        icon = Icons.Default.Close,
                        onClick = onNavigateBack,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = HighDensity,
                contentDescription = null,
                modifier = Modifier.size(160.dp),
                tint = Color(0xFF333333),
            )
        }
    }
}
