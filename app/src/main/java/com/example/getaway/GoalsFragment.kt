package com.example.getaway

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GoalsFragment : Fragment() {

    private lateinit var goalsTextView: TextView
    private lateinit var contributionTextView: TextView
    private lateinit var editGoalsButton: Button
    private lateinit var editGoalsEditText: EditText
    private lateinit var saveGoalsButton: Button

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        // Initialize views and buttons
        goalsTextView = view.findViewById(R.id.goalsTextView)
        contributionTextView = view.findViewById(R.id.contributionTextView)
        editGoalsButton = view.findViewById(R.id.editGoalsButton)
        editGoalsEditText = view.findViewById(R.id.editGoalsEditText)
        saveGoalsButton = view.findViewById(R.id.saveGoalsButton)

        // Retrieve goals and contribution amount from Firebase using the group name
        val groupName = arguments?.getString("groupName") // Retrieve the group name from arguments
        groupName?.let {
            retrieveGoalsAndContribution(it)
        }

        // Set up the button click listeners
        editGoalsButton.setOnClickListener {
            editGoalsEditText.setText(goalsTextView.text)
            toggleEditVisibility(true)
        }

        saveGoalsButton.setOnClickListener {
            val newGoals = editGoalsEditText.text.toString()
            groupName?.let {
                updateGoals(it, newGoals)
            }
            toggleEditVisibility(false)
        }

        return view
    }

    private fun retrieveGoalsAndContribution(groupName: String) {
        firebaseFirestore.collection("groups").document(groupName)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val goals = documentSnapshot.getString("groupGoal")
                    val contribution = documentSnapshot.getDouble("amount")

                    // Update the UI with retrieved goals and contribution
                    goalsTextView.text = goals ?: "No goals available"
                    contributionTextView.text = "Contribution Amount: $$contribution"
                }
            }
            .addOnFailureListener { exception ->
                Log.e("GoalsFragment", "Error retrieving goals and contribution", exception)
            }
    }


    private fun updateGoals(groupName: String, newGoals: String) {
        // Replace "goals" with your actual field name in the Firestore document
        firebaseFirestore.collection("groups").document(groupName)
            .update("goals", newGoals)
            .addOnSuccessListener {
                goalsTextView.text = newGoals
            }
    }

    private fun toggleEditVisibility(editMode: Boolean) {
        if (editMode) {
            goalsTextView.visibility = View.GONE
            contributionTextView.visibility = View.GONE
            editGoalsButton.visibility = View.GONE
            editGoalsEditText.visibility = View.VISIBLE
            saveGoalsButton.visibility = View.VISIBLE
        } else {
            goalsTextView.visibility = View.VISIBLE
            contributionTextView.visibility = View.VISIBLE
            editGoalsButton.visibility = View.VISIBLE
            editGoalsEditText.visibility = View.GONE
            saveGoalsButton.visibility = View.GONE
        }
    }
}
//