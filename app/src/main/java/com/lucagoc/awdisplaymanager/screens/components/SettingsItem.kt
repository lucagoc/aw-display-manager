package com.lucagoc.awdisplaymanager.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    onClick: () -> Unit,
    isCurrent: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(value = false) }
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().onFocusChanged { isFocused = it.isFocused },
        colors = ButtonDefaults.colors(
            containerColor = if (isFocused) Color.White else Color.Transparent,
            contentColor = if (isFocused) Color.Black else Color.White,
        ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
            } else if (iconPainter != null) {
                Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                subtitle?.let { sub ->
                    Text(
                        text = sub,
                        fontSize = 14.sp,
                        color = if (isFocused) Color.DarkGray else Color.LightGray,
                    )
                }
            }
            if (isCurrent) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Current",
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}
