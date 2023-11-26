package com.example.getaway

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.text.InputType

class MembersActivity : AppCompatActivity() {

    private lateinit var groupName: String
    private var membersCount: Int = 0
    private lateinit var editTexts: MutableList<EditText> // Change to MutableList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        // Retrieve group information from intent
        groupName = intent.getStringExtra("groupName") ?: ""
        membersCount = intent.getIntExtra("members", 0)

        // Dynamically generate EditText fields based on 'membersCount'
        generateEditTextFields()

        // Set click listener for the submit button
        val buttonSubmitMembers = findViewById<Button>(R.id.buttonSubmitMembers)
        buttonSubmitMembers.setOnClickListener {
            handleSubmitButtonClick()
        }
    }

    private fun generateEditTextFields() {
        editTexts = mutableListOf()

        val layout = findViewById<LinearLayout>(R.id.linearLayoutMembers)

        for (i in 1..membersCount) {
            val editText = EditText(this)
            editText.hint = "Member Email $i"
            editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            editTexts.add(editText)
            layout.addView(editText)
        }
    }

    private fun handleSubmitButtonClick() {
        // Collect member emails from EditText fields
        val memberEmails = editTexts.map { it.text.toString() }

        // TODO: Implement logic to send invites, save to Firestore, etc.

        // Once done, start the Group Homepage activity
        startGroupHomepage()
    }

    private fun startGroupHomepage() {
        val intent = Intent(this, GroupHomepageActivity::class.java)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
        finish() // Optional: Close this activity if needed
    }
}
