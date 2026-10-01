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
val Hd: ImageVector
  get() {
    if (_Hd != null) {
      return _Hd!!
    }
    _Hd =
      ImageVector.Builder(
          name = "hd",
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
            moveTo(6f, 15f)
            horizontalLineTo(7.5f)
            verticalLineTo(13f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(11f)
            verticalLineTo(9f)
            horizontalLineTo(9.5f)
            verticalLineToRelative(2.5f)
            horizontalLineToRelative(-2f)
            verticalLineTo(9f)
            horizontalLineTo(6f)
            verticalLineToRelative(6f)
            close()
            moveToRelative(7f, 0f)
            horizontalLineToRelative(4f)
            quadToRelative(0.43f, 0f, 0.71f, -0.29f)
            reflectiveQuadTo(18f, 14f)
            verticalLineTo(10f)
            quadTo(18f, 9.57f, 17.71f, 9.29f)
            reflectiveQuadTo(17f, 9f)
            horizontalLineTo(13f)
            verticalLineToRelative(6f)
            close()
            moveToRelative(1.5f, -1.5f)
            verticalLineToRelative(-3f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(3f)
            horizontalLineToRelative(-2f)
            close()
            moveTo(4f, 20f)
            quadTo(3.18f, 20f, 2.59f, 19.41f)
            reflectiveQuadTo(2f, 18f)
            verticalLineTo(6f)
            quadTo(2f, 5.18f, 2.59f, 4.59f)
            reflectiveQuadTo(4f, 4f)
            horizontalLineTo(20f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            quadTo(22f, 5.18f, 22f, 6f)
            verticalLineTo(18f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(20f, 20f)
            horizontalLineTo(4f)
            close()
            moveTo(4f, 18f)
            horizontalLineTo(20f)
            verticalLineTo(6f)
            horizontalLineTo(4f)
            verticalLineTo(18f)
            close()
            moveToRelative(0f, 0f)
            verticalLineTo(6f)
            verticalLineTo(18f)
            close()
          }
        }
        .build()
    return _Hd!!
  }

private var _Hd: ImageVector? = null
