package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.ItemEventBinding

class EventAdapter(
    private var events: MutableList<Event>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder class to bind the data to the view
    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location
            binding.eventDate.text = event.date
            binding.eventTime.text = event.time
            binding.eventImage.setImageResource(event.imageRes)
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
