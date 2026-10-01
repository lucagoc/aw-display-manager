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
val TwoK: ImageVector
  get() {
    if (_TwoK != null) {
      return _TwoK!!
    }
    _TwoK =
      ImageVector.Builder(
          name = "2k",
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
            moveTo(13f, 15f)
            horizontalLineToRelative(1.5f)
            verticalLineTo(12.75f)
            lineTo(16.25f, 15f)
            horizontalLineToRelative(1.82f)
            lineTo(15.75f, 12f)
            lineTo(18.08f, 9f)
            horizontalLineTo(16.25f)
            lineTo(14.5f, 11.25f)
            verticalLineTo(9f)
            horizontalLineTo(13f)
            verticalLineToRelative(6f)
            close()
            moveTo(6.5f, 15f)
            horizontalLineTo(11f)
            verticalLineTo(13.5f)
            horizontalLineTo(8f)
            verticalLineToRelative(-1f)
            horizontalLineToRelative(2f)
            quadToRelative(0.43f, 0f, 0.71f, -0.29f)
            quadTo(11f, 11.93f, 11f, 11.5f)
            verticalLineTo(10f)
            quadTo(11f, 9.57f, 10.71f, 9.29f)
            reflectiveQuadTo(10f, 9f)
            horizontalLineTo(6.5f)
            verticalLineToRelative(1.5f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(1f)
            horizontalLineToRelative(-2f)
            quadToRelative(-0.42f, 0f, -0.71f, 0.29f)
            reflectiveQuadTo(6.5f, 12.5f)
            verticalLineTo(15f)
            close()
            moveTo(5f, 21f)
            quadTo(4.18f, 21f, 3.59f, 20.41f)
            reflectiveQuadTo(3f, 19f)
            verticalLineTo(5f)
            quadTo(3f, 4.17f, 3.59f, 3.59f)
            reflectiveQuadTo(5f, 3f)
            horizontalLineTo(19f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(21f, 5f)
            verticalLineTo(19f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(19f, 21f)
            horizontalLineTo(5f)
            close()
            moveTo(5f, 19f)
            horizontalLineTo(19f)
            verticalLineTo(5f)
            horizontalLineTo(5f)
            verticalLineTo(19f)
            close()
            moveTo(5f, 5f)
            verticalLineTo(19f)
            verticalLineTo(5f)
            close()
          }
        }
        .build()
    return _TwoK!!
  }

private var _TwoK: ImageVector? = null
