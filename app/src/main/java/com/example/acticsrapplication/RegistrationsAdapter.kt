package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RegistrationAdapter(private val registrations: List<Registration>) :
    RecyclerView.Adapter<RegistrationAdapter.RegistrationViewHolder>() {

    class RegistrationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.registrationName)
        val emailTextView: TextView = view.findViewById(R.id.registrationEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_registration, parent, false)
        return RegistrationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegistrationViewHolder, position: Int) {
        val registration = registrations[position]
        holder.nameTextView.text = registration.name
        holder.emailTextView.text = registration.email
    }

    override fun getItemCount(): Int = registrations.size
}
