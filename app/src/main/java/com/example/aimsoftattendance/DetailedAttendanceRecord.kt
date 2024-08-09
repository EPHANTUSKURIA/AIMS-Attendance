package com.example.aimsoftattendance

data class DetailedAttendanceRecord(
    val date: String,
    val day: String,
    val hoursWorked: Float,
    val hoursMissed: Float,
    val overtime: Float,
    val requiredHours: Float
)