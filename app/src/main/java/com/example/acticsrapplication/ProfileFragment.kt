package com.example.acticsrapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userName: TextView
    private lateinit var branchName: TextView
    private lateinit var userMobile: TextView
    private lateinit var registrationNo: TextView
    private lateinit var yearOfStudy: TextView
    private lateinit var userEmail: TextView
    private lateinit var updateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get references to UI elements
        userName = view.findViewById(R.id.full_name)
        branchName = view.findViewById(R.id.branch_name)
        userMobile = view.findViewById(R.id.user_mobile)
        registrationNo = view.findViewById(R.id.registration_no)
        yearOfStudy = view.findViewById(R.id.year_of_study)
        userEmail = view.findViewById(R.id.user_email)
        updateButton = view.findViewById(R.id.update_profile_button)

        // Load user data
        loadUserData()

        // Set click listener for the update button
        updateButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, UpdateProfileFragment()) // Use your container's ID
            transaction.addToBackStack(null) // Add to back stack to allow for back navigation
            transaction.commit()
        }

        return view
    }

    private fun loadUserData() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val userId = user.uid
            Log.d("ProfileFragment", "Fetching data for user: $userId")
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        Log.d("ProfileFragment", "Document data: ${document.data}")
                        displayUserData(document)
                    } else {
                        Log.w("ProfileFragment", "No such document")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileFragment", "Error fetching document", e)
                }
        } else {
            Log.e("ProfileFragment", "User not authenticated")
        }
    }


    private fun displayUserData(document: DocumentSnapshot) {
        userName.text = document.getString("username") ?: "No Name"
        Log.d("ProfileFragment", "Set userName: ${userName.text}")

        branchName.text = document.getString("branchName") ?: "No Branch"
        Log.d("ProfileFragment", "Set branchName: ${branchName.text}")

        userMobile.text = document.getString("mobile") ?: "No Mobile"
        registrationNo.text = document.getString("registrationNo") ?: "No Registration No"
        yearOfStudy.text = document.getString("yearOfStudy") ?: "No Year"
        userEmail.text = document.getString("email") ?: "No Email"
    }

}
