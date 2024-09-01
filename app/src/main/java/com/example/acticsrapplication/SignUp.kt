package com.example.acticsrapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUp : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var goToLoginTextView: TextView


    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        if (TextUtils.isEmpty(name)){
            nameEditText.error = "Name is required"
        }

        if (TextUtils.isEmpty(email)){
            nameEditText.error = "Email is required"
        }

        if (TextUtils.isEmpty(password)) {
            nameEditText.error = "Password is required"
        }

        if (TextUtils.isEmpty(confirmPassword)){
            nameEditText.error = "Password must be at least 6 character long"
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.go_login_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nameEditText = findViewById<>(R.id.nameEditText)
        emailEditText = findViewById<>(R.id.emailEditText)
        passwordEditText = findViewById<>(R.id.passwordEditText)
        confirmPasswordEditText = findViewById<>(R.id.confirmPasswordEditText)
        signUpButton = findViewById<>(R.id.signUpButton)
        goToLoginTextView = findViewById<>(R.id.go_login_screen)

        signUpButton.setOnClickLister {
            val name = nameEditText.text.toString().trim()
            val email = nameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (validateInput(name, email , password , confirmPassword)) {
                signup(name,email,password)
            }
        }
    }
}