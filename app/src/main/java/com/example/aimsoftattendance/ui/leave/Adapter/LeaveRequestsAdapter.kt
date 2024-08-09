package com.example.aimsoftattendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aimsoftattendance.databinding.ItemLeaveRequestBinding

class LeaveRequestsAdapter(private var leaveRequests: List<LeaveRequest>) : RecyclerView.Adapter<LeaveRequestsAdapter.ViewHolder>() {

    // ViewHolder to hold views
    class ViewHolder(private val binding: ItemLeaveRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(request: LeaveRequest) {
            binding.leaveRequest = request
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaveRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(leaveRequests[position])
    }

    override fun getItemCount(): Int = leaveRequests.size

    // Method to update data
    fun updateData(newRequests: List<LeaveRequest>) {
        leaveRequests = newRequests
        notifyDataSetChanged()
    }
}
