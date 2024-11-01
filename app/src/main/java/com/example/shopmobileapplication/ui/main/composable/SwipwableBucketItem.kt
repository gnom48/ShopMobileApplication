package com.example.shopmobileapplication.ui.main.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.SurfaceScope
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayOnButton
import com.example.shopmobileapplication.ui.theme.red
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun SwipeableItemWithActions(
    leftAction: @Composable RowScope.() -> Unit,
    rightAction: @Composable RowScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    var leftActionWidth by remember {
        mutableFloatStateOf(300f)
    }
    var rightActionWidth by remember {
        mutableFloatStateOf(300f)
    }
    val offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(IntrinsicSize.Min)
            .background(whiteGreyBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            leftAction()
            rightAction()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(leftActionWidth, rightActionWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(-rightActionWidth, leftActionWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= leftActionWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(leftActionWidth)
                                    }
                                }

                                offset.value <= -rightActionWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(-rightActionWidth)
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}

@Composable
@Preview
fun ProductCountClickerPreview() {
    ProductCountClicker(5, {})
}

@Composable
fun ProductCountClicker(count: Int, onCountChange: (change: Int) -> Unit) {
    var countState by remember { mutableStateOf(count) }
    Card(
        modifier = Modifier
            .height(130.dp)
            .width(75.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(blueGradientStart)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                countState++
                onCountChange(countState)
            }) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plus",
                    tint = Color.White
                )
            }

            Text(text = countState.toString(), style = ralewayOnButton)

            IconButton(onClick = {
                countState--
                onCountChange(countState)
            }) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(id = R.drawable.minus),
                    contentDescription = "Minus",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
@Preview
fun ProductDeleteClickerPreview() {
    ProductDeleteClicker { }
}

@Composable
fun ProductDeleteClicker(onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .width(75.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(red)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                onDelete()
            }) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Plus",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
@Preview
fun SwipeableItemWithActionsPreview() {
    SwipeableItemRowWithActions({
         ProductCountClicker(count = 0, onCountChange = {})
    },{
        ProductDeleteClicker({})
    },{ })
}

@Composable
fun SwipeableItemRowWithActions(
    leftAction: @Composable RowScope.() -> Unit,
    rightAction: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit
) {
    throw Exception("Не получилось")
    var leftActionWidth by remember {
        mutableFloatStateOf(100f)
    }
    var rightActionWidth by remember {
        mutableFloatStateOf(100f)
    }
    var currentWidth by remember { mutableStateOf(0f) }
    val initOffset = 100f

    val offset = remember {
        Animatable(initialValue = initOffset)
    }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxWidth().background(whiteGreyBackground).padding(vertical = 10.dp)
            .onSizeChanged { size ->
                currentWidth = size.width.absoluteValue.toFloat()
            }
    ) {
        Row(modifier = Modifier.wrapContentWidth()) {
            leftAction()

            Surface(
                modifier = Modifier
                    .width(currentWidth.dp)
                    .offset { IntOffset(offset.value.roundToInt(), 0) }
                    .pointerInput(leftActionWidth, rightActionWidth) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, dragAmount ->
                                scope.launch {
                                    val newOffset = (offset.value + initOffset + dragAmount).coerceIn(-rightActionWidth, leftActionWidth)
                                    offset.snapTo(newOffset)
                                }
                            },
                            onDragEnd = {
                                when {
                                    offset.value >= leftActionWidth / 2f -> {
                                        scope.launch {
                                            offset.animateTo(initOffset+leftActionWidth)
                                        }
                                    }

                                    offset.value <= -rightActionWidth / 2f -> {
                                        scope.launch {
                                            offset.animateTo(initOffset-rightActionWidth)
                                        }
                                    }

                                    else -> {
                                        scope.launch {
                                            offset.animateTo(initOffset)
                                        }
                                    }
                                }
                            }
                        )
                    }
            ) {
                content()
            }

            rightAction()
        }
    }
}