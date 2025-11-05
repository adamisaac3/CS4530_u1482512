package com.example.marblegame.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.marblegame.viewmodel.MarbleViewModel


@Composable
fun MarbleScreen(viewModel: MarbleViewModel){
    val marbleState by viewModel.marbleState.collectAsState()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))
    ){
        val maxWidthDp = maxWidth
        val maxHeightDp = maxHeight

        with(density){
            viewModel.updateScreenBounds(
                maxWidthDp.toPx(),
                maxHeightDp.toPx()
            )
        }

        Box(
            modifier = Modifier
                .offset(marbleState.x.dp, marbleState.y.dp)
                .size(46.dp)
                .background(Color(0xFF2196F3), CircleShape)
        )
    }
}