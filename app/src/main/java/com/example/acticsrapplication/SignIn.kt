package com.example.acticsrapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignIn : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signInButton: MaterialButton
    private lateinit var goToSignUpTextView: TextView
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Email is required"
            isValid = false
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Password is required"
            isValid = false
        }

        return isValid
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Check if user is already signed in
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            // User is signed in, redirect to MainActivity
//            val intent = Intent(this, AdminMainActivity::class.java)
//            startActivity(intent)
//            finish() // Finish SignIn activity so it doesn't stay in the back stack
            fetchUserRoleAndRedirect(currentUser.uid)
            return
        }

        // Set up views
        emailEditText = findViewById(R.id.emailInput)
        passwordEditText = findViewById(R.id.passwordInput)
        signInButton = findViewById(R.id.signInButton)
        goToSignUpTextView = findViewById(R.id.go_register_screen)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                signin(email, password)
            }
        }

        goToSignUpTextView.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        // Set up forgot password functionality
        forgotPasswordTextView.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                emailEditText.error = "Please enter your email to reset the password"
            } else {
                showResetPasswordDialog(email)
            }
        }
    }

    private fun signin(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null && user.isEmailVerified) {
//                        Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
//
//                        // Navigate to MainActivity after successful sign-in
//                        val intent = Intent(this, AdminMainActivity::class.java)
//                        startActivity(intent)
//                        finish() // Optional: Call finish() to remove SignIn from the back stack
                        fetchUserRoleAndRedirect(user.uid)
                    } else {
                        Toast.makeText(this, "Please verify your email first", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Sign in failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showResetPasswordDialog(email: String) {
        AlertDialog.Builder(this)
            .setTitle("Reset Password")
            .setMessage("Are you sure you want to reset your password for $email?")
            .setPositiveButton("Yes") { _, _ ->
                resetPassword(email)
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    private fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
