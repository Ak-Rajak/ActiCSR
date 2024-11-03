package com.example.acticsrapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var goToLoginTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(name)) {
            nameEditText.error = "Name is required"
            isValid = false
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Email is required"
            isValid = false
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters long"
            isValid = false
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.error = "Confirm Password is required"
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEditText = findViewById(R.id.nameInput)
        emailEditText = findViewById(R.id.emailInput)
        passwordEditText = findViewById(R.id.passwordInput)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordInput)
        signUpButton = findViewById(R.id.signUpButton)
        goToLoginTextView = findViewById(R.id.go_login_screen)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        goToLoginTextView.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (validateInput(name, email, password, confirmPassword)) {
                signup(name,email, password)
            }
        }
    }

    private fun signup(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
//                    // Optionally navigate to SignIn or another activity
//                    val intent = Intent(this, SignIn::class.java)
//                    startActivity(intent)
//                    finish()
                    val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener

                    val userData = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "role" to "user"
                     )

                    firestore.collection("user").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Registration successful",Toast.LENGTH_SHORT).show()
                            // Redirect to mainActivity
                            val intent = Intent(this,SignIn::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this,"Failed to save user data: ${e.message}",Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
