package com.example.a24012011119_mad_medicare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "medicare.db"
        private const val DATABASE_VERSION = 4
    }

    override fun onCreate(db: SQLiteDatabase) {

        // Prescription table
        db.execSQL(
            """
            CREATE TABLE prescriptions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                doctor_name TEXT,
                disease TEXT,
                prescription_date TEXT
            )
            """.trimIndent()
        )

        // Medicines inside each prescription
        db.execSQL(
            """
            CREATE TABLE prescription_medicines (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                prescription_id INTEGER,
                medicine_name TEXT,
                dosage TEXT,
                frequency TEXT,
                frequency_count INTEGER,
                duration_days INTEGER,
                reminder_times TEXT,
                taken_count INTEGER DEFAULT 0
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        if (oldVersion < 4) {

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS prescriptions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    doctor_name TEXT,
                    disease TEXT,
                    prescription_date TEXT
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS prescription_medicines (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    prescription_id INTEGER,
                    medicine_name TEXT,
                    dosage TEXT,
                    frequency TEXT,
                    frequency_count INTEGER,
                    duration_days INTEGER,
                    reminder_times TEXT,
                    taken_count INTEGER DEFAULT 0
                )
                """.trimIndent()
            )
        }
    }

    // Insert complete prescription with multiple medicines
    fun insertPrescription(
        doctorName: String,
        date: String,
        disease: String,
        medicines: MutableList<PrescriptionMedicine>
    ): Long {

        val db = writableDatabase

        db.beginTransaction()

        try {

            // Insert prescription
            val prescriptionValues = ContentValues()

            prescriptionValues.put(
                "doctor_name",
                doctorName
            )

            prescriptionValues.put(
                "disease",
                disease
            )

            prescriptionValues.put(
                "prescription_date",
                date
            )

            val prescriptionId =
                db.insert(
                    "prescriptions",
                    null,
                    prescriptionValues
                )

            if (prescriptionId == -1L) {
                return -1L
            }

            // Insert medicines
            for (medicine in medicines) {

                val medicineValues =
                    ContentValues()

                medicineValues.put(
                    "prescription_id",
                    prescriptionId
                )

                medicineValues.put(
                    "medicine_name",
                    medicine.medicineName
                )

                medicineValues.put(
                    "dosage",
                    medicine.dosage
                )

                medicineValues.put(
                    "frequency",
                    medicine.frequency
                )

                medicineValues.put(
                    "frequency_count",
                    medicine.frequencyCount
                )

                medicineValues.put(
                    "duration_days",
                    medicine.durationDays
                )

                medicineValues.put(
                    "reminder_times",
                    medicine.reminderTimes
                )

                medicineValues.put(
                    "taken_count",
                    0
                )

                val medicineId =
                    db.insert(
                        "prescription_medicines",
                        null,
                        medicineValues
                    )

                if (medicineId == -1L) {
                    return -1L
                }

                // Store generated database ID
                medicine.id = medicineId
            }

            db.setTransactionSuccessful()

            return prescriptionId

        } finally {

            db.endTransaction()
        }
    }

    // Get all prescriptions
    fun getAllPrescriptions(): Cursor {

        val db = readableDatabase

        return db.query(
            "prescriptions",
            null,
            null,
            null,
            null,
            null,
            "id DESC"
        )
    }

    // Get one prescription
    fun getPrescription(
        prescriptionId: Long
    ): Cursor {

        val db = readableDatabase

        return db.query(
            "prescriptions",
            null,
            "id = ?",
            arrayOf(
                prescriptionId.toString()
            ),
            null,
            null,
            null
        )
    }

    // Get medicines of a prescription
    fun getPrescriptionMedicines(
        prescriptionId: Long
    ): Cursor {

        val db = readableDatabase

        return db.query(
            "prescription_medicines",
            null,
            "prescription_id = ?",
            arrayOf(
                prescriptionId.toString()
            ),
            null,
            null,
            "id ASC"
        )
    }

    // Update medicine taken count
    fun updateTakenCount(
        medicineId: Long,
        takenCount: Int
    ) {

        val db = writableDatabase

        val values = ContentValues()

        values.put(
            "taken_count",
            takenCount
        )

        db.update(
            "prescription_medicines",
            values,
            "id = ?",
            arrayOf(
                medicineId.toString()
            )
        )
    }

    // Get medicine taken count
    fun getTakenCount(
        medicineId: Long
    ): Int {

        val db = readableDatabase

        val cursor =
            db.query(
                "prescription_medicines",
                arrayOf("taken_count"),
                "id = ?",
                arrayOf(
                    medicineId.toString()
                ),
                null,
                null,
                null
            )

        var count = 0

        if (cursor.moveToFirst()) {

            count =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "taken_count"
                    )
                )
        }

        cursor.close()

        return count
    }
}