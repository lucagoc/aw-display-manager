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

val Overscan: ImageVector
    get() {
        if (_Overscan != null) {
            return _Overscan!!
        }
        _Overscan = ImageVector.Builder(
            name = "Overscan",
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
                moveTo(12f, 5.5f)
                lineTo(10f, 8f)
                horizontalLineToRelative(4f)
                lineToRelative(-2f, -2.5f)
                close()
                moveTo(18.5f, 12f)
                lineTo(16f, 10f)
                verticalLineToRelative(4f)
                lineToRelative(2.5f, -2f)
                close()
                moveTo(12f, 18.5f)
                lineTo(14f, 16f)
                horizontalLineToRelative(-4f)
                lineToRelative(2f, 2.5f)
                close()
                moveTo(5.5f, 12f)
                lineTo(8f, 14f)
                verticalLineToRelative(-4f)
                lineToRelative(-2.5f, 2f)
                close()
                moveTo(21f, 3f)
                lineTo(3f, 3f)
                curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                verticalLineToRelative(14f)
                curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
                horizontalLineToRelative(18f)
                curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
                lineTo(23f, 5f)
                curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
                close()
                moveTo(21f, 19f)
                lineTo(3f, 19f)
                lineTo(3f, 5f)
                horizontalLineToRelative(18f)
                verticalLineToRelative(14f)
                close()
            }
        }.build()
        return _Overscan!!
    }

private var _Overscan: ImageVector? = null
