package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acticsrapplication.databinding.ItemAdminEventBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class AdminEventAdapter(
    private var events: MutableList<Event>,
    private val onItemClickListener: (Event) -> Unit, // Handles item click to view details
    private val onUpdateClickListener: (Event) -> Unit, // Handles update action
    private val onDeleteClickListener: (Event) -> Unit // Handles delete action
) : RecyclerView.Adapter<AdminEventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemAdminEventBinding) : RecyclerView.ViewHolder(binding.root) {

        // Binds event data to the views in the layout
        fun bind(event: Event) {
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location

            // Format the date using Firebase Timestamp
            binding.eventDate.text = formatDate(event.date)
            binding.eventTime.text = event.time

            // Set up click listeners for actions
            itemView.setOnClickListener { onItemClickListener(event) }
            binding.updateButton.setOnClickListener { onUpdateClickListener(event) }
            binding.deleteButton.setOnClickListener { onDeleteClickListener(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemAdminEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    // Method to update the events list and refresh RecyclerView
    fun updateEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    // Formats the Firebase Timestamp into a readable date format
    private fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}
