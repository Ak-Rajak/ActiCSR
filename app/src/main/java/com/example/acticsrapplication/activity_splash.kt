package com.example.acticsrapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class activity_splash : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set a delay for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatusAndRedirect()
        }, 1000) // Display splash screen for 4 seconds
    }

    private fun checkUserStatusAndRedirect() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null && currentUser.isEmailVerified) {
            // User is verified, fetch role and redirect
            fetchUserRoleAndRedirect(currentUser.uid)
        } else {
            // If user is not verified, redirect to SignIn
            redirectToSignIn()
        }
    }

    private fun fetchUserRoleAndRedirect(userId: String) {
        val userRef = firestore.collection("user").document(userId)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role")
                    val intent = if (role == "admin") {
                        Intent(this, AdminMainActivity::class.java)
                    } else {
                        Intent(this, MainActivity::class.java)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_LONG).show()
                    redirectToSignIn()
                }
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to retrieve user data: ${e.message}", Toast.LENGTH_LONG).show()
                redirectToSignIn()
            }
    }

    private fun redirectToSignIn() {
        startActivity(Intent(this, SignIn::class.java))
        finish()
    }
}
