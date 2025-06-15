package com.example.my_notes.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.my_notes.screens.Home_page
import com.example.my_notes.screens.note_detail
import com.example.my_notes.screens.start_screen

@Composable
fun NotesNav(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "start")
    {
        composable(NotesNavigationItem.Start.route)
        {
            start_screen(navHostController)
        }

        composable(NotesNavigationItem.Home.route) {
            Home_page(navHostController)
        }

        composable(NotesNavigationItem.Notes.route + "/{id}") {
            val id = it.arguments?.getString("id")
            note_detail(navHostController, id)
        }
    }
}