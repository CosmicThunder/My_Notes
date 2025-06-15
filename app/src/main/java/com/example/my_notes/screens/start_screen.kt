package com.example.my_notes.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.my_notes.Navigation.NotesNavigationItem
import com.example.my_notes.R
import kotlinx.coroutines.delay

@Composable
fun start_screen(navHostController: NavHostController) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_book_24),
                contentDescription = "logo",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
            )
        }

        LaunchedEffect(Unit) {
            delay(2500)
            navHostController.navigate(NotesNavigationItem.Home.route) {
                popUpTo(NotesNavigationItem.Start.route) {
                    inclusive = true
                }
            }
        }
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    start_screen(navHostController = navController)
}