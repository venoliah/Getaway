package com.example.getaway

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class ActivityFragment : Fragment() {

    private lateinit var activityDetailsTextView: TextView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        activityDetailsTextView = view.findViewById(R.id.activityDetailsTextView)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Fetch and display payment details
        fetchAndDisplayPaymentDetails()

        return view
    }

    private fun fetchAndDisplayPaymentDetails() {
        // Query the "payments" collection
        firestore.collection("payments")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val activityDetails = StringBuilder()

                for (document in querySnapshot) {
                    // Get details from each document
                    val phoneNumber = document.getString("phoneNumber")
                    val amount = document.getString("amount")
                    val timestamp = document.getTimestamp("timestamp")

                    // Append details to the StringBuilder
                    activityDetails.append("Phone Number: $phoneNumber\n")
                    activityDetails.append("Amount: $$amount\n")
                    activityDetails.append("Timestamp: $timestamp\n\n")
                }

                // Display details in the TextView
                activityDetailsTextView.text = activityDetails.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("ActivityFragment", "Error fetching payment details", exception)
            }
    }
}
