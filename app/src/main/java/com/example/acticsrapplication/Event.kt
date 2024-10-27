package com.example.acticsrapplication

data class Event(
    val title: String,
    val location: String,
    val date: String,
    val imageRes: Int // Change to Int
)

data class Events(
    val title: String,
    val location: String,
    val date: String,
    val time: String,
)
