package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.screens.components.SettingsItem

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AboutScreen(onNavigateBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F0F))) {
        // Left pane: Options
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 56.dp, top = 48.dp, end = 32.dp, bottom = 48.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.about),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(48.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    SettingsItem(
                        title = stringResource(R.string.version),
                        subtitle = "1.0",
                        icon = Icons.Default.Info,
                        onClick = {},
                    )
                }
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
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingsItem(
                        title = stringResource(R.string.go_back),
                        icon = Icons.Default.Close,
                        onClick = onNavigateBack,
                    )
                }
            }
        }
        // Right pane: Decorative
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(240.dp),
                tint = Color(0xFF333333),
            )
        }
    }
}
