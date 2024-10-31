package com.example.acticsrapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth
        currentUser = auth.currentUser ?: return view

        // Set up click listeners for each option
        view.findViewById<LinearLayout>(R.id.update_profile_option).setOnClickListener {
            onUpdateProfileClick()
        }

        view.findViewById<LinearLayout>(R.id.change_password_option).setOnClickListener {
            showChangePasswordConfirmationDialog()
        }

        // Removed delete profile option as per your request

        return view
    }

    private fun onUpdateProfileClick() {
        val updateProfileFragment = UpdateProfileFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, updateProfileFragment) // Replace with your actual container ID
            .addToBackStack(null)
            .commit()
    }

    private fun showChangePasswordConfirmationDialog() {
        // Inflate the custom dialog layout
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        // Create the AlertDialog builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView) // Set the custom layout

        // Find the buttons in the dialog view
        val buttonYes = dialogView.findViewById<Button>(R.id.button_yes)
        val buttonNo = dialogView.findViewById<Button>(R.id.button_no)

        // Create the dialog instance
        val dialog = builder.create()

        // Set up the Yes button click listener
        buttonYes.setOnClickListener {
            onChangePasswordClick() // Call the original method to send the reset email
            dialog.dismiss() // Dismiss the dialog after action
        }

        // Set up the No button click listener
        buttonNo.setOnClickListener { dialog.dismiss() } // Dismiss the dialog

        // Show the dialog
        dialog.show()
    }

    private fun onChangePasswordClick() {
        val email = currentUser.email
        if (email != null) {
            // Send password reset email
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "User email not found.", Toast.LENGTH_SHORT).show()
        }
    }
}