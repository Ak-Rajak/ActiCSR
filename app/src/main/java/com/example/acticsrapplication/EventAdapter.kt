package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.ItemEventBinding

class EventAdapter(private var events: MutableList<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(events: Event) {
            binding.eventImage.setImageResource(events.imageRes)
            binding.eventTitle.text = events.title
            binding.eventLocation.text = events.location
            binding.eventDate.text = events.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val events = events[position]
        holder.bind(events)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    // Method to update events
    fun updateEvents(newEvents: List<Event>) {
        events.clear()  // Clear the old list
        events.addAll(newEvents)  // Add the new events
        notifyDataSetChanged()  // Notify the adapter to refresh the view
    }
}
