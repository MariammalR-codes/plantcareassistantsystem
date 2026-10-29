package com.example.plantcareassistantsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ================= CHECK LOGIN STATE =================

        val prefs = getSharedPreferences(
            "PlantCarePrefs",
            MODE_PRIVATE
        )

        if (prefs.getBoolean("isLoggedIn", false)) {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
            return
        }

        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // ================= EMAIL & PASSWORD =================

        val email = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)

        // ================= BUTTONS =================

        val loginButton = findViewById<Button>(
            R.id.loginButton
        )

        val createAccount = findViewById<TextView>(
            R.id.createAccount
        )

        val forgotPassword = findViewById<TextView>(
            R.id.forgotPassword
        )

        // ================= LOGIN =================

        loginButton.setOnClickListener {

            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString()

            // Check email
            if (!userEmail.contains("@")) {

                email.error = "Enter your email"
                email.requestFocus()
                return@setOnClickListener
            }

            // Check password
            if (userPassword.isEmpty()) {

                password.error = "Enter your password"
                password.requestFocus()
                return@setOnClickListener
            }

            // ================= FIREBASE LOGIN =================

            auth.signInWithEmailAndPassword(
                userEmail,
                userPassword
            ).addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    // ================= SAVE LOGIN STATE =================

                    getSharedPreferences(
                        "PlantCarePrefs",
                        MODE_PRIVATE
                    )
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .apply()

                    Toast.makeText(
                        this,
                        "Login successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // ================= GO TO HOME =================

                    val intent = Intent(
                        this,
                        HomeActivity::class.java
                    )

                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // ================= FORGOT PASSWORD =================

        forgotPassword.setOnClickListener {

            val userEmail = email.text.toString().trim()

            if (userEmail.isEmpty()) {

                email.error = "Enter your email first"
                email.requestFocus()

                Toast.makeText(
                    this,
                    "Enter your registered email",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Password reset email sent!",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this,
                            "Unable to send password reset email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // ================= CREATE ACCOUNT =================

        createAccount.setOnClickListener {

            val intent = Intent(
                this,
                RegisterActivity::class.java
            )

            startActivity(intent)
        }
    }
}