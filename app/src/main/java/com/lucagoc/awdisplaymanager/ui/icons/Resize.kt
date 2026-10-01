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
val Resize: ImageVector
  get() {
    if (_Resize != null) {
      return _Resize!!
    }
    _Resize =
      ImageVector.Builder(
          name = "resize",
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
            moveTo(19f, 9f)
            verticalLineTo(5f)
            horizontalLineTo(15f)
            verticalLineTo(3f)
            horizontalLineToRelative(6f)
            verticalLineTo(9f)
            horizontalLineTo(19f)
            close()
            moveTo(3f, 21f)
            verticalLineTo(15f)
            horizontalLineTo(5f)
            verticalLineToRelative(4f)
            horizontalLineTo(9f)
            verticalLineToRelative(2f)
            horizontalLineTo(3f)
            close()
            moveTo(3f, 13f)
            verticalLineTo(11f)
            horizontalLineTo(5f)
            verticalLineToRelative(2f)
            horizontalLineTo(3f)
            close()
            moveTo(3f, 9f)
            verticalLineTo(7f)
            horizontalLineTo(5f)
            verticalLineTo(9f)
            horizontalLineTo(3f)
            close()
            moveTo(3f, 5f)
            verticalLineTo(3f)
            horizontalLineTo(5f)
            verticalLineTo(5f)
            horizontalLineTo(3f)
            close()
            moveTo(7f, 5f)
            verticalLineTo(3f)
            horizontalLineTo(9f)
            verticalLineTo(5f)
            horizontalLineTo(7f)
            close()
            moveToRelative(4f, 16f)
            verticalLineTo(19f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(11f)
            close()
            moveTo(11f, 5f)
            verticalLineTo(3f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            horizontalLineTo(11f)
            close()
            moveToRelative(4f, 16f)
            verticalLineTo(19f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(15f)
            close()
            moveToRelative(4f, 0f)
            verticalLineTo(19f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(19f)
            close()
            moveToRelative(0f, -4f)
            verticalLineTo(15f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(19f)
            close()
            moveToRelative(0f, -4f)
            verticalLineTo(11f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(19f)
            close()
          }
        }
        .build()
    return _Resize!!
  }

private var _Resize: ImageVector? = null
