package com.nextxform.simpleapicall

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.Either
import arrow.core.left
import com.nextxform.simpleapicall.api.HTTPCallback
import com.nextxform.simpleapicall.api.HttpRequest
import com.nextxform.simpleapicall.api.ResponseModel
import com.nextxform.simpleapicall.ui.theme.SimpleApiCallTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleApiCallTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            HttpRequest(
                "https://pokeapi.co/api/v2/pokemon/ditto",
                HashMap(),
                object : HTTPCallback {
                    override fun callResponse(response: Either<ResponseModel, ResponseModel>) {
                        response.onLeft {
                            Log.d(
                                "RESPONSE",
                                "Response Failed: ${it.responseCode} ${it.responseBody}"
                            )
                        }

                        response.onRight {
                            Log.d(
                                "RESPONSE",
                                "Response Success: ${it.responseCode} ${it.responseBody}"
                            )
                        }
                    }
                }
            ).performCall()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleApiCallTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MaterialCardPreview() {
    Row(modifier = Modifier.padding(20.dp)) {

        Card(
            modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.black),
                contentColor = Color.White
            )
        ) {
            Text(text = "hello there", modifier = Modifier.padding(10.dp))
        }
    }
}