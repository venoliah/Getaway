package com.example.getaway

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Toast

class GroupsActivity : AppCompatActivity(), GroupAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_groups)

        recyclerView = findViewById(R.id.recyclerViewGroups)

        groupAdapter = GroupAdapter(this, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = groupAdapter

        loadUserGroups()
    }

    private fun loadUserGroups() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            val userId = user.uid
            val groupsRef = FirebaseFirestore.getInstance().collection("groups")

            groupsRef.whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val groupList = mutableListOf<Group>()

                    for (document in documents) {
                        val groupName = document.getString("groupName") ?: ""
                        val group = Group(groupName)
                        groupList.add(group)
                    }

                    groupAdapter.setGroups(groupList)
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    Log.e("GroupsActivity", "Error getting user groups", exception)
                    Toast.makeText(
                        this,
                        "Error getting user groups",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onItemClick(group: Group) {
        val intent = Intent(this, GroupHomepageActivity::class.java)
        intent.putExtra("groupName", group.groupName)
        startActivity(intent)
    }
}
//