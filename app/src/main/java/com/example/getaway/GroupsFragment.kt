package com.example.getaway

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

// UserGroups.kt
data class UserGroups(
    val groups: List<Group>
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

        // Set up button click listener
        addButton.setOnClickListener {
            // Handle click on the add button (e.g., navigate to NewGroupActivity)
            // You can add your logic here
        }

        // Load user's groups from Firestore
        loadUserGroups()

        return view
    }

    private fun loadUserGroups() {
        // TODO: Implement Firestore query to get user's groups
        // You may use Firebase Firestore to perform the query

        // Example (assuming you have a FirebaseUser object)
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val userId = user.uid
            val myGroupsRef = FirebaseFirestore.getInstance().collection("my_groups").document(userId)

            myGroupsRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val groups = documentSnapshot.toObject(UserGroups::class.java)
                        groups?.let {
                            // Update the RecyclerView with the list of groups
                            groupAdapter.setGroups(groups.groups)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    Log.e("GroupsFragment", "Error getting user groups", exception)
                }
        }
    }
}

// GroupAdapter.kt
class GroupAdapter : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private var groups: List<Group> = emptyList()

    fun setGroups(groups: List<Group>) {
        this.groups = groups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement ViewHolder binding logic
        // You can set text, click listeners, etc., for the item view
        fun bind(group: Group) {
            // Example:
            // itemView.findViewById<TextView>(R.id.textViewGroupName).text = group.name
            // itemView.setOnClickListener { /* Handle item click */ }
        }
    }
}
