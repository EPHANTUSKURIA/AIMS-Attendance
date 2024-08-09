package com.example.aimsoftattendance

data class WeeklySummary(
    val totalWorked: Float,
    val totalMissed: Float,
    val totalOvertime: Float,
    val totalRequired: Float
)