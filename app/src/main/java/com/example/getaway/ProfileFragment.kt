package com.example.getaway

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var profileInfoTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var saveButton: Button

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize views
        profileInfoTextView = view.findViewById(R.id.profileInfoTextView)
        nameEditText = view.findViewById(R.id.editTextName)
        bioEditText = view.findViewById(R.id.editTextBio)
        saveButton = view.findViewById(R.id.saveButton)

        // Load and display user's email
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userEmail = user.email
            userEmail?.let {
                profileInfoTextView.text = "Email: $it"
            }
        }

        // Load and display existing profile information (if any)
        loadProfileData()

        // Set onClickListener for the Save button
        saveButton.setOnClickListener {
            saveProfileData()
        }

        return view
    }

    private fun loadProfileData() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            firebaseFirestore.collection("profiles").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val name = documentSnapshot.getString("name")
                        val bio = documentSnapshot.getString("bio")

                        nameEditText.setText(name)
                        bioEditText.setText(bio)
                    }
                }
        }
    }

    private fun saveProfileData() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val name = nameEditText.text.toString().trim()
            val bio = bioEditText.text.toString().trim()

            val profileData = hashMapOf(
                "name" to name,
                "bio" to bio
            )

            firebaseFirestore.collection("profiles").document(userId)
                .set(profileData)
                .addOnSuccessListener {
                    profileInfoTextView.text = "Email: ${user.email}\nName: $name\nBio: $bio"
                    profileInfoTextView.setTextColor(resources.getColor(android.R.color.holo_purple)) // Set text color to white
                }
                .addOnFailureListener { e ->
                    // Handle the failure
                }
        }
    }
}
//