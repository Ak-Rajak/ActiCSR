package com.example.acticsrapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignIn : AppCompatActivity() {
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signInButton: MaterialButton
    private lateinit var goToSignUpTextView: TextView

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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailEditText = findViewById(R.id.emailInput)
        passwordEditText = findViewById(R.id.passwordInput)
        signInButton = findViewById(R.id.signInButton)
        goToSignUpTextView = findViewById(R.id.go_register_screen)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                signin(email, password)
            }
        }

        goToSignUpTextView.setOnClickListener {
            // Navigate to SignUp activity
            val intent = Intent(this@SignIn, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun signin(email: String, password: String) {
        // Retrieve user details from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val storedEmail = sharedPreferences.getString("Email", null)
        val storedPassword = sharedPreferences.getString("Password", null)

        // Validate the credentials
        if (email == storedEmail && password == storedPassword) {
            Toast.makeText(this, "Sign in successful for $email", Toast.LENGTH_SHORT).show()
            // Navigate to another activity if needed
            // Example: val intent = Intent(this, HomeActivity::class.java)
            // startActivity(intent)
            // finish()
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }
    }
}
