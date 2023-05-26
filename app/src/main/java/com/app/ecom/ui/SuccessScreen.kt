package com.app.ecom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SuccessScreen() {
    Column(modifier = Modifier
        .fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Text(text = "HOLA1")
        }
        Card(
            elevation = 18.dp,
            shape = RoundedCornerShape(topStart = 1050.dp, topEnd = 1050.dp ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Icon(
                painter = painterResource(id = com.app.ecom.R.drawable.check),
                contentDescription = "")
        }
    }
}