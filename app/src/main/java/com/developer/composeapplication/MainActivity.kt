package com.developer.composeapplication

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.developer.composeapplication.ui.theme.ComposeApplicationTheme
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            ComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101010))
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
                                .padding(30.dp)
                        ) {
                            var volume by remember {
                                mutableStateOf(0f)
                            }
                            var barCount = 20

                            MusicNob(modifier = Modifier.size(100.dp)) {
                                volume = it
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            VolumeBar(
                                modifier = Modifier.fillMaxWidth().height(30.dp),
                                activeBar = (barCount * volume).roundToInt(),
                                barCount = barCount
                            )
                        }

                    }
                }
            }
        }

    }

    @ExperimentalComposeUiApi
    @Composable
    fun MusicNob(
        modifier: Modifier,
        limitAngle: Float = 25f,
        onValueChange: (Float) -> Unit
    ) {
        var rotation by remember {
            mutableStateOf(limitAngle)
        }
        var touchX by remember {
            mutableStateOf(0f)
        }
        var touchY by remember {
            mutableStateOf(0f)
        }
        var centerX by remember {
            mutableStateOf(0f)
        }
        var centerY by remember {
            mutableStateOf(0f)
        }

        Image(
            painter = painterResource(id = R.drawable.dagger),
            contentDescription = "Music Knob",
            modifier = modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    val windowBounds = it.boundsInWindow()
                    centerX = windowBounds.size.width / 2f
                    centerY = windowBounds.size.height / 2f
                }
                .pointerInteropFilter { motionEvent ->
                    touchX = motionEvent.x
                    touchY = motionEvent.y
                    val angle = -atan2(centerX - touchX, centerY - touchY) * (180f / PI).toFloat()
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            if (angle !in -limitAngle..limitAngle) {
                                val fixedAngle = if (angle in -180f..-limitAngle) {
                                    360f + angle
                                } else {
                                    angle
                                }
                                rotation = fixedAngle

                                val percentage = (fixedAngle - limitAngle) / (360f - 2 * limitAngle)
                                onValueChange(percentage)
                                true
                            } else false
                        }
                        else -> false
                    }
                }
                .rotate(rotation)
        )
    }

    @Composable
    fun VolumeBar(
        modifier: Modifier,
        activeBar: Int = 0,
        barCount: Int = 10
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            val barWidth = remember {
                constraints.maxWidth / (2f * barCount)
            }
            Canvas(modifier = modifier) {
                for (i in 0 until barCount) {
                    drawRoundRect(
                        color = if (i in 0..activeBar) Color.Green else Color.Gray,
                        topLeft = Offset(i * barWidth * 2f + barWidth / 2f, 0f),
                        size = Size(barWidth, constraints.minHeight.toFloat()),
                        cornerRadius = CornerRadius(0f)
                    )
                }
            }
        }
    }

    @Composable
    fun CircularProgressBar(
        percentage: Float,
        number: Int,
        fontSize: TextUnit = 28.sp,
        radius: Dp = 50.dp,
        color: Color = Color.Green,
        strokeWidth: Dp = 8.dp,
        animDuration: Int = 1000,
        animDelay: Int = 0
    ) {
        var animationPlayed by remember {
            mutableStateOf(false)
        }

        var currentPercentage = animateFloatAsState(
            targetValue = if (animationPlayed) percentage else 0f,
            animationSpec = tween(
                durationMillis = animDuration,
                delayMillis = animDelay
            )
        )
        LaunchedEffect(key1 = true) {
            animationPlayed = true
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(radius * 2f)
        ) {
            Canvas(modifier = Modifier.size(radius * 2f)) {
                drawArc(
                    color = color,
                    -90f,
                    360 * currentPercentage.value,
                    useCenter = false,
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = (currentPercentage.value * number).toInt().toString(),
                color = Color.Black,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    private fun callCircularBar() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressBar(
                percentage = 0.5f,
                number = 34,
                animDuration = 3000,
                animDelay = 1000
            )
        }
    }

    @Composable
    private fun CallImageCard4() {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(16.dp)
        ) {
            ImageCard(
                painter = painterResource(id = R.drawable.dagger),
                contentDescription = "Dagger Image",
                title = "Dagger"
            )
        }


    }

    @Composable
    private fun CallStyle5() {
        val fontFamily = FontFamily(
            Font(R.font.lexend_thin, FontWeight.Thin),
            Font(R.font.lexend_light, FontWeight.Light),
            Font(R.font.lexend_black, FontWeight.Black),
            Font(R.font.lexend_bold, FontWeight.Bold),
            Font(R.font.lexend_extrabold, FontWeight.ExtraBold),
            Font(R.font.lexend_extralight, FontWeight.ExtraLight),
            Font(R.font.lexend_medium, FontWeight.Medium),
            Font(R.font.lexend_regular, FontWeight.Normal),
            Font(R.font.lexend_semibold, FontWeight.SemiBold)
        )

        StyleText(fontFamily)
    }

    @Composable
    private fun CallState6() {
        Column(modifier = Modifier.fillMaxSize()) {
            val color = remember {
                mutableStateOf(Color.Yellow)
            }
            ColorBox(
                Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                color.value = it
            }
            Box(
                modifier = Modifier
                    .background(color = color.value)
                    .weight(1f)
                    .fillMaxSize()
            )
        }
    }

    @Composable
    private fun CallSnackBar7() {
        val scaffoldState = rememberScaffoldState()
        var textValue by remember {
            mutableStateOf("")
        }
        val scope = rememberCoroutineScope()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                TextField(value = textValue, label = {
                    Text("Enter your name")
                }, onValueChange = { newString ->
                    textValue = newString
                },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Hello $textValue!")
                    }
                }) {
                    Text(text = "Greetings..")
                }
            }
        }
    }

    @Composable
    private fun CallSideEffect9() {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(scaffoldState = scaffoldState) {
            var counter by remember {
                mutableStateOf(0)
            }
            if (counter % 2 == 0 && counter > 0) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Hello bharath")
                }
            }
            Button(onClick = { counter++ }) {
                Text(text = "Click me: $counter")
            }
        }
    }

    @Composable
    private fun CallConstrainLayout8() {
        val constraintsScope = ConstraintSet {
            val greenBox = createRefFor("greenBox")
            val redBox = createRefFor("redBox")
            val guideline = createGuidelineFromTop(0.5f)

            constrain(greenBox) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                width = Dimension.value(100.dp)
                height = Dimension.value(100.dp)
            }

            constrain(redBox) {
                top.linkTo(parent.top)
                start.linkTo(greenBox.end)
                end.linkTo(parent.end)
                width = Dimension.value(100.dp)
                height = Dimension.value(100.dp)
            }
            createHorizontalChain(greenBox, redBox, chainStyle = ChainStyle.Packed)
        }

        ConstraintLayout(constraintsScope, modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(Color.Green)
                    .layoutId("greenBox")
            )
            Box(
                modifier = Modifier
                    .background(Color.Red)
                    .layoutId("redBox")
            )
        }
    }
}

