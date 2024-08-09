package com.example.aimsoftattendance

data class AttendanceRecord(
    var name: String,          // Changed to var for two-way binding
    var email: String,         // Added email property
    var phone: String,         // Added phone property
    val attended: Int,
    val absent: Int,
    val late: Int,
    val date: String,
    val attendedHours: Float,
    val missedHours: Float,
    val overtimeHours: Float,
    val workedHours: Float
)
