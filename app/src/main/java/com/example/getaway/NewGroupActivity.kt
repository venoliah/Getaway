package com.example.getaway
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent

class NewGroupActivity : AppCompatActivity() {

    private lateinit var editTextGroupName: EditText
    private lateinit var editTextMembers: EditText
    private lateinit var spinnerGroupType: Spinner
    private lateinit var editTextGroupGoal: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var buttonLeave: Button
    private lateinit var editTextAmount: EditText


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newgroup)

        // Initialize FirebaseAuth and FirebaseFirestore
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize views
        editTextGroupName = findViewById(R.id.editTextGroupName)
        editTextMembers = findViewById(R.id.editTextMembers)
        spinnerGroupType = findViewById(R.id.spinnerGroupType)
        editTextGroupGoal = findViewById(R.id.editTextGroupGoal)
        editTextAmount = findViewById(R.id.editTextAmount)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        buttonLeave = findViewById(R.id.buttonLeave)

        // Populate spinner with group types
        val groupTypes = resources.getStringArray(R.array.group_types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGroupType.adapter = adapter

        // Set click listener for submit button
        buttonSubmit.setOnClickListener {
            saveGroupData()
        }

        // Set click listener for leave button
        buttonLeave.setOnClickListener {
            // Handle leave action, e.g., navigate back to HomeActivity
            finish()
        }
    }

    private fun saveGroupData() {
        val groupName = editTextGroupName.text.toString().trim()
        val members = editTextMembers.text.toString().trim().toIntOrNull()
        val groupType = spinnerGroupType.selectedItem.toString()
        val groupGoal = editTextGroupGoal.text.toString().trim()
        val amount = editTextAmount.text.toString().trim().toDoubleOrNull()

        if (groupName.isNotEmpty() && members != null && amount != null) {
            val userId = mAuth.currentUser?.uid
            userId?.let {
                val groupData = hashMapOf(
                    "groupName" to groupName,
                    "members" to members,
                    "groupType" to groupType,
                    "groupGoal" to groupGoal,
                    "amount" to amount,
                    "createdBy" to userId
                )

                db.collection("groups").add(groupData)
                    .addOnSuccessListener { documentReference ->
                        // Data saved successfully
                        // documentReference.id contains the ID of the newly created document
                        val intent = Intent(this, MembersActivity::class.java)
                        intent.putExtra("groupName", groupName)
                        intent.putExtra("members", members)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        // You may want to show an error message to the user
                        e.printStackTrace()
                    }
            }
        } else {
            // Handle invalid input, show a message, etc.
        }
    }

}
//