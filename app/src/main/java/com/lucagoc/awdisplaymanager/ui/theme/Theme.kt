package com.lucagoc.awdisplaymanager.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AllwinnerScreenSettingsTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = darkColorScheme(
        primary = TvPrimary,
        onPrimary = TvOnPrimary,
        primaryContainer = TvPrimaryContainer,
        onPrimaryContainer = TvOnPrimaryContainer,
        secondary = TvSecondary,
        onSecondary = TvOnSecondary,
        secondaryContainer = TvSecondaryContainer,
        onSecondaryContainer = TvOnSecondaryContainer,
        surface = TvSurface,
        onSurface = TvOnSurface,
        onSurfaceVariant = TvOnSurfaceVariant,
        surfaceVariant = TvSurfaceContainerHigh,
        border = TvOutlineVariant,
        errorContainer = TvErrorContainer,
        onErrorContainer = TvOnErrorContainer,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
