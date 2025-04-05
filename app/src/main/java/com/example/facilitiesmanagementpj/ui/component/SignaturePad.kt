package com.example.facilitiesmanagementpj.ui.component

// File: ui/components/SignaturePad.kt
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Icon
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.graphics.createBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize


@Composable
fun SignaturePad(
    modifier: Modifier = Modifier,
    onSigned: (Bitmap) -> Unit
) {
    val density = LocalDensity.current
    val paths = remember { mutableStateListOf<Path>() }
    val currentPath = remember { mutableStateOf(Path()) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    // Vẽ nét
    Canvas(
        modifier = modifier
            .onSizeChanged { canvasSize = it }
            .pointerInput(Unit) {
                var points = mutableListOf<Offset>()
                detectDragGestures(
                    onDragStart = { offset ->
                        points = mutableListOf(offset)
                        currentPath.value = Path().apply { moveTo(offset.x, offset.y) }
                    },
                    onDrag = { change, _ ->
                        points.add(change.position)
                        currentPath.value = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            points.drop(1).forEach { lineTo(it.x, it.y) }
                        }
                    },
                    onDragEnd = {
                        paths.add(currentPath.value)
                        currentPath.value = Path()
                    }
                )
            }
    ) {
        paths.forEach { drawPath(it, color = Color.Black, style = Stroke(width = 6f)) }
        drawPath(currentPath.value, color = Color.Black, style = Stroke(width = 6f))
    }

    // Nút Xoá và Xác nhận
    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = {
                paths.clear()
                currentPath.value = Path()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.White.copy(alpha = 0.9f), CircleShape)
        ) {
            Icon(Icons.Default.Clear, contentDescription = "Xoá chữ ký", tint = Color.Black)
        }

        Button(
            onClick = {
                val widthPx = with(density) { canvasSize.width }
                val heightPx = with(density) { canvasSize.height }
                val bitmap = createBitmap(widthPx, heightPx)
                val canvas = android.graphics.Canvas(bitmap)
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    strokeWidth = 6f
                    style = android.graphics.Paint.Style.STROKE
                    isAntiAlias = true
                }
                paths.forEach { canvas.drawPath(it.asAndroidPath(), paint) }
                onSigned(bitmap)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Xác nhận chữ ký")
        }
    }
}









