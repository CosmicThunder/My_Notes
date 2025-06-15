package com.example.my_notes.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.TextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.my_notes.Models.Notes


@Composable
fun note_detail(navHostController: NavHostController, id: String?) {

    val context = LocalContext.current

    val db = FirebaseFirestore.getInstance()
    val notesDBRef = db.collection("notes")

    val title = remember {
        mutableStateOf(" ")
    }
    val description = remember {
        mutableStateOf(" ")
    }


    LaunchedEffect(Unit) {
        if (id != "defaultId") {
            notesDBRef.document(id.toString()).get().addOnSuccessListener {
                val singleData = it.toObject(Notes::class.java)
                title.value = singleData!!.title
                description.value = singleData.description
            }
        }
    }


    Scaffold(floatingActionButton = {
        FloatingActionButton(contentColor = Color.White,
            containerColor = Color.Red,
            shape = RoundedCornerShape(corner = CornerSize(100.dp)),
            onClick = {

                if (title.value.isEmpty() && description.value.isEmpty()) {
                    Toast.makeText(context, "Enter data", Toast.LENGTH_SHORT).show()
                } else {

                    val myNotesID = if (id != "defaultId") {
                        id.toString()
                    } else {
                        notesDBRef.document().id
                    }

                    val notes = Notes(
                        id = myNotesID,
                        title = title.value,
                        description = description.value
                    )

                    notesDBRef.document(myNotesID).set(notes).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Notes inserted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            navHostController.popBackStack()

                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }) {
            Icon(imageVector = Icons.Default.Check,
                contentDescription = " ",
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
                    text = "Insert Notes",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                    shape = RoundedCornerShape(15.dp),
                    label = {
                        Text(
                            text = "Enter your Title",
                            style = TextStyle(fontSize = 20.sp, color = Color.White)
                        )
                    }, value = title.value, onValueChange = {
                        title.value = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(1.dp))
                TextField(textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                    shape = RoundedCornerShape(15.dp),
                    label = {
                        Text(
                            text = "Enter the description",
                            style = TextStyle(fontSize = 20.sp, color = Color.White)
                        )
                    }, value = description.value, onValueChange = {
                        description.value = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(8.dp)
                )
            }
        }
    }
}