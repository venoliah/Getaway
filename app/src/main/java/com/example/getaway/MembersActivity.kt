package com.example.getaway

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MembersActivity : AppCompatActivity() {

    private lateinit var groupName: String
    private var membersCount: Int = 0
    private lateinit var editTexts: MutableList<EditText> // Change to MutableList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        //FirebaseApp.initializeApp(this)
        if (isUserAuthenticated()) {
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
        } else {
            // User is not authenticated, handle authentication (e.g., show login screen)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            showToast("Authentication required. Please log in.")
            finish() // Optionally, finish the current activity if needed
        }
    }

    private fun isUserAuthenticated(): Boolean {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser != null
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

        // Validate email addresses
        if (validateEmails(memberEmails)) {
            // TODO: Implement logic to send invites, save to Firestore, etc.

            sendEmailInvitations(groupName, memberEmails)
            saveGroupToFirestore(groupName, memberEmails)
            // Once done, start the Group Homepage activity
            startGroupHomepage()
        } else {
            // Show error if any of the emails are invalid
            showToast("Please enter valid email addresses.")
        }
    }

    private fun sendEmailInvitations(groupName: String, memberEmails: List<String>) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch {
            val sendEmailTasks = memberEmails.map { emailAddress ->
                async {
                    try {
                        // Validate the email address before sending
                        if (isValidEmail(emailAddress)) {
                            val message = createEmailMessage(groupName, emailAddress)
                            Transport.send(message)
                            showToast("Invitation sent successfully to: $emailAddress")
                        } else {
                            showToast("Invalid email address: $emailAddress")
                        }
                    } catch (e: MessagingException) {
                        // Log the exception for debugging
                        e.printStackTrace()
                        Log.e("EmailDebug", "Error sending invitation to: $emailAddress. Error: ${e.message}")
                        showToast("Error sending invitation to: $emailAddress")
                    }
                }
            }

            sendEmailTasks.awaitAll()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }
    private fun createEmailMessage(groupName: String, emailAddress: String?): Message {
        val session = createEmailSession()


        return MimeMessage(session).apply {
            setFrom(InternetAddress("newiachibs@gmail.com"))
            // Add a null check before parsing the email address
            if (emailAddress != null) {
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress))
                subject = "Invitation from Getaway to Join $groupName Group"
                var registrationLink = ""
                setText("Hello,\n\nYou are invited to join the $groupName group and be part of an exciting adventurous community! \n" +
                        "\n" +
                        "Accept the invitation and join us by clicking here: $registrationLink. This link will lead you to the registration page, where you can become an official member and access exclusive benefits.\n" +
                        "\n" +
                        "If joining isn't the right fit for you at the moment, we completely understand. No pressure, we respect your decision.\n" +
                        "\n" +
                        "Looking forward to having you onboard,\n" +
                        "\n" +
                        "[$groupName]")
            }
        }
    }

    private fun createEmailSession(): Session {
        val properties = Properties()

        try {
            // Load properties from the email.properties file in the assets directory
            val inputStream: InputStream = assets.open("email.properties")
            properties.load(inputStream)

            // Log properties for debugging
            Log.d("EmailDebug", "Loaded properties: $properties")
        } catch (e: IOException) {
            Log.e("EmailDebug", "Error loading properties: ${e.message}")
            e.printStackTrace()
        }

        return Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                val username = properties.getProperty("mail.smtp.user")
                val password = properties.getProperty("mail.smtp.password")

                // Log authentication details for debugging
                Log.d("EmailDebug", "Username: $username, Password: $password")

                return PasswordAuthentication(username, password)
            }
        })
    }


    private fun saveGroupToFirestore(groupName: String, memberEmails: List<String>) {
        val db = FirebaseFirestore.getInstance()
        val groupRef = db.collection("groupsInfo").document(groupName)

        // Add or update group information
        groupRef.set(mapOf("members" to memberEmails))
            .addOnSuccessListener {
                showToast("Group information saved successfully!")
                // Continue with the next steps or navigate to the Group Homepage
            }
            .addOnFailureListener { e ->
                showToast("Error saving group information: ${e.message}")
            }
    }


    private fun validateEmails(emails: List<String>): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS
        return emails.all { emailPattern.matcher(it).matches() }
    }

    private fun startGroupHomepage() {
        val intent = Intent(this, GroupHomepageActivity::class.java)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
        finish() // Optional: Close this activity if needed
    }
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
//