package com.lucagoc.awdisplaymanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.R
import com.lucagoc.awdisplaymanager.ui.icons.Resize

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun OverscanScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    val overscan by viewModel.overscan.collectAsState()
    val margin = overscan.firstOrNull() ?: 100

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.startOverscanAdjustment()
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF003366)) // Dark Blue to see physical limits
            .border(8.dp, Color.Red), // Thick red border to easily spot edges
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color(0xCC000000), MaterialTheme.shapes.medium)
                .padding(32.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Resize, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.screen_adjustment),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.margin_percent, margin),
                fontSize = 24.sp,
                color = Color.White,
            )
            Text(
                text = stringResource(R.string.use_dpad_to_adjust),
                fontSize = 16.sp,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { /* Do nothing, just to hold focus */ },
                    modifier = Modifier
                        .onKeyEvent { event ->
                            when (event.type) {
                                KeyEventType.KeyDown -> {
                                    when (event.key) {
                                        Key.DirectionUp -> {
                                            viewModel.updateOverscan(margin + 1)
                                            true
                                        }
                                        Key.DirectionDown -> {
                                            viewModel.updateOverscan(margin - 1)
                                            true
                                        }
                                        else -> false
                                    }
                                }
                                else -> false
                            }
                        }
                        .focusRequester(focusRequester),
                    colors = ButtonDefaults.colors(containerColor = Color.White, contentColor = Color.Black),
                ) {
                    Icon(Resize, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.adjust))
                }
                
                Button(
                    onClick = {
                        viewModel.saveOverscan()
                        onNavigateBack()
                    },
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.save))
                }
                Button(
                    onClick = {
                        viewModel.updateOverscan(100)
                    },
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.reset))
                }
                Button(
                    onClick = {
                        viewModel.cancelOverscan()
                        onNavigateBack()
                    },
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}
