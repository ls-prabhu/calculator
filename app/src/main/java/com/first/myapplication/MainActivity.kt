package com.first.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.*
import org.mariuszgromada.math.mxparser.Expression
import android.graphics.Color.*
import android.icu.text.ListFormatter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black

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
fun CalScreen(){
    Box(Modifier
        .background(color = Black)
    ){
        var expression by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
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
                listOf("7","8","9","÷"),
                listOf("6","5","4","X"),
                listOf("3","2","1","-"),
                listOf("C","0","=","+")
            )
            buttons.forEach({row->
                var x =0
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    row.forEach({label->
                        CalButton(label = label){
                            when(label){
                                "C" ->{
                                    expression = ""
                                    result = ""
                                }
                                "=" ->
                                    result = evaluate(expression)
                                else ->
                                    expression+=label
                            }

                        }
                    })


                }
                Row(){
                    Box(
                        modifier = Modifier
                            .height(35.dp)
                            .width(20.dp)
                    ) {
                        // Content here
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))
            })
        }
    }

}

//@Composable
//fun CalButton(label: String, onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        modifier = Modifier
//            .size(80.dp),
//        shape =
//    ) {
//        Text(label, fontSize = 24.sp)
//    }
//}
@Composable
fun CalButton(label : String, onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(label)
    }
}
/*fun evaluate(expression: String): String {
    val operators = mutableListOf<Char>()
    val values = mutableListOf<Int>()

    var tnum = ""

    // Tokenizing numbers and operators
    for (ch in expression) {
        if (ch.isDigit()) {
            tnum += ch
        } else if (ch == '+' || ch == '-' || ch == '÷' || ch == 'X') {
            if (tnum.isNotEmpty()) {
                values.add(tnum.toInt())
                tnum = ""
            }
            operators.add(
                when (ch) {
                    'X' -> '*'
                    '÷' -> '/'
                    else -> ch
                }
            )
        }
    }

    // Add the last number if any
    if (tnum.isNotEmpty()) {
        values.add(tnum.toInt())
    }

    // 🧠 Simple left-to-right evaluation (doesn’t follow operator precedence)
    var result = values[0]
    for (i in 0 until operators.size) {
        val op = operators[i]
        val nextVal = values[i + 1]
        result = when (op) {
            '+' -> result + nextVal
            '-' -> result - nextVal
            '*' -> result * nextVal
            '/' -> result / nextVal
            else -> result
        }
    }

    return result.toString()
}
*/

fun evaluate(expression: String): String {
    return try {
        val fixedExpr = expression.replace("X", "*").replace("÷", "/")
        val result = Expression(fixedExpr).calculate()
        if (result.isNaN()) "Invalid" else result.toString()
    } catch (e: Exception) {
        "Invalid"
    }
}