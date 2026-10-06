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
val hdr_on: ImageVector
    get() {
        if (_hdr_on != null) {
            return _hdr_on!!
        }
        _hdr_on =
            ImageVector.Builder(
                name = "hdr_on",
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
                        moveTo(16f, 15f)
                        verticalLineTo(9f)
                        horizontalLineToRelative(3.5f)
                        quadToRelative(0.6f, 0f, 1.05f, 0.45f)
                        reflectiveQuadTo(21f, 10.5f)
                        verticalLineToRelative(1f)
                        quadToRelative(0f, 0.57f, -0.26f, 0.89f)
                        reflectiveQuadTo(20.1f, 12.9f)
                        lineTo(21f, 15f)
                        horizontalLineTo(19.5f)
                        lineTo(18.6f, 13f)
                        horizontalLineTo(17.5f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(16f)
                        close()
                        moveToRelative(1.5f, -3.5f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(-1f)
                        horizontalLineToRelative(-2f)
                        verticalLineToRelative(1f)
                        close()
                        moveTo(3f, 15f)
                        verticalLineTo(9f)
                        horizontalLineTo(4.5f)
                        verticalLineToRelative(2f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(9f)
                        horizontalLineTo(8f)
                        verticalLineToRelative(6f)
                        horizontalLineTo(6.5f)
                        verticalLineTo(12.5f)
                        horizontalLineToRelative(-2f)
                        verticalLineTo(15f)
                        horizontalLineTo(3f)
                        close()
                        moveToRelative(6.5f, 0f)
                        verticalLineTo(9f)
                        horizontalLineTo(13f)
                        quadToRelative(0.6f, 0f, 1.05f, 0.45f)
                        reflectiveQuadTo(14.5f, 10.5f)
                        verticalLineToRelative(3f)
                        quadToRelative(0f, 0.6f, -0.45f, 1.05f)
                        reflectiveQuadTo(13f, 15f)
                        horizontalLineTo(9.5f)
                        close()
                        moveTo(11f, 13.5f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(-3f)
                        horizontalLineTo(11f)
                        verticalLineToRelative(3f)
                        close()
                    }
                }
                .build()
        return _hdr_on!!
    }

private var _hdr_on: ImageVector? = null
