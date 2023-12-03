package com.example.getaway

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    // Called when the Register button is clicked
    fun onRegisterClick(view: View) {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    // Called when the Login button is clicked
    fun onLoginClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    // Called when the Exit button is clicked
    fun onExitClick(view: View) {
        finish() // This will close the activity and exit the app
    }
}
//
