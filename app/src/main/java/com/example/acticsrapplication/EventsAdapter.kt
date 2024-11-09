package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import com.google.firebase.Timestamp
import java.util.*

// This Adapter file is used in fragment_home.xml for showing events, like Upcoming and Interested Events
// RecyclerView Adapter for Events
class EventsAdapter(
    private var events: MutableList<Event>,
    private val onEventClick: (Event) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    // ViewHolder class to hold references to views
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.event_title)
        val location: TextView = itemView.findViewById(R.id.event_location)
        val date: TextView = itemView.findViewById(R.id.event_date)
        val time: TextView = itemView.findViewById(R.id.event_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_home, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.title.text = event.title
        holder.location.text = event.location

        // Format the date (if it's a Timestamp)
        holder.date.text = formatDate(event.date)

        holder.time.text = event.time

        holder.itemView.setOnClickListener {
            onEventClick(event)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    // Method to update the events list and notify the adapter
    fun updateEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    // Helper function to format the Timestamp into a string
    private fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate() // Convert Timestamp to Date
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Define your date format
        return formatter.format(date) // Return the formatted date as string
    }
}
