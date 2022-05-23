package com.example.jetareader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetareader.navigation.ReaderNavigation
import com.example.jetareader.ui.theme.JetAReaderTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetAReaderTheme {

                ReaderApp()
                // A surface container using the 'background' color from the theme

            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ReaderApp(){
    Surface(color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()) {

        Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

            ReaderNavigation()

        }

    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetAReaderTheme {

    }
}