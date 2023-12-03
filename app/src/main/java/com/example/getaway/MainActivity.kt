package com.example.getaway

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance()

        // Check if the user is signed in
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            // If not signed in, open RegistrationActivity
            openRegistration()
        } else {
            // If signed in, you can proceed with the main functionality
            // For example, you can display a welcome message
            // You may want to fetch user data from Firestore at this point
            val email = currentUser.email
            // Do something with the user's email
        }
    }

    private fun openRegistration() {
        // Open RegistrationActivity
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }
}
//