package com.example.my_notes.Navigation

sealed class NotesNavigationItem(val route: String) {

    object Start : NotesNavigationItem("start")
    object Home : NotesNavigationItem("home")
    object Notes : NotesNavigationItem("insert")

}