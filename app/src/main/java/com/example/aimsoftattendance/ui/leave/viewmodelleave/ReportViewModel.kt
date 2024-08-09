package com.example.aimsoftattendance.ui.leave.viewmodelleave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class LeaveRequestReport(val leaveType: String, val count: Int)

class ReportViewModel : ViewModel() {

    private val _reportData = MutableLiveData<List<LeaveRequestReport>>()
    val reportData: LiveData<List<LeaveRequestReport>> get() = _reportData

    init {
        loadReportData()
    }

    fun loadReportData() {
        // Example data, replace this with actual data fetching logic
        val data = listOf(
            LeaveRequestReport("Sick Leave", 10),
            LeaveRequestReport("Vacation", 20),
            LeaveRequestReport("Personal Leave", 5)
        )
        _reportData.value = data
    }
}
