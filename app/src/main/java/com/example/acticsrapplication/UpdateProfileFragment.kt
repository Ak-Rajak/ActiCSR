package com.example.acticsrapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var editTextName: EditText
    private lateinit var editTextBranchName: EditText
    private lateinit var editTextMobile: EditText
    private lateinit var editTextRegistrationNo: EditText
    private lateinit var editTextYearOfStudy: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_profile, container, false)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Reference to UI elements
        editTextName = view.findViewById(R.id.editTextName)
        editTextBranchName = view.findViewById(R.id.editTextBranchName)
        editTextMobile = view.findViewById(R.id.editTextMobile)
        editTextRegistrationNo = view.findViewById(R.id.editTextRegistrationNo)
        editTextYearOfStudy = view.findViewById(R.id.editTextYearOfStudy)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        buttonSave = view.findViewById(R.id.buttonSave)

        // Load current user data
        loadUserData()

        // Set click listener for save button
        buttonSave.setOnClickListener {
            saveUserData()
        }

        return view
    }

    private fun loadUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        editTextName.setText(document.getString("username"))
                        editTextBranchName.setText(document.getString("branchName"))
                        editTextMobile.setText(document.getString("mobile"))
                        editTextRegistrationNo.setText(document.getString("registrationNo"))
                        editTextYearOfStudy.setText(document.getString("yearOfStudy"))
                        editTextEmail.setText(document.getString("email"))
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error fetching user data", e)
                }
        }
    }

    private fun saveUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid

            // Prepare user data map
            val userData = hashMapOf(
                "username" to editTextName.text.toString(),
                "branchName" to editTextBranchName.text.toString(),
                "mobile" to editTextMobile.text.toString(),
                "registrationNo" to editTextRegistrationNo.text.toString(),
                "yearOfStudy" to editTextYearOfStudy.text.toString(),
                "email" to editTextEmail.text.toString()
            )

            // Update data in Firestore
            db.collection("users").document(userId).set(userData)
                .addOnSuccessListener {
                    // Data saved successfully, navigate back to ProfileFragment
                    requireActivity().supportFragmentManager.popBackStack()
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error saving user data", e)
                }
        }
    }
}
