package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RegistrationsAdapter(private val registrations: List<Registration>) :
    RecyclerView.Adapter<RegistrationsAdapter.RegistrationViewHolder>() {

    class RegistrationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.textViewStudentName)
        val studentId: TextView = itemView.findViewById(R.id.textViewStudentId)
        val eventTitle: TextView = itemView.findViewById(R.id.textViewEventTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_registration, parent, false)
        return RegistrationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegistrationViewHolder, position: Int) {
        val registration = registrations[position]
        holder.studentName.text = registration.studentName
        holder.studentId.text = registration.studentId
        holder.eventTitle.text = registration.eventTitle
    }

    override fun getItemCount(): Int = registrations.size
}
