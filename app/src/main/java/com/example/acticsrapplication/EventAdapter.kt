package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.ItemEventBinding

// This Adapter file is used in fragment_event.kt for showing events, like Completed, Upcoming Events
class EventAdapter(
    private var events: MutableList<Event>,
    private val itemClickListener: (Event) -> Unit // Change this to pass the full Event object
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder class to bind the data to the view
    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location
            binding.eventDate.text = event.date
            binding.eventTime.text = event.time

            // Set up the click listener for each event item
            itemView.setOnClickListener {
                itemClickListener(event) // Pass the full event object
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    // Update events method to switch between event types based on tabs
    fun updateEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }
}
