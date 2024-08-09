package com.example.aimsoftattendance

data class LeaveRequest(
    val id: String, // Use a unique identifier for the request
    val leaveType: String,
    val startDate: String,
    val endDate: String,
    val comments: String
)

