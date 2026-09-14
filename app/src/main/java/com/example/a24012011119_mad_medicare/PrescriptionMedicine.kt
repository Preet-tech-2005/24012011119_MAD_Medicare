package com.example.a24012011119_mad_medicare

data class PrescriptionMedicine(
    var id: Long = 0,
    val medicineName: String,
    val dosage: String,
    val frequency: String,
    val frequencyCount: Int,
    val durationDays: Int,
    val reminderTimes: String,
    var takenCount: Int = 0
)