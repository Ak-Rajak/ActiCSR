package com.example.acticsrapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class User(
    val id: String,
    val email: String
)

class RegistratedUserAdapter (
    private val registeredUsers : List<User>
) : RecyclerView.Adapter<RegistratedUserAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_registered_user , parent
        ,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = registeredUsers[position]
        holder.emailTextView.text = user.email
        holder.userIdTextView.text = user.id
    }

    override fun getItemCount(): Int {
        return registeredUsers.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val userIdTextView: TextView = itemView.findViewById(R.id.userIdTextView)
    }
}