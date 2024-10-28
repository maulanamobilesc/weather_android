package com.maulana.weathermobile.core.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SwipeToDeleteItem(
    height: Dp,
    onClickDelete: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateDpAsState(targetValue = offsetX.dp, label = "")

    // Trigger when delete icon is clicked
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        // Background showing Delete Icon
        Box(
            modifier = Modifier
                .fillMaxSize().clip(RoundedCornerShape(16.dp))
                .background(Color.Red)
                .padding(vertical = 16.dp)
                .clickable {
                    // Animate swipe back to original position
                    scope.launch {
                        offsetX = 0F
                        // Trigger delete after animation completes
                        onClickDelete()
                    }
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 32.dp)
            )
        }

        // Foreground content that can be swiped
        Box(
            modifier = Modifier
                .offset(x = animatedOffsetX)
                .fillMaxWidth()
                .swipeableToDelete { offsetX = it }
        ) {
            content()
        }
    }
}

fun Modifier.swipeableToDelete(onSwipe: (Float) -> Unit): Modifier = composed {
    var offsetX by remember { mutableFloatStateOf(0f) }
    pointerInput(Unit) {
        detectHorizontalDragGestures(
            onHorizontalDrag = { _, dragAmount ->
                // Accumulate drag offset
                offsetX += dragAmount
                // Prevent swiping past the left edge
                if (offsetX < 0) {
                    onSwipe(offsetX)
                }
            },
            onDragEnd = {
                // If swiped far enough, snap to the delete state (for example, at -100f)
                if (offsetX < -100f) {
                    onSwipe(-100f)
                } else {
                    onSwipe(0f) // Reset to original position if not swiped enough
                }
            }
        )
    }
}

