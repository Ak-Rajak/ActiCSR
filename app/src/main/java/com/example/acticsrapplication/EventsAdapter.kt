package com.example.acticsrapplication // Replace with your app's package name

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// RecyclerView Adapter for Events
class EventsAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

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
        holder.date.text = event.date
        holder.time.text = event.time
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