var i = 0

@Composable
fun CallSideEffect(backPressedDispatcher: OnBackPressedDispatcher) {
    SideEffect {
        i++
    }
    val callBack = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Do Something
            }
        }
    }
    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher.addCallback(callBack)
        onDispose {
            callBack.remove()
        }
    }

    Button(onClick = { /*TODO*/ }) {
        Text(text = "Click Me")
    }

}

@Composable
fun Greeting(name: String) {
    Column(
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth()
            .fillMaxHeight()
            .border(5.dp, color = Color.Green)
            .padding(top = 5.dp)
    ) {
        Text(text = "Hello $name!",
            modifier = Modifier
                .padding(16.dp)
                .clickable { }
        )
        Text(text = "Welcome")

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeApplicationTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(16.dp)
        ) {
            ImageCard(
                painter = painterResource(id = R.drawable.dagger),
                contentDescription = "Dagger Image",
                title = "Dagger"
            )
        }

    }
}


@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            ) {

            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = title, style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }

}

@Composable
fun StyleText(aFontFamily: FontFamily) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
    ) {
        /*Text(
            text = "Text Style",
            color = Color.White,
            fontSize = 34.sp,
            fontFamily = aFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.LineThrough
        )*/
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Green,
                        fontSize = 50.sp,
                        fontStyle = FontStyle.Italic
                    )
                ) {
                    append("J")
                }
                append("etpack ")
                withStyle(
                    style = SpanStyle(
                        color = Color.Green,
                        fontSize = 50.sp,
                        fontStyle = FontStyle.Italic
                    )
                ) {
                    append("C")
                }
                append("ompose")

            },
            color = Color.White,
            fontSize = 34.sp,
            fontFamily = aFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun ColorBox(
    modifier: Modifier = Modifier,
    updateColor: (Color) -> Unit
) {

    val color = remember {
        mutableStateOf(Color.Yellow)
    }

    Box(modifier = modifier
        .background(color = color.value)
        .clickable {
            updateColor(
                Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f
                )
            )
        })
}
