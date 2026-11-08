package com.upchiapas.kt_template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upchiapas.kt_template.counter.CounterPage
import com.upchiapas.kt_template.counterVM.presentation.CounterVMPage
import com.upchiapas.kt_template.helloworld.view.HelloWorldPage
import com.upchiapas.kt_template.ui.theme.Kt_templateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kt_templateTheme {
                CounterVMPage()
            }
        }
    }
}
