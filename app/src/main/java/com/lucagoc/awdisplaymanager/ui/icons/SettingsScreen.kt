@file:Suppress("unused", "ObjectPropertyName")

package com.lucagoc.awdisplaymanager.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val SettingsScreen: ImageVector
  get() {
    if (_SettingsScreen != null) {
      return _SettingsScreen!!
    }
    _SettingsScreen =
      ImageVector.Builder(
          name = "settings_screen",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(15f, 22f)
            quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
            reflectiveQuadTo(13f, 20f)
            verticalLineTo(16f)
            quadToRelative(0f, -0.83f, 0.59f, -1.41f)
            reflectiveQuadTo(15f, 14f)
            horizontalLineToRelative(6f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(23f, 16f)
            verticalLineToRelative(4f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(21f, 22f)
            horizontalLineTo(15f)
            close()
            moveToRelative(0f, -2f)
            horizontalLineToRelative(6f)
            verticalLineTo(16f)
            horizontalLineTo(15f)
            verticalLineToRelative(4f)
            close()
            moveTo(11f, 15.35f)
            quadTo(9.93f, 15f, 9.24f, 14.1f)
            reflectiveQuadTo(8.55f, 12f)
            quadToRelative(0f, -1.45f, 1.03f, -2.48f)
            reflectiveQuadTo(12.05f, 8.5f)
            reflectiveQuadToRelative(2.47f, 1.02f)
            reflectiveQuadTo(15.55f, 12f)
            horizontalLineTo(14.5f)
            quadToRelative(-0.27f, 0f, -0.51f, 0.04f)
            reflectiveQuadToRelative(-0.46f, 0.11f)
            quadToRelative(0f, -0.05f, 0f, -0.09f)
            quadToRelative(0f, -0.04f, 0f, -0.09f)
            quadToRelative(0f, -0.63f, -0.42f, -1.05f)
            reflectiveQuadTo(12.05f, 10.5f)
            reflectiveQuadToRelative(-1.06f, 0.44f)
            reflectiveQuadTo(10.55f, 12f)
            quadToRelative(0f, 0.52f, 0.31f, 0.91f)
            reflectiveQuadToRelative(0.81f, 0.51f)
            quadToRelative(-0.3f, 0.43f, -0.47f, 0.9f)
            reflectiveQuadTo(11f, 15.35f)
            close()
            moveTo(12.05f, 12f)
            close()
            moveTo(9.25f, 22f)
            lineTo(8.85f, 18.8f)
            quadTo(8.53f, 18.68f, 8.24f, 18.5f)
            reflectiveQuadTo(7.68f, 18.13f)
            lineTo(4.7f, 19.38f)
            lineTo(1.95f, 14.63f)
            lineTo(4.53f, 12.68f)
            quadTo(4.5f, 12.5f, 4.5f, 12.34f)
            quadToRelative(0f, -0.16f, 0f, -0.34f)
            reflectiveQuadToRelative(0f, -0.34f)
            reflectiveQuadTo(4.53f, 11.33f)
            lineTo(1.95f, 9.38f)
            lineTo(4.7f, 4.63f)
            lineTo(7.68f, 5.88f)
            quadTo(7.95f, 5.68f, 8.25f, 5.5f)
            reflectiveQuadTo(8.85f, 5.2f)
            lineTo(9.25f, 2f)
            horizontalLineToRelative(5.5f)
            lineToRelative(0.4f, 3.2f)
            quadToRelative(0.33f, 0.13f, 0.61f, 0.3f)
            reflectiveQuadToRelative(0.56f, 0.38f)
            lineTo(19.3f, 4.63f)
            lineToRelative(2.75f, 4.75f)
            lineToRelative(-2.57f, 1.95f)
            quadToRelative(0.02f, 0.18f, 0.02f, 0.34f)
            reflectiveQuadToRelative(0f, 0.34f)
            horizontalLineToRelative(-2f)
            quadTo(17.48f, 11.52f, 17.43f, 11.16f)
            reflectiveQuadTo(17.28f, 10.48f)
            lineTo(19.43f, 8.85f)
            lineTo(18.45f, 7.15f)
            lineTo(15.98f, 8.2f)
            quadTo(15.43f, 7.63f, 14.76f, 7.24f)
            reflectiveQuadTo(13.33f, 6.65f)
            lineTo(13f, 4f)
            horizontalLineTo(11.03f)
            lineTo(10.68f, 6.65f)
            quadTo(9.9f, 6.85f, 9.24f, 7.24f)
            reflectiveQuadTo(8.03f, 8.17f)
            lineTo(5.55f, 7.15f)
            lineTo(4.58f, 8.85f)
            lineToRelative(2.15f, 1.6f)
            quadTo(6.6f, 10.83f, 6.55f, 11.2f)
            reflectiveQuadTo(6.5f, 12f)
            quadToRelative(0f, 0.4f, 0.05f, 0.77f)
            reflectiveQuadToRelative(0.17f, 0.75f)
            lineTo(4.58f, 15.15f)
            lineToRelative(0.98f, 1.7f)
            lineTo(8.03f, 15.8f)
            quadToRelative(0.6f, 0.63f, 1.35f, 1.05f)
            quadTo(10.13f, 17.27f, 11f, 17.4f)
            verticalLineTo(22f)
            horizontalLineTo(9.25f)
            close()
            moveToRelative(2.8f, -10f)
            close()
            moveTo(18f, 18f)
            close()
          }
        }
        .build()
    return _SettingsScreen!!
  }

private var _SettingsScreen: ImageVector? = null
