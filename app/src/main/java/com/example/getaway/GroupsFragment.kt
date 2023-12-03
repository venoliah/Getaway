package com.example.getaway

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.TextView
import android.widget.Toast

// Group.kt
data class Group(
    val groupName: String = "",
    val userId: String = ""
)

class GroupsFragment : Fragment(), GroupAdapter.OnItemClickListener  {

    private lateinit var recyclerView: RecyclerView
    //private lateinit var addButton: Button
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groups, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewGroups)
        // addButton = view.findViewById(R.id.addButton)

        // Initialize RecyclerView and adapter
        groupAdapter = GroupAdapter(requireContext(),this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = groupAdapter

        // addButton.setOnClickListener {
        // Handle click on the add button (e.g., navigate to NewGroupActivity)
        // val intent = Intent(requireContext(), NewGroupActivity::class.java)
        // startActivity(intent)
        // }
        loadUserGroups()

        return view
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
                    Log.e("GroupsFragment", "Error getting user groups", exception)
                    Toast.makeText(
                        requireContext(),
                        "Error getting user groups",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    override fun onItemClick(group: Group) {
        val intent = Intent(requireContext(), GroupHomepageActivity::class.java)
        intent.putExtra("groupName", group.groupName)
        startActivity(intent)
    }
}
