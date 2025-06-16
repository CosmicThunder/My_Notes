package com.example.my_notes.screens

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.example.my_notes.Models.Notes
import com.example.my_notes.Navigation.NotesNavigationItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Home_page(navHostController: NavHostController) {

    val db = FirebaseFirestore.getInstance()
    val notesDBRef = db.collection("notes")
    val notesList = remember { mutableStateListOf<Notes>() }
    val dataValue = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        notesDBRef.addSnapshotListener { value, error ->
            if (error == null) {
                val notes = value?.documents?.map { doc ->
                    doc.toObject(Notes::class.java)?.copy(id = doc.id) ?: Notes()
                } ?: emptyList()

                notesList.clear()
                notesList.addAll(notes)
                dataValue.value = true
            } else {
                dataValue.value = false
            }
        }
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            contentColor = Color.White,
            containerColor = Color.Red,
            shape = RoundedCornerShape(corner = CornerSize(100.dp)),
            onClick = {

                navHostController.navigate(NotesNavigationItem.Notes.route + "/defaultId")

            }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp)
            )
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = "Notes",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                if (dataValue.value) {
                    LazyColumn {
                        items(notesList) {
                            Notes_listitem(it,notesDBRef,navHostController)
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize())
                    {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(45.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Notes_listitem(notes: Notes, notesDBRef: CollectionReference, navController: NavHostController) {

    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .padding(12.dp)
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = " ${notes.title}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                DropdownMenu(modifier = Modifier.background(Color.White),
                    properties = PopupProperties(clippingEnabled = true),
                    offset = DpOffset(x = (-40).dp, y = 0.dp),
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }) {
                    DropdownMenuItem(text = { Text(text = "Update", style = TextStyle(color = Color.DarkGray))},
                        onClick = {

                            navController.navigate(NotesNavigationItem.Notes.route + "/${notes.id}")
                            expanded = false

                        })
                    DropdownMenuItem(text = { Text(text = "Delete", style = TextStyle(color = Color.DarkGray))},
                        onClick = {

                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setMessage("Are you sure you want to delete this note?")
                            alertDialog.setPositiveButton("Yes"
                            ) { dialog, which ->
                                notesDBRef.document(notes.id).delete()
                                dialog?.dismiss()
                                expanded = false

                            }

                            alertDialog.setNegativeButton("No"
                            ) { dialog, which -> dialog?.dismiss()
                                expanded = false
                            }

                            alertDialog.show()

                        })
                }

                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(Icons.Default.MoreVert, "Menu")
                }
            }
            Text(
                text = " ${notes.description} ",
                fontWeight = FontWeight.Thin,
                modifier = Modifier.padding(bottom = 8.dp, start = 10.dp)
            )
        }
    }
}
