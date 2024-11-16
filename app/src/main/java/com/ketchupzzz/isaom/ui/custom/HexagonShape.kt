package com.ketchupzzz.isaom.ui.custom

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


fun HexagonShape(): Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            moveTo(width / 2, 0f)
            lineTo(width, height / 4)
            lineTo(width, 3 * height / 4)
            lineTo(width / 2, height)
            lineTo(0f, 3 * height / 4)
            lineTo(0f, height / 4)
            close()
        }
        return Outline.Generic(path)
    }
}