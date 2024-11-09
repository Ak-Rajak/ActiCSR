package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.ItemEventBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdapter(
    private var events: MutableList<Event>,
    private val itemClickListener: (Event) -> Unit // Passing the full Event object
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location

            // Format the date (using the correct Firebase Timestamp)
            val formattedDate = formatDate(event.date)
            binding.eventDate.text = formattedDate

            binding.eventTime.text = event.time

            itemView.setOnClickListener {
                itemClickListener(event) // Pass the full event object to the click listener
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

    // Method to update the events list
    fun updateEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    // Helper function to format the Firebase Timestamp into a string
    private fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate() // Convert Firebase Timestamp to Date
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Define your date format
        return formatter.format(date) // Return the formatted date as string
    }
}
