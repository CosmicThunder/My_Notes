package com.example.my_notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.my_notes.Navigation.NotesNav
import com.google.firebase.FirebaseApp
import android.util.Log

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NotesNav(navController)
            FirebaseApp.initializeApp(this)
            Log.d(TAG, "onCreate: Activity started")
        }
    }
}
