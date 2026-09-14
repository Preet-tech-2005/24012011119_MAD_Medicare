package com.example.a24012011119_mad_medicare

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PrescriptionActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView

    private lateinit var edtDoctorName: EditText
    private lateinit var edtDisease: EditText
    private lateinit var edtPrescriptionDate: EditText

    private lateinit var edtMedicineName: EditText
    private lateinit var edtDosage: EditText
    private lateinit var edtFrequency: EditText
    private lateinit var edtDuration: EditText

    private lateinit var edtReminderTime1: EditText
    private lateinit var edtReminderTime2: EditText
    private lateinit var edtReminderTime3: EditText

    private lateinit var btnAddMedicine: MaterialButton
    private lateinit var btnSavePrescription: MaterialButton

    private lateinit var txtAddedMedicines: TextView

    private lateinit var databaseHelper: DatabaseHelper

    private val medicines =
        mutableListOf<PrescriptionMedicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prescription)

        databaseHelper =
            DatabaseHelper(this)

        // Back
        txtBack =
            findViewById(R.id.txtBack)

        // Prescription details
        edtDoctorName =
            findViewById(R.id.edtDoctorName)

        edtDisease =
            findViewById(R.id.edtDisease)

        edtPrescriptionDate =
            findViewById(R.id.edtPrescriptionDate)

        // Medicine details
        edtMedicineName =
            findViewById(R.id.edtMedicineName)

        edtDosage =
            findViewById(R.id.edtDosage)

        edtFrequency =
            findViewById(R.id.edtFrequency)

        edtDuration =
            findViewById(R.id.edtDuration)

        // Reminder times
        edtReminderTime1 =
            findViewById(R.id.edtReminderTime1)

        edtReminderTime2 =
            findViewById(R.id.edtReminderTime2)

        edtReminderTime3 =
            findViewById(R.id.edtReminderTime3)

        // Buttons
        btnAddMedicine =
            findViewById(R.id.btnAddMedicine)

        btnSavePrescription =
            findViewById(R.id.btnSavePrescription)

        // Added medicine list
        txtAddedMedicines =
            findViewById(R.id.txtAddedMedicines)

        // Back button
        txtBack.setOnClickListener {
            finish()
        }

        // Add medicine
        btnAddMedicine.setOnClickListener {
            addMedicine()
        }

        // Save prescription
        btnSavePrescription.setOnClickListener {
            savePrescription()
        }

        // Date field
        edtPrescriptionDate.setOnClickListener {
            showDatePicker()
        }

        // Reminder time fields
        edtReminderTime1.setOnClickListener {
            showTimePicker(edtReminderTime1)
        }

        edtReminderTime2.setOnClickListener {
            showTimePicker(edtReminderTime2)
        }

        edtReminderTime3.setOnClickListener {
            showTimePicker(edtReminderTime3)
        }

        updateMedicineList()
    }

    private fun addMedicine() {

        val name =
            edtMedicineName.text.toString().trim()

        val dosage =
            edtDosage.text.toString().trim()

        val frequency =
            edtFrequency.text.toString().trim()

        val durationText =
            edtDuration.text.toString().trim()

        val time1 =
            edtReminderTime1.text.toString().trim()

        val time2 =
            edtReminderTime2.text.toString().trim()

        val time3 =
            edtReminderTime3.text.toString().trim()

        if (
            name.isEmpty() ||
            dosage.isEmpty() ||
            frequency.isEmpty() ||
            durationText.isEmpty() ||
            time1.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Please fill medicine details",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val duration =
            durationText.toIntOrNull()

        if (
            duration == null ||
            duration <= 0
        ) {

            Toast.makeText(
                this,
                "Enter valid duration",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val frequencyCount =
            when {

                frequency.equals(
                    "Once daily",
                    true
                ) -> 1

                frequency.equals(
                    "Twice daily",
                    true
                ) -> 2

                frequency.equals(
                    "Thrice daily",
                    true
                ) -> 3

                else -> 1
            }

        // Create reminder time string
        var reminderTimes =
            time1

        if (
            frequencyCount >= 2 &&
            time2.isNotEmpty()
        ) {

            reminderTimes +=
                "|$time2"
        }

        if (
            frequencyCount >= 3 &&
            time3.isNotEmpty()
        ) {

            reminderTimes +=
                "|$time3"
        }

        val medicine =
            PrescriptionMedicine(
                medicineName = name,
                dosage = dosage,
                frequency = frequency,
                frequencyCount = frequencyCount,
                durationDays = duration,
                reminderTimes = reminderTimes
            )

        // Add medicine to list
        medicines.add(medicine)

        Toast.makeText(
            this,
            "$name added",
            Toast.LENGTH_SHORT
        ).show()

        // Show all medicines
        updateMedicineList()

        // Clear only medicine fields
        clearMedicineFields()
    }

    private fun updateMedicineList() {

        if (medicines.isEmpty()) {

            txtAddedMedicines.text =
                "Added Medicines:"

            return
        }

        val text =
            StringBuilder()

        text.append("Added Medicines:\n\n")

        for (i in medicines.indices) {

            val medicine =
                medicines[i]

            text.append(
                "${i + 1}. ${medicine.medicineName}\n"
            )

            text.append(
                "   Dosage: ${medicine.dosage}\n"
            )

            text.append(
                "   Frequency: ${medicine.frequency}\n"
            )

            text.append(
                "   Duration: ${medicine.durationDays} days\n"
            )

            text.append(
                "   Reminder: ${
                    medicine.reminderTimes
                        .replace("|", ", ")
                }\n\n"
            )
        }

        txtAddedMedicines.text =
            text.toString()
    }

    private fun clearMedicineFields() {

        edtMedicineName.text.clear()
        edtDosage.text.clear()
        edtFrequency.text.clear()
        edtDuration.text.clear()

        edtReminderTime1.text.clear()
        edtReminderTime2.text.clear()
        edtReminderTime3.text.clear()

        // Hide extra reminder fields
        edtReminderTime2.visibility =
            android.view.View.GONE

        edtReminderTime3.visibility =
            android.view.View.GONE
    }

    private fun savePrescription() {

        val doctor =
            edtDoctorName.text.toString().trim()

        val disease =
            edtDisease.text.toString().trim()

        val date =
            edtPrescriptionDate.text.toString().trim()

        if (
            doctor.isEmpty() ||
            disease.isEmpty() ||
            date.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Please fill prescription details",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (medicines.isEmpty()) {

            Toast.makeText(
                this,
                "Please add at least one medicine",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Save prescription and all medicines
        val prescriptionId =
            databaseHelper.insertPrescription(
                doctor,
                date,
                disease,
                medicines
            )

        if (prescriptionId != -1L) {

            // Set notification alarms
            setMedicineAlarms()

            Toast.makeText(
                this,
                "${medicines.size} medicines saved and reminders set",
                Toast.LENGTH_LONG
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "Failed to save prescription",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setMedicineAlarms() {

        for (medicine in medicines) {

            val times =
                medicine.reminderTimes.split("|")

            for (time in times) {

                setAlarm(
                    medicine.medicineName,
                    time
                )
            }
        }
    }

    private fun setAlarm(
        medicineName: String,
        time: String
    ) {

        try {

            val timeFormat =
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                )

            val date =
                timeFormat.parse(time)

            if (date == null) {
                return
            }

            val calendar =
                Calendar.getInstance()

            val parsedCalendar =
                Calendar.getInstance()

            parsedCalendar.time =
                date

            calendar.set(
                Calendar.HOUR_OF_DAY,
                parsedCalendar.get(
                    Calendar.HOUR_OF_DAY
                )
            )

            calendar.set(
                Calendar.MINUTE,
                parsedCalendar.get(
                    Calendar.MINUTE
                )
            )

            calendar.set(
                Calendar.SECOND,
                0
            )

            calendar.set(
                Calendar.MILLISECOND,
                0
            )

            // If time has already passed,
            // schedule it for tomorrow.
            if (
                calendar.timeInMillis <=
                System.currentTimeMillis()
            ) {

                calendar.add(
                    Calendar.DAY_OF_YEAR,
                    1
                )
            }

            val intent =
                Intent(
                    this,
                    AlarmReceiver::class.java
                )

            intent.putExtra(
                "medicine_name",
                medicineName
            )

            val requestCode =
                medicineName.hashCode() +
                        time.hashCode()

            val pendingIntent =
                PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            val alarmManager =
                getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            // Schedule notification
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Alarm error: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showDatePicker() {

        val calendar =
            Calendar.getInstance()

        val year =
            calendar.get(Calendar.YEAR)

        val month =
            calendar.get(Calendar.MONTH)

        val day =
            calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker =
            android.app.DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val selectedDate =
                        String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear
                        )

                    edtPrescriptionDate.setText(
                        selectedDate
                    )
                },
                year,
                month,
                day
            )

        datePicker.show()
    }

    private fun showTimePicker(
        editText: EditText
    ) {

        val calendar =
            Calendar.getInstance()

        val hour =
            calendar.get(Calendar.HOUR_OF_DAY)

        val minute =
            calendar.get(Calendar.MINUTE)

        val timePicker =
            android.app.TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->

                    val selectedCalendar =
                        Calendar.getInstance()

                    selectedCalendar.set(
                        Calendar.HOUR_OF_DAY,
                        selectedHour
                    )

                    selectedCalendar.set(
                        Calendar.MINUTE,
                        selectedMinute
                    )

                    val timeFormat =
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                        )

                    editText.setText(
                        timeFormat.format(
                            selectedCalendar.time
                        )
                    )
                },
                hour,
                minute,
                false
            )

        timePicker.show()
    }

    override fun onDestroy() {

        databaseHelper.close()

        super.onDestroy()
    }
}