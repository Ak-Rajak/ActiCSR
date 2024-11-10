package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.ItemAdminEventBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

// This Adapter file is used in fragment_admin_event.xml for showing events, like Upcoming and Interested Events
class AdminEventAdapter(
    private var events: MutableList<Event>,
    private val itemClickListener: (Event) -> Unit, // For handling click on event
    private val updateListener: (Event) -> Unit, // For handling the update action
    private val deleteListener: (Event) -> Unit // For handling the delete action
) : RecyclerView.Adapter<AdminEventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemAdminEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            // Binding data to views
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location

            // Format the date (using the correct Firebase Timestamp)
            val formattedDate = formatDate(event.date)
            binding.eventDate.text = formattedDate

            binding.eventTime.text = event.time

            // Handling item click to show event details
            itemView.setOnClickListener {
                itemClickListener(event)
            }

            // Handling update button click
            binding.updateButton.setOnClickListener {
                updateListener(event) // Trigger update action
            }

            // Handling delete button click
            binding.deleteButton.setOnClickListener {
                deleteListener(event) // Trigger delete action
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        // Inflate the correct layout using ItemAdminEventBinding
        val binding = ItemAdminEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
