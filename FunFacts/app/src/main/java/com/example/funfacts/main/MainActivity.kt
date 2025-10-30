package com.example.funfacts.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.funfacts.ui.main.FunFactsScreen
import com.example.funfacts.ui.theme.FunFactsTheme
import com.example.funfacts.viewmodel.FunFactViewModel
import com.example.funfacts.viewmodel.FunFactViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FunFactsTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: FunFactViewModel = viewModel(
                        factory = FunFactViewModelProvider.Factory
                    )
                    FunFactsScreen(viewModel)
                }
            }
        }
    }
}