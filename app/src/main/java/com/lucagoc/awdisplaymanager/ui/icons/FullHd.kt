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
val FullHd: ImageVector
  get() {
    if (_FullHd != null) {
      return _FullHd!!
    }
    _FullHd =
      ImageVector.Builder(
          name = "full_hd",
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
            moveTo(9.5f, 15f)
            horizontalLineTo(11f)
            verticalLineTo(13f)
            horizontalLineToRelative(1.5f)
            verticalLineToRelative(2f)
            horizontalLineTo(14f)
            verticalLineTo(9f)
            horizontalLineTo(12.5f)
            verticalLineToRelative(2.5f)
            horizontalLineTo(11f)
            verticalLineTo(9f)
            horizontalLineTo(9.5f)
            verticalLineToRelative(6f)
            close()
            moveTo(15f, 15f)
            horizontalLineToRelative(3.5f)
            quadToRelative(0.43f, 0f, 0.71f, -0.29f)
            reflectiveQuadTo(19.5f, 14f)
            verticalLineTo(10f)
            quadToRelative(0f, -0.43f, -0.29f, -0.71f)
            reflectiveQuadTo(18.5f, 9f)
            horizontalLineTo(15f)
            verticalLineToRelative(6f)
            close()
            moveToRelative(1.5f, -1.5f)
            verticalLineToRelative(-3f)
            horizontalLineTo(18f)
            verticalLineToRelative(3f)
            horizontalLineTo(16.5f)
            close()
            moveTo(4.5f, 15f)
            horizontalLineTo(6f)
            verticalLineTo(13f)
            horizontalLineTo(8f)
            verticalLineTo(11.5f)
            horizontalLineTo(6f)
            verticalLineToRelative(-1f)
            horizontalLineTo(8.5f)
            verticalLineTo(9f)
            horizontalLineToRelative(-4f)
            verticalLineToRelative(6f)
            close()
            moveTo(3f, 20f)
            quadTo(2.18f, 20f, 1.59f, 19.41f)
            reflectiveQuadTo(1f, 18f)
            verticalLineTo(6f)
            quadTo(1f, 5.18f, 1.59f, 4.59f)
            reflectiveQuadTo(3f, 4f)
            horizontalLineTo(21f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            quadTo(23f, 5.18f, 23f, 6f)
            verticalLineTo(18f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(21f, 20f)
            horizontalLineTo(3f)
            close()
            moveTo(3f, 18f)
            horizontalLineTo(21f)
            verticalLineTo(6f)
            horizontalLineTo(3f)
            verticalLineTo(18f)
            close()
            moveToRelative(0f, 0f)
            verticalLineTo(6f)
            verticalLineTo(18f)
            close()
          }
        }
        .build()
    return _FullHd!!
  }

private var _FullHd: ImageVector? = null
