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
        private const val DATABASE_VERSION = 1

        private const val TABLE_MEDICINES = "medicines"

        private const val ID = "id"
        private const val MEDICINE_NAME = "medicine_name"
        private const val DOSAGE = "dosage"
        private const val START_DATE = "start_date"
        private const val END_DATE = "end_date"
        private const val REMINDER_TIME = "reminder_time"
        private const val FREQUENCY = "frequency"
        private const val NOTES = "notes"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val query = """
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

        db.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEDICINES")
        onCreate(db)
    }

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

        db.close()

        return result != -1L
    }

    // Get all medicines
    fun getAllMedicines(): Cursor {

        val db = readableDatabase

        return db.rawQuery(
            "SELECT * FROM $TABLE_MEDICINES ORDER BY $ID DESC",
            null
        )
    }
}