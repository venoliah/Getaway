package com.example.getaway

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private var groups: List<Group> = emptyList()

    fun setGroups(groups: List<Group>) {
        Log.d("GroupsDebug", "Updating groups: $groups")
        //showToast("Group names updated: $groups")
        this.groups = groups
        notifyDataSetChanged()
    }
    //

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group)
       // showToast("Binding group name at position $position: $group")
        // Set click listener on the item view
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(group)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNameTextView: TextView = itemView.findViewById(R.id.textViewGroupName)

        fun bind(group: Group) {
            groupNameTextView.text = group.groupName
            // Add click listeners or other logic if needed
        }
    }
    interface OnItemClickListener {
        fun onItemClick(group: Group)
    }
}
