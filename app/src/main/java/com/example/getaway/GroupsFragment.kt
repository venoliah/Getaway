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

// UserGroups.kt
data class UserGroups(
    val groupName: List<String> // Updated field name to match Firestore document
)

// Group.kt
data class Group(
    val id: String, // Replace with the actual type of your group ID
    val name: String // Replace with the actual type of your group name
)

class GroupsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var groupAdapter: GroupAdapter

    // ... (other declarations)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groups, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewGroups)
        addButton = view.findViewById(R.id.addButton)

        // Initialize RecyclerView
        groupAdapter = GroupAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = groupAdapter

        addButton.setOnClickListener {
            // Handle click on the add button (e.g., navigate to NewGroupActivity)
            val intent = Intent(requireContext(), NewGroupActivity::class.java)
            startActivity(intent)
        }

        // Load user's groups from Firestore
        loadUserGroups()

        return view
    }

    private fun loadUserGroups() {
        // TODO: Implement Firestore query to get user's groups
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            val userId = user.uid
            val myGroupsRef = FirebaseFirestore.getInstance().collection("groups").document(userId)

            myGroupsRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Retrieve the "groupName" field from the document
                        val groupName = documentSnapshot.get("groupName") as? List<String>

                        groupName?.let {
                            // Update the RecyclerView with the list of group names
                            groupAdapter.setGroupNames(groupName)
                            // Log for debugging
                            Log.d("GroupsFragment", "Retrieved group names: $groupName")
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    Log.e("GroupsFragment", "Error getting user groups", exception)
                }
        }
    }

// GroupAdapter.kt
class GroupAdapter : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {
    private var groupName: List<String> = emptyList()

    fun setGroupNames(groupName: List<String>) {
        this.groupName = groupName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        Log.d("GroupAdapter", "onCreateViewHolder called")
        return GroupViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val groupName = groupName[position]
        holder.bind(groupName)
        Log.d("GroupAdapter", "onBindViewHolder called for position: $position")
    }
    override fun getItemCount(): Int {
        return groupName.size
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNameTextView: TextView = itemView.findViewById(R.id.textViewGroupName)
        fun bind(groupName: String) {
            groupNameTextView.text = groupName
            // You can add click listeners or other logic here if needed
            // Log for debugging
            Log.d("GroupAdapter", "Binding group name: $groupName")
        }
    }

}}