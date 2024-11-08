package com.hackwithsodiq.gsearch.ui.lib

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hackwithsodiq.gsearch.R
import com.hackwithsodiq.gsearch.ui.theme.Gray
import com.hackwithsodiq.gsearch.ui.theme.Gray2
import com.hackwithsodiq.gsearch.ui.theme.Gray3

@Composable
fun ProgressButton(
    modifier: Modifier = Modifier,
    text:String,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Black,
        contentColor = Color.White,
        disabledContainerColor = Gray,
        disabledContentColor = Gray3
    ),
    isLoading:Boolean = false,
    isEnabled: Boolean = true,
    onClicked:() -> Unit){

    Button(onClick = { onClicked() }, modifier = modifier
        .height(35.dp),
        enabled = !isLoading && isEnabled,
        colors = colors,
        shape = RoundedCornerShape(3.dp)
    ) {
        if (isLoading){
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 5.dp).size(20.dp), strokeWidth = 2.dp)
        }else {
            Text(text)
        }
    }
}

@Composable
fun EmptyState(message: String, painter: Painter = painterResource(R.drawable.ic_empty_state)){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter,
            contentDescription = "search_github"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(message, color = Gray2, textAlign = TextAlign.Center)
    }
}