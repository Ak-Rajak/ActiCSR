package com.example.acticsrapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var profilePhoto: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Get references to UI elements
        profilePhoto = view.findViewById(R.id.profile_photo)
        userName = view.findViewById(R.id.user_Name)
        userEmail = view.findViewById(R.id.user_Email)

        // Load user data
        loadUserData()

        return view
    }

    private fun loadUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            userName.text = it.displayName ?: "User Name"
            userEmail.text = it.email ?: "user_email@example.com"
            // Here you can load the profile image if you have a URL
            // For example, using Glide or Picasso
            // Glide.with(this).load(it.photoUrl).into(profilePhoto)
        }
    }
}
