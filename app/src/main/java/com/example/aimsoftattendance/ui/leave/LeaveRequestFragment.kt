package com.example.aimsoftattendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aimsoftattendance.databinding.FragmentLeaveRequestBinding

class LeaveRequestFragment : Fragment() {

    private lateinit var binding: FragmentLeaveRequestBinding
    private val viewModel: LeaveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and initialize the binding
        binding = FragmentLeaveRequestBinding.inflate(inflater, container, false)

        // Set up the click listener for the submit button
        binding.submitLeaveRequest.setOnClickListener {
            val leaveType = binding.leaveType.text.toString()
            val startDate = binding.startDate.text.toString()
            val endDate = binding.endDate.text.toString()
            val comments = binding.comments.text.toString()

            // Validate and submit leave request
            if (validateLeaveRequest(leaveType, startDate, endDate)) {
                val leaveRequest = LeaveRequest(
                    id = "", // Set appropriate ID or leave empty if auto-generated
                    leaveType = leaveType,
                    startDate = startDate,
                    endDate = endDate,
                    comments = comments
                )
                submitLeaveRequest(leaveRequest)
            }
        }

        return binding.root
    }

    // Validate the leave request details
    private fun validateLeaveRequest(leaveType: String, startDate: String, endDate: String): Boolean {
        // Example validation logic
        return leaveType.isNotBlank() && startDate.isNotBlank() && endDate.isNotBlank()
    }

    // Handle submission of the leave request
    private fun submitLeaveRequest(leaveRequest: LeaveRequest) {
        viewModel.submitLeaveRequest(leaveRequest)
        // Optionally show a confirmation message or handle UI updates
        println("Leave Request Submitted:")
        println("Type: ${leaveRequest.leaveType}")
        println("Start Date: ${leaveRequest.startDate}")
        println("End Date: ${leaveRequest.endDate}")
        println("Comments: ${leaveRequest.comments}")

        // Optionally navigate to LeaveRequestsFragment or refresh the UI
    }
}

