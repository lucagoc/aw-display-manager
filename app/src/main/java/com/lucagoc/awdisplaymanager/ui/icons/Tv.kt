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

val Tv: ImageVector
    get() {
        if (_Tv != null) {
            return _Tv!!
        }
        _Tv = ImageVector.Builder(
            name = "Tv",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(21f, 3f)
                horizontalLineTo(3f)
                curveTo(1.9f, 3f, 1f, 3.9f, 1f, 5f)
                verticalLineTo(17f)
                curveTo(1f, 18.1f, 1.9f, 19f, 3f, 19f)
                horizontalLineTo(8f)
                verticalLineTo(21f)
                horizontalLineTo(16f)
                verticalLineTo(19f)
                horizontalLineTo(21f)
                curveTo(22.1f, 19f, 23f, 18.1f, 23f, 17f)
                verticalLineTo(5f)
                curveTo(23f, 3.9f, 22.1f, 3f, 21f, 3f)
                close()
                moveTo(21f, 17f)
                horizontalLineTo(3f)
                verticalLineTo(5f)
                horizontalLineTo(21f)
                verticalLineTo(17f)
                close()
            }
        }.build()
        return _Tv!!
    }

private var _Tv: ImageVector? = null
