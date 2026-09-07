package com.example.a24012011119_mad_medicare

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddMedicineActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView

    private lateinit var edtMedicineName: TextInputEditText
    private lateinit var edtDosage: TextInputEditText
    private lateinit var edtStartDate: TextInputEditText
    private lateinit var edtEndDate: TextInputEditText
    private lateinit var edtReminderTime: TextInputEditText
    private lateinit var edtFrequency: TextInputEditText
    private lateinit var edtNotes: TextInputEditText

    private lateinit var btnSaveMedicine: MaterialButton

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_medicine)

        // Database
        databaseHelper = DatabaseHelper(this)

        // Find views
        txtBack = findViewById(R.id.txtBack)

        edtMedicineName = findViewById(R.id.edtMedicineName)
        edtDosage = findViewById(R.id.edtDosage)
        edtStartDate = findViewById(R.id.edtStartDate)
        edtEndDate = findViewById(R.id.edtEndDate)
        edtReminderTime = findViewById(R.id.edtReminderTime)
        edtFrequency = findViewById(R.id.edtFrequency)
        edtNotes = findViewById(R.id.edtNotes)

        btnSaveMedicine = findViewById(R.id.btnSaveMedicine)

        // Back button
        txtBack.setOnClickListener {
            finish()
        }

        // Start Date
        edtStartDate.setOnClickListener {
            showDatePicker(edtStartDate)
        }

        // End Date
        edtEndDate.setOnClickListener {
            showDatePicker(edtEndDate)
        }

        // Reminder Time
        edtReminderTime.setOnClickListener {
            showTimePicker()
        }

        // Save Medicine
        btnSaveMedicine.setOnClickListener {
            saveMedicine()
        }
    }

    private fun showDatePicker(editText: TextInputEditText) {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->

                val date = Calendar.getInstance()

                date.set(year, month, day)

                val format =
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    )

                editText.setText(
                    format.format(date.time)
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {

        val calendar = Calendar.getInstance()

        TimePickerDialog(
            this,
            { _, hour, minute ->

                val time = Calendar.getInstance()

                time.set(
                    Calendar.HOUR_OF_DAY,
                    hour
                )

                time.set(
                    Calendar.MINUTE,
                    minute
                )

                val format =
                    SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    )

                edtReminderTime.setText(
                    format.format(time.time)
                )
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun saveMedicine() {

        val name =
            edtMedicineName.text.toString().trim()

        val dosage =
            edtDosage.text.toString().trim()

        val startDate =
            edtStartDate.text.toString().trim()

        val endDate =
            edtEndDate.text.toString().trim()

        val reminderTime =
            edtReminderTime.text.toString().trim()

        val frequency =
            edtFrequency.text.toString().trim()

        val notes =
            edtNotes.text.toString().trim()

        // Validation
        if (name.isEmpty()) {
            edtMedicineName.error = "Enter medicine name"
            edtMedicineName.requestFocus()
            return
        }

        if (dosage.isEmpty()) {
            edtDosage.error = "Enter dosage"
            edtDosage.requestFocus()
            return
        }

        if (startDate.isEmpty()) {
            edtStartDate.error = "Select start date"
            return
        }

        if (endDate.isEmpty()) {
            edtEndDate.error = "Select end date"
            return
        }

        if (reminderTime.isEmpty()) {
            edtReminderTime.error = "Select reminder time"
            return
        }

        if (frequency.isEmpty()) {
            edtFrequency.error = "Enter frequency"
            edtFrequency.requestFocus()
            return
        }

        // Insert into SQLite
        val inserted = databaseHelper.insertMedicine(
            name,
            dosage,
            startDate,
            endDate,
            reminderTime,
            frequency,
            notes
        )

        if (inserted) {

            Toast.makeText(
                this,
                "Medicine Added Successfully",
                Toast.LENGTH_SHORT
            ).show()

            // Go back to My Medicines
            finish()

        } else {

            Toast.makeText(
                this,
                "Failed to add medicine",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        databaseHelper.close()
        super.onDestroy()
    }
}