package com.developer.composeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developer.composeapplication.ui.theme.ComposeApplicationTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            ComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    
                }
            }
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
        }) {

    }

}
