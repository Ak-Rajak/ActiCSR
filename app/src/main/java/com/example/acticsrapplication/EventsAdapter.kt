package com.example.acticsrapplication // Replace with your app's package name

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Data class for Events
data class Event(val title: String, val location: String, val date: String, val time: String)

// RecyclerView Adapter for Events
class EventsAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.event_title)
        private val locationView: TextView = itemView.findViewById(R.id.event_location)
        private val dateView: TextView = itemView.findViewById(R.id.event_date)
        private val timeView: TextView = itemView.findViewById(R.id.event_time)
        private val imageView: ImageView = itemView.findViewById(R.id.event_image) // New ImageView

        fun bind(event: Event) {
            titleView.text = event.title
            locationView.text = event.location
            dateView.text = event.date
            timeView.text = event.time
            // Set a default image for now, this can be dynamically loaded
            imageView.setImageResource(R.drawable.default_event_image) // Replace with actual image resource or logic
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_home, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}
