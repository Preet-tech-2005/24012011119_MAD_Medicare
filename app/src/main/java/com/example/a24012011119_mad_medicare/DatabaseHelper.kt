package com.example.a24012011119_mad_medicare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "MediCare.db"
        private const val DATABASE_VERSION = 2

        // Medicine Table
        private const val TABLE_MEDICINES = "medicines"

        private const val ID = "id"
        private const val MEDICINE_NAME = "medicine_name"
        private const val DOSAGE = "dosage"
        private const val START_DATE = "start_date"
        private const val END_DATE = "end_date"
        private const val REMINDER_TIME = "reminder_time"
        private const val FREQUENCY = "frequency"
        private const val NOTES = "notes"

        // Prescription Table
        private const val TABLE_PRESCRIPTION = "prescription"

        private const val PRESCRIPTION_ID = "id"
        private const val DOCTOR_NAME = "doctor_name"
        private const val PRESCRIPTION_DATE = "prescription_date"
        private const val DIAGNOSIS = "diagnosis"
        private const val PRESCRIPTION_DETAILS = "prescription_details"
    }

    override fun onCreate(db: SQLiteDatabase) {

        // Medicine Table

        val medicineQuery = """
            CREATE TABLE $TABLE_MEDICINES (
                $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $MEDICINE_NAME TEXT,
                $DOSAGE TEXT,
                $START_DATE TEXT,
                $END_DATE TEXT,
                $REMINDER_TIME TEXT,
                $FREQUENCY TEXT,
                $NOTES TEXT
            )
        """.trimIndent()

        db.execSQL(medicineQuery)


        // Prescription Table

        val prescriptionQuery = """
            CREATE TABLE $TABLE_PRESCRIPTION (
                $PRESCRIPTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $DOCTOR_NAME TEXT,
                $PRESCRIPTION_DATE TEXT,
                $DIAGNOSIS TEXT,
                $PRESCRIPTION_DETAILS TEXT
            )
        """.trimIndent()

        db.execSQL(prescriptionQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        if (oldVersion < 2) {

            val prescriptionQuery = """
                CREATE TABLE IF NOT EXISTS $TABLE_PRESCRIPTION (
                    $PRESCRIPTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $DOCTOR_NAME TEXT,
                    $PRESCRIPTION_DATE TEXT,
                    $DIAGNOSIS TEXT,
                    $PRESCRIPTION_DETAILS TEXT
                )
            """.trimIndent()

            db.execSQL(prescriptionQuery)
        }
    }

    // ---------------- MEDICINE ----------------

    fun insertMedicine(
        name: String,
        dosage: String,
        startDate: String,
        endDate: String,
        reminderTime: String,
        frequency: String,
        notes: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(MEDICINE_NAME, name)
        values.put(DOSAGE, dosage)
        values.put(START_DATE, startDate)
        values.put(END_DATE, endDate)
        values.put(REMINDER_TIME, reminderTime)
        values.put(FREQUENCY, frequency)
        values.put(NOTES, notes)

        val result = db.insert(
            TABLE_MEDICINES,
            null,
            values
        )

        return result != -1L
    }

    fun getAllMedicines(): Cursor {

        val db = readableDatabase

        return db.rawQuery(
            "SELECT * FROM $TABLE_MEDICINES ORDER BY $ID DESC",
            null
        )
    }

    // ---------------- PRESCRIPTION ----------------

    fun insertPrescription(
        doctorName: String,
        date: String,
        diagnosis: String,
        details: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(DOCTOR_NAME, doctorName)
        values.put(PRESCRIPTION_DATE, date)
        values.put(DIAGNOSIS, diagnosis)
        values.put(PRESCRIPTION_DETAILS, details)

        val result = db.insert(
            TABLE_PRESCRIPTION,
            null,
            values
        )

        return result != -1L
    }

    fun getLatestPrescription(): Cursor {

        val db = readableDatabase

        return db.rawQuery(
            "SELECT * FROM $TABLE_PRESCRIPTION ORDER BY $PRESCRIPTION_ID DESC LIMIT 1",
            null
        )
    }
}