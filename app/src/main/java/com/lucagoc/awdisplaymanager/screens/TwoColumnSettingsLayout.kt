package com.lucagoc.awdisplaymanager.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.lucagoc.awdisplaymanager.MainViewModel
import com.lucagoc.awdisplaymanager.ui.theme.TvSurfaceContainer
import com.lucagoc.awdisplaymanager.ui.theme.TvSurfaceContainerLow

enum class SettingsMenu {
    ROOT,
    RESOLUTION,
    HDR,
    COLOR_SPACE,
    ADVANCED,
    RENDER_RESOLUTION,
    DENSITY,
    OVERSCAN,
    ABOUT,
    OVERSCAN_FULL
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TwoColumnSettingsLayout(
    viewModel: MainViewModel,
) {
    var menuStack by remember { mutableStateOf(listOf(SettingsMenu.ROOT)) }
    val currentMenu = menuStack.last()

    // Focus state between Left and Right columns
    var isLeftFocused by remember { mutableStateOf(true) }

    // Right-pane preview selection when at ROOT or ADVANCED
    var selectedRootItem by remember { mutableStateOf(SettingsMenu.RESOLUTION) }
    var selectedAdvancedItem by remember { mutableStateOf(SettingsMenu.RENDER_RESOLUTION) }

    val rootFocusRequesters = remember {
        mapOf(
            SettingsMenu.RESOLUTION to FocusRequester(),
            SettingsMenu.HDR to FocusRequester(),
            SettingsMenu.COLOR_SPACE to FocusRequester(),
            SettingsMenu.ADVANCED to FocusRequester(),
            SettingsMenu.OVERSCAN to FocusRequester(),
            SettingsMenu.ABOUT to FocusRequester(),
        )
    }

    val advancedFocusRequesters = remember {
        mapOf(
            SettingsMenu.RENDER_RESOLUTION to FocusRequester(),
            SettingsMenu.DENSITY to FocusRequester(),
        )
    }

    val activeRightFocusRequester = remember { FocusRequester() }

    val focusRightColumn: () -> Unit = {
        isLeftFocused = false
        try {
            activeRightFocusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    val navigateTo: (SettingsMenu) -> Unit = { targetMenu ->
        isLeftFocused = true
        menuStack = menuStack + targetMenu
    }

    val navigateBack: () -> Unit = {
        if (menuStack.size > 1) {
            isLeftFocused = true
            menuStack = menuStack.dropLast(1)
        }
    }

    BackHandler(enabled = !isLeftFocused || menuStack.size > 1) {
        if (!isLeftFocused) {
            try {
                val targetRequester = when (currentMenu) {
                    SettingsMenu.ROOT -> rootFocusRequesters[selectedRootItem]
                    SettingsMenu.ADVANCED -> advancedFocusRequesters[selectedAdvancedItem]
                    else -> null
                }
                targetRequester?.requestFocus()
                isLeftFocused = true
            } catch (_: Exception) {}
        } else if (menuStack.size > 1) {
            navigateBack()
        }
    }

    AnimatedContent(
        targetState = menuStack,
        transitionSpec = {
            val isEnteringOverscan = targetState.last() == SettingsMenu.OVERSCAN_FULL
            val isExitingOverscan = initialState.last() == SettingsMenu.OVERSCAN_FULL

            if (isEnteringOverscan) {
                fadeIn(animationSpec = tween(350)) + scaleIn(initialScale = 0.92f, animationSpec = tween(350)) togetherWith
                        fadeOut(animationSpec = tween(350))
            } else if (isExitingOverscan) {
                fadeIn(animationSpec = tween(350)) togetherWith
                        fadeOut(animationSpec = tween(350)) + scaleOut(targetScale = 0.92f, animationSpec = tween(350))
            } else {
                val slideOffset = { fullWidth: Int -> (fullWidth * 0.42f).toInt() }
                if (targetState.size > initialState.size) {
                    slideInHorizontally(animationSpec = tween(350)) { slideOffset(it) } + fadeIn(tween(350)) togetherWith
                            slideOutHorizontally(animationSpec = tween(350)) { -slideOffset(it) } + fadeOut(tween(350))
                } else {
                    slideInHorizontally(animationSpec = tween(350)) { -slideOffset(it) } + fadeIn(tween(350)) togetherWith
                            slideOutHorizontally(animationSpec = tween(350)) { slideOffset(it) } + fadeOut(tween(350))
                }
            }
        },
        label = "MenuTransition"
    ) { currentStack ->
        val targetMenu = currentStack.last()

        if (targetMenu == SettingsMenu.OVERSCAN_FULL) {
            OverscanScreen(
                viewModel = viewModel,
                onNavigateBack = { navigateBack() }
            )
        } else {
            val rightPaneFocusRequester = remember { FocusRequester() }

            val currentLeftFocusRequester = when (targetMenu) {
                SettingsMenu.ROOT -> rootFocusRequesters[selectedRootItem]
                SettingsMenu.ADVANCED -> advancedFocusRequesters[selectedAdvancedItem]
                else -> null
            }

            LaunchedEffect(targetMenu, isLeftFocused) {
                if (!isLeftFocused) {
                    try {
                        activeRightFocusRequester.requestFocus()
                    } catch (_: Exception) {
                        try {
                            rightPaneFocusRequester.requestFocus()
                        } catch (_: Exception) {}
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Left Column (weight 0.42)
                Box(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight()
                        .background(TvSurfaceContainerLow)
                        .onFocusChanged { if (it.hasFocus) isLeftFocused = true }
                        .graphicsLayer { alpha = if (isLeftFocused) 1.0f else 0.65f }
                        .padding(start = 32.dp, top = 28.dp, end = 20.dp, bottom = 28.dp)
                ) {
                    when (targetMenu) {
                        SettingsMenu.ROOT -> {
                            HomeScreenLeftPane(
                                viewModel = viewModel,
                                selectedItem = selectedRootItem,
                                onSelectItem = { selectedRootItem = it },
                                onConfirmItem = { item ->
                                    when (item) {
                                        SettingsMenu.ADVANCED -> {
                                            isLeftFocused = true
                                            navigateTo(SettingsMenu.ADVANCED)
                                        }
                                        SettingsMenu.OVERSCAN -> {
                                            isLeftFocused = true
                                            navigateTo(SettingsMenu.OVERSCAN_FULL)
                                        }
                                        else -> {
                                            focusRightColumn()
                                        }
                                    }
                                },
                                focusRequesters = rootFocusRequesters,
                                isLeftFocused = isLeftFocused
                            )
                        }
                        SettingsMenu.ADVANCED -> {
                            AdvancedLeftPane(
                                viewModel = viewModel,
                                selectedItem = selectedAdvancedItem,
                                onSelectItem = { selectedAdvancedItem = it },
                                onConfirmItem = { _ ->
                                    focusRightColumn()
                                },
                                onNavigateBack = navigateBack,
                                focusRequesters = advancedFocusRequesters,
                                requestInitialFocus = isLeftFocused
                            )
                        }
                        else -> {}
                    }
                }

                // Right Column (weight 0.58)
                Box(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight()
                        .focusRequester(rightPaneFocusRequester)
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown) {
                                when (event.key) {
                                    Key.DirectionLeft, Key.Back, Key.Escape -> {
                                        try {
                                            currentLeftFocusRequester?.requestFocus()
                                            isLeftFocused = true
                                            true
                                        } catch (_: Exception) {
                                            false
                                        }
                                    }
                                    else -> false
                                }
                            } else false
                        }
                        .onFocusChanged { if (it.hasFocus) isLeftFocused = false }
                        .graphicsLayer { alpha = if (!isLeftFocused) 1.0f else 0.65f }
                        .background(TvSurfaceContainer)
                        .padding(start = 32.dp, top = 28.dp, end = 32.dp, bottom = 28.dp)
                ) {
                    val rightPaneTarget = when (targetMenu) {
                        SettingsMenu.ROOT -> selectedRootItem
                        SettingsMenu.ADVANCED -> selectedAdvancedItem
                        else -> SettingsMenu.ROOT
                    }

                    AnimatedContent(
                        targetState = rightPaneTarget,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                        },
                        label = "RightPaneTransition"
                    ) { target ->
                        when (target) {
                            SettingsMenu.RESOLUTION -> {
                                ResolutionRightPane(
                                    viewModel = viewModel,
                                    focusRequester = activeRightFocusRequester
                                )
                            }
                            SettingsMenu.HDR -> {
                                HdrRightPane(
                                    viewModel = viewModel,
                                    focusRequester = activeRightFocusRequester
                                )
                            }
                            SettingsMenu.COLOR_SPACE -> {
                                ColorSpaceRightPane(
                                    viewModel = viewModel,
                                    focusRequester = activeRightFocusRequester
                                )
                            }
                            SettingsMenu.ADVANCED -> {
                                AdvancedSubMenuRightPane(
                                    viewModel = viewModel,
                                    onNavigateToSubItem = { subItem ->
                                        selectedAdvancedItem = subItem
                                        navigateTo(SettingsMenu.ADVANCED)
                                    },
                                    focusRequester = activeRightFocusRequester
                                )
                            }
                            SettingsMenu.RENDER_RESOLUTION -> {
                                RenderResolutionRightPane(
                                    viewModel = viewModel,
                                    focusRequester = activeRightFocusRequester
                                )
                            }
                            SettingsMenu.DENSITY -> {
                                DensityRightPane(
                                    viewModel = viewModel,
                                    focusRequester = activeRightFocusRequester
                                )
                            }
                            SettingsMenu.OVERSCAN -> {
                                OverscanRightPane()
                            }
                            SettingsMenu.ABOUT -> {
                                AboutRightPane(
                                    viewModel = viewModel,
                                    focusRequester = activeRightFocusRequester
                                )
                            }
                            else -> {
                                DecorativeRightPane()
                            }
                        }
                    }
                }
            }
        }
    }
}
