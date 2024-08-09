package com.example.aimsoftattendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.aimsoftattendance.databinding.FragmentLeaveRequestsBinding

class LeaveRequestsFragment : Fragment() {

    private lateinit var binding: FragmentLeaveRequestsBinding
    private val viewModel: LeaveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaveRequestsBinding.inflate(inflater, container, false)

        // Set up RecyclerView with LeaveRequestsAdapter
        val adapter = LeaveRequestsAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        // Observe leave requests
        viewModel.leaveRequests.observe(viewLifecycleOwner, Observer { requests ->
            adapter.updateData(requests)
        })

        // Load leave requests for a specific employee (replace with actual employee ID)
        viewModel.loadLeaveRequests("employeeId")

        return binding.root
    }
}
