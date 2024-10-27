package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.ItemEventBinding

class EventAdapter(private var events: MutableList<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventImage.setImageResource(event.imageRes)
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location
            binding.eventDate.text = event.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
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
