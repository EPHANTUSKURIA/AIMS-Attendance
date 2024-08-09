package com.example.aimsoftattendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.sumOf


class DetailedAttendanceViewModel : ViewModel() {

    private val _detailedAttendanceData = MutableLiveData<List<DetailedAttendanceRecord>>()
    val detailedAttendanceData: LiveData<List<DetailedAttendanceRecord>> = _detailedAttendanceData

    private val _detailedRecords = MutableLiveData<List<AttendanceRecord>>()
    val detailedRecords: LiveData<List<AttendanceRecord>> get() = _detailedRecords

    private val _weeklySummaryData = MutableLiveData<WeeklySummary>()
    val weeklySummaryData: LiveData<WeeklySummary> = _weeklySummaryData

    init {
        loadDetailedAttendanceData()
        computeWeeklySummary()
    }

    fun loadDetailedRecords(date: String) {
        // Load detailed records based on date
        _detailedRecords.value = fetchDataForDate(date)
    }

    private fun fetchDataForDate(date: String): List<AttendanceRecord> {
        // Sample data for demonstration
        val allRecords = listOf(
            AttendanceRecord("Alice Johnson", "alice.johnson@example.com", "+1234567890", 20, 2, 1, "2023-01-01", 8f, 0f, 2f, 10f),
            AttendanceRecord("Bob Smith", "bob.smith@example.com", "+0987654321", 18, 3, 2, "2023-01-02", 7f, 1f, 1f, 8f),
            AttendanceRecord("Charlie Brown", "charlie.brown@example.com", "+1122334455", 22, 1, 0, "2023-01-03", 8f, 0f, 0f, 8f),
            AttendanceRecord("Diana Prince", "diana.prince@example.com", "+5566778899", 15, 5, 2, "2023-01-04", 6f, 2f, 1f, 9f),
            AttendanceRecord("Eve Adams", "eve.adams@example.com", "+6677889900", 25, 0, 0, "2023-01-05", 8f, 0f, 0f, 8f),
            AttendanceRecord("Frank Castle", "frank.castle@example.com", "+7788990011", 17, 4, 1, "2023-01-06", 7f, 1f, 0f, 8f),
            AttendanceRecord("Grace Kelly", "grace.kelly@example.com", "+8899002233", 21, 2, 1, "2023-01-07", 8f, 0f, 2f, 10f),
            AttendanceRecord("Henry Ford", "henry.ford@example.com", "+9900112233", 19, 3, 1, "2023-01-08", 7f, 1f, 1f, 8f)
        )

        // Filter records based on the provided date
        return allRecords.filter { it.date == date }
    }
    private fun loadDetailedAttendanceData() {
        // Load detailed attendance data
        _detailedAttendanceData.value = listOf(
            DetailedAttendanceRecord("2023-01-01", "Monday", 8f, 0f, 2f, 10f),
            DetailedAttendanceRecord("2023-01-02", "Tuesday", 7f, 1f, 1f, 8f),
            DetailedAttendanceRecord("2023-01-03", "Wednesday", 6f, 2f, 1f, 7f),
            DetailedAttendanceRecord("2023-01-04", "Thursday", 8f, 0f, 0f, 8f),
            DetailedAttendanceRecord("2023-01-05", "Friday", 9f, 0f, 1f, 10f),
            DetailedAttendanceRecord("2023-01-06", "Saturday", 0f, 8f, 0f, 8f),
            DetailedAttendanceRecord("2023-01-07", "Sunday", 0f, 8f, 0f, 8f)
        )
    }

    private fun computeWeeklySummary() {
        _detailedAttendanceData.value?.let { records ->
            val totalWorked = records.fold(0f) { acc, record -> acc + record.hoursWorked }
            val totalMissed = records.fold(0f) { acc, record -> acc + record.hoursMissed }
            val totalOvertime = records.fold(0f) { acc, record -> acc + record.overtime }
            val totalRequired = records.fold(0f) { acc, record -> acc + record.requiredHours }

            // Update WeeklySummary LiveData
            _weeklySummaryData.value = WeeklySummary(
                totalWorked = totalWorked,
                totalMissed = totalMissed,
                totalOvertime = totalOvertime,
                totalRequired = totalRequired
            )
        }
    }
}

