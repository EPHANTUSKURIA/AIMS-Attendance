package com.example.aimsoftattendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LeaveViewModel : ViewModel() {
    private val _leaveRequests = MutableLiveData<List<LeaveRequest>>()
    val leaveRequests: LiveData<List<LeaveRequest>> get() = _leaveRequests

    // Implement leave request submission logic here
    fun submitLeaveRequest(leaveRequest: LeaveRequest) {
        // Example: Add request to the list
        val updatedList = _leaveRequests.value.orEmpty() + leaveRequest
        _leaveRequests.postValue(updatedList)

        // TODO: Save the request to a database or send it to a server
    }

    // Implement logic to load leave requests for an employee
    fun loadLeaveRequests(employeeId: String) {
        // TODO: Load leave requests from a database or server
        // Example dummy data:
        val dummyRequests = listOf(
            LeaveRequest("1", "Sick Leave", "2024-08-01", "2024-08-03", "Feeling unwell"),
            LeaveRequest("2", "Vacation", "2024-08-10", "2024-08-15", "Family trip")
        )
        _leaveRequests.postValue(dummyRequests)
    }
}
