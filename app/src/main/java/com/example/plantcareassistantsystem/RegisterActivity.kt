package com.example.plantcareassistantsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.registerEmail)
        val password = findViewById<EditText>(R.id.registerPassword)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val backToLogin = findViewById<TextView>(R.id.backToLogin)

        registerButton.setOnClickListener {

            val userName = name.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString()
            val userConfirmPassword = confirmPassword.text.toString()

            // ================= CHECK EMPTY FIELDS =================

            if (userName.isEmpty()) {
                name.error = "Enter your name"
                name.requestFocus()
                return@setOnClickListener
            }

            if (userEmail.isEmpty()) {
                email.error = "Enter your email"
                email.requestFocus()
                return@setOnClickListener
            }

            if (userPassword.isEmpty()) {
                password.error = "Enter your password"
                password.requestFocus()
                return@setOnClickListener
            }

            if (userPassword.length < 6) {
                password.error = "Password must be at least 6 characters"
                password.requestFocus()
                return@setOnClickListener
            }

            if (userConfirmPassword.isEmpty()) {
                confirmPassword.error = "Confirm your password"
                confirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (userPassword != userConfirmPassword) {
                confirmPassword.error = "Passwords do not match"
                confirmPassword.requestFocus()
                return@setOnClickListener
            }

            // ================= CREATE FIREBASE ACCOUNT =================

            auth.createUserWithEmailAndPassword(
                userEmail,
                userPassword
            ).addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    // ================= SAVE PROFILE DETAILS =================

                    val preferences =
                        getSharedPreferences(
                            "PlantCareProfile",
                            MODE_PRIVATE
                        )

                    preferences.edit()
                        .putString("name", userName)
                        .putString("email", userEmail)
                        .apply()

                    // ================= SUCCESS MESSAGE =================

                    Toast.makeText(
                        this,
                        "Account created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // ================= GO TO LOGIN =================

                    val intent =
                        Intent(
                            this,
                            MainActivity::class.java
                        )

                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(
                        this,
                        task.exception?.message
                            ?: "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // ================= ALREADY HAVE ACCOUNT =================

        backToLogin.setOnClickListener {

            val intent =
                Intent(
                    this,
                    MainActivity::class.java
                )

            startActivity(intent)
            finish()
        }
    }
}