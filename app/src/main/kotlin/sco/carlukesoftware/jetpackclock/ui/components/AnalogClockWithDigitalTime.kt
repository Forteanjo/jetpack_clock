package sco.carlukesoftware.jetpackclock.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import sco.carlukesoftware.jetpackclock.ui.theme.JetpackClockTheme
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalogClockWithDigitalTime(
    modifier: Modifier = Modifier,
    currentTime: LocalTime = rememberCurrentTime()
) {
// Extract hour, minute, and second
    val hours = currentTime.hour
    val minutes = currentTime.minute
    val seconds = currentTime.second

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment
            .CenterHorizontally,
        verticalArrangement = Arrangement
            .Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    color = Color.Black,
                    shape = CircleShape
                ),
            contentAlignment = Alignment
                .Center,
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val center = Offset(
                    x = size.width / 2,
                    y = size.height / 2
                )
                val radius = size.minDimension / 2.2f

                // Draw clock face
                drawCircle(
                    color = Color.White,
                    radius = radius,
                    center = center
                )

                // Draw minute ticks (small lines for every minute)
                for (minuteTick in 0 until 60) {
                    val angle = Math
                        .toRadians(minuteTick * 6.0)
                        .toFloat()

                    val isHourMarker = minuteTick % 5 == 0
                    val startLength = if (isHourMarker) 0.8f else 0.9f // Longer for hour markers
                    val start = center + Offset(
                        x = radius * startLength * cos(angle),
                        y = radius * startLength * sin(angle)
                    )

                    val end = center + Offset(
                        x = radius * cos(angle),
                        y = radius * sin(angle)
                    )

                    drawLine(
                        color = Color.Black,
                        start = start,
                        end = end,
                        strokeWidth = if (isHourMarker) 4f else 2f
                    )
                }

                // Draw hour numbers (1-12)
                val textPaint = android.graphics.Paint()
                    .apply {
                        color = android.graphics.Color.BLACK
                        textSize = size.width / 12
                        textAlign = android.graphics.Paint.Align.CENTER
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                    }

                drawIntoCanvas { canvas ->
                    for (hour in 1..12) {
                        val angle = Math
                            .toRadians(hour * 30.0 - 90)
                            .toFloat()

                        val textOffset = center +
                                Offset(
                                    x = radius * 0.72f * cos(angle),
                                    y = radius * 0.72f * sin(angle)
                                )

                        canvas.nativeCanvas.drawText(
                            hour.toString(),
                            textOffset.x,
                            textOffset.y + (textPaint.textSize / 3), // Adjust baseline
                            textPaint
                        )
                    }
                }

                // Calculate angles
                val hourAngle = (hours % 12 + minutes / 60f) * 30 - 90
                val minuteAngle = (minutes + seconds / 60f) * 6 - 90
                val secondAngle = (seconds * 6) - 90

                // Draw clock hands
                drawHand(
                    angle = hourAngle,
                    length = radius * 0.5f,
                    strokeWidth = 8f,
                    color = Color.Black
                )
                drawHand(
                    angle = minuteAngle,
                    length = radius * 0.7f,
                    strokeWidth = 6f,
                    color = Color.DarkGray
                )
                drawHand(
                    angle = secondAngle.toFloat(),
                    length = radius * 0.9f,
                    strokeWidth = 2f,
                    color = Color.Red
                )

                // Draw center circle
                drawCircle(
                    color = Color.Black,
                    radius = 6f,
                    center = center
                )
            }
        }
    }
}

// Function to draw clock hands
fun DrawScope.drawHand(
    angle: Float,
    length: Float,
    strokeWidth: Float,
    color: Color
) {
    val radians = Math
        .toRadians(
            angle.toDouble()
        )
        .toFloat()

    val end = center +
            Offset(
                x = length * cos(radians),
                y = length * sin(radians)
            )

    drawLine(
        color = color,
        start = center,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun rememberCurrentTime(): LocalTime {
    var currentTime by remember {
        mutableStateOf(
            LocalTime
                .now()
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime
                .now()

            delay(1000)
        }
    }

    return currentTime
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun AnalogClockPreview() {
    JetpackClockTheme {
        AnalogClockWithDigitalTime()
    }
}
