package com.example.acticsrapplication
import com.google.firebase.Timestamp

data class Event(
    val id: String,
    val title: String,
    val location: String,
    val date: Timestamp,
    val time: String
)

