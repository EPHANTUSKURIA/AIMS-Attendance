package com.example.aimsoftattendance.ui.history.viewmodelhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aimsoftattendance.AttendanceRecord

class AttendanceHistoryViewModel : ViewModel() {

    private val _attendanceData = MutableLiveData<List<AttendanceRecord>>()
    val attendanceData: LiveData<List<AttendanceRecord>> = _attendanceData

    init {
        // Load your attendance data here
        _attendanceData.value = listOf(
            AttendanceRecord("Alice Johnson", "alice.johnson@example.com", "+1234567890", 20, 2, 1, "2023-01-01", 8f, 0f, 2f, 10f),
            AttendanceRecord("Bob Smith", "bob.smith@example.com", "+0987654321", 18, 3, 2, "2023-01-02", 7f, 1f, 1f, 8f),
            AttendanceRecord("Charlie Brown", "charlie.brown@example.com", "+1122334455", 22, 1, 0, "2023-01-03", 8f, 0f, 0f, 8f),
            AttendanceRecord("Diana Prince", "diana.prince@example.com", "+5566778899", 15, 5, 2, "2023-01-04", 6f, 2f, 1f, 9f),
            AttendanceRecord("Eve Adams", "eve.adams@example.com", "+6677889900", 25, 0, 0, "2023-01-05", 8f, 0f, 0f, 8f),
            AttendanceRecord("Frank Castle", "frank.castle@example.com", "+7788990011", 17, 4, 1, "2023-01-06", 7f, 1f, 0f, 8f),
            AttendanceRecord("Grace Kelly", "grace.kelly@example.com", "+8899002233", 21, 2, 1, "2023-01-07", 8f, 0f, 2f, 10f),
            AttendanceRecord("Henry Ford", "henry.ford@example.com", "+9900112233", 19, 3, 1, "2023-01-08", 7f, 1f, 1f, 8f)
        )
    }
}
