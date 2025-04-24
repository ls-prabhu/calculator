package com.first.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.focusable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mariuszgromada.math.mxparser.Expression

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalScreen()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CalScreen() {
    val focusRequester = remember { FocusRequester() }
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    val keyCode = event.nativeKeyEvent?.keyCode
                    val char = event.nativeKeyEvent?.displayLabel ?: return@onKeyEvent false
                    val numberKeys = listOf(
                            Key.Zero, Key.One, Key.Two, Key.Three, Key.Four,
                            Key.Five, Key.Six, Key.Seven, Key.Eight, Key.Nine,
                            Key.NumPad0, Key.NumPad1, Key.NumPad2, Key.NumPad3, Key.NumPad4,
                            Key.NumPad5, Key.NumPad6, Key.NumPad7, Key.NumPad8, Key.NumPad9
                        )
                    when (event.key) {
                        Key.Enter, Key.NumPadEnter -> {
                            result = evaluate(expression)
                            true
                        }
                        Key.Backspace -> {
                            if (expression.isNotEmpty()) {
                                expression = expression.dropLast(1)
                            }
                            true
                        }

                        Key.Plus, Key.NumPadAdd -> {
                            expression += "+"
                            true
                        }
                        Key.Minus, Key.NumPadSubtract -> {
                            expression += "-"
                            true
                        }
                        Key.Slash, Key.NumPadDivide -> {
                            expression += "÷"
                            true
                        }
                        Key.X, Key.NumPadMultiply -> {
                            expression += "X"
                            true
                        }
                        else -> false
                    }

                    when {

                        char in '0'..'9' -> {
                            expression += char
                            true
                        }

                        char == '+' || char == '-' -> {
                            expression += char
                            true
                        }

                        char == '/' -> {
                            expression += "÷"
                            true
                        }

                        char == '*' || char == 'x' || char == 'X' -> {
                            expression += "X"
                            true
                        }

                        char == '\n' -> { // Enter key
                            result = evaluate(expression)
                            true
                        }

                        keyCode == android.view.KeyEvent.KEYCODE_DEL -> { // Backspace
                            if (expression.isNotEmpty()) {
                                expression = expression.dropLast(1)
                            }
                            true
                        }

                        else -> false
                    }
                } else false
            }
            .focusable()
            .background(color = Color.Black)
    ) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = expression,
                fontSize = 32.sp,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.End
            )
            Text(
                text = result,
                fontSize = 32.sp,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(166.dp))

            val buttons = listOf(
                listOf("7", "8", "9", "÷"),
                listOf("6", "5", "4", "X"),
                listOf("3", "2", "1", "-"),
                listOf("C", "0", "=", "+")
            )

            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { label ->
                        CalButton(label = label) {
                            when (label) {
                                "C" -> {
                                    expression = ""
                                    result = ""
                                }

                                "=" -> result = evaluate(expression)

                                else -> expression += label
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CalButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.size(75.dp)
    ) {
        Text(label)
    }
}

fun evaluate(expression: String): String {
    return try {
        val fixedExpr = expression.replace("X", "*").replace("÷", "/")
        if (!fixedExpr.matches(Regex("[0-9+\\-*/.()]*"))) {
            return "Invalid"
        }
        val result = Expression(fixedExpr).calculate()
        if (result.isNaN()) "Invalid" else result.toString()
    } catch (e: Exception) {
        "Invalid"
    }
}
