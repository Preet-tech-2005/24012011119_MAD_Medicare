package com.example.a24012011119_mad_medicare

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PrescriptionActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView

    private lateinit var edtDoctorName: TextInputEditText
    private lateinit var edtDisease: TextInputEditText
    private lateinit var edtPrescriptionDate: TextInputEditText

    private lateinit var edtMedicineName: TextInputEditText
    private lateinit var edtDosage: TextInputEditText
    private lateinit var edtFrequency: TextInputEditText
    private lateinit var edtDuration: TextInputEditText

    private lateinit var edtReminderTime1: TextInputEditText
    private lateinit var edtReminderTime2: TextInputEditText
    private lateinit var edtReminderTime3: TextInputEditText

    private lateinit var txtAddedMedicines: TextView

    private lateinit var btnAddMedicine: MaterialButton
    private lateinit var btnSavePrescription: MaterialButton

    private lateinit var databaseHelper: DatabaseHelper

    private val medicines =
        mutableListOf<PrescriptionMedicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_prescription
        )

        databaseHelper =
            DatabaseHelper(this)

        txtBack =
            findViewById(R.id.txtBack)

        edtDoctorName =
            findViewById(R.id.edtDoctorName)

        edtDisease =
            findViewById(R.id.edtDisease)

        edtPrescriptionDate =
            findViewById(R.id.edtPrescriptionDate)

        edtMedicineName =
            findViewById(R.id.edtMedicineName)

        edtDosage =
            findViewById(R.id.edtDosage)

        edtFrequency =
            findViewById(R.id.edtFrequency)

        edtDuration =
            findViewById(R.id.edtDuration)

        edtReminderTime1 =
            findViewById(R.id.edtReminderTime1)

        edtReminderTime2 =
            findViewById(R.id.edtReminderTime2)

        edtReminderTime3 =
            findViewById(R.id.edtReminderTime3)

        txtAddedMedicines =
            findViewById(R.id.txtAddedMedicines)

        btnAddMedicine =
            findViewById(R.id.btnAddMedicine)

        btnSavePrescription =
            findViewById(R.id.btnSavePrescription)

        txtBack.setOnClickListener {
            finish()
        }

        setupDatePicker()

        setupTimePicker(edtReminderTime1)
        setupTimePicker(edtReminderTime2)
        setupTimePicker(edtReminderTime3)

        setupFrequency()

        btnAddMedicine.setOnClickListener {
            addMedicine()
        }

        btnSavePrescription.setOnClickListener {
            savePrescription()
        }
    }

    private fun setupDatePicker() {

        edtPrescriptionDate.setOnClickListener {

            val calendar =
                Calendar.getInstance()

            val year =
                calendar.get(Calendar.YEAR)

            val month =
                calendar.get(Calendar.MONTH)

            val day =
                calendar.get(Calendar.DAY_OF_MONTH)

            val dialog =
                android.app.DatePickerDialog(
                    this,
                    { _, selectedYear,
                      selectedMonth,
                      selectedDay ->

                        val date =
                            "$selectedDay/${selectedMonth + 1}/$selectedYear"

                        edtPrescriptionDate.setText(
                            date
                        )
                    },
                    year,
                    month,
                    day
                )

            dialog.show()
        }
    }

    private fun setupTimePicker(
        editText: TextInputEditText
    ) {

        editText.setOnClickListener {

            val calendar =
                Calendar.getInstance()

            val hour =
                calendar.get(Calendar.HOUR_OF_DAY)

            val minute =
                calendar.get(Calendar.MINUTE)

            val dialog =
                TimePickerDialog(
                    this,
                    { _, selectedHour,
                      selectedMinute ->

                        val amPm =
                            if (selectedHour >= 12) {
                                "PM"
                            } else {
                                "AM"
                            }

                        var displayHour =
                            selectedHour % 12

                        if (displayHour == 0) {
                            displayHour = 12
                        }

                        val time =
                            String.format(
                                "%02d:%02d %s",
                                displayHour,
                                selectedMinute,
                                amPm
                            )

                        editText.setText(time)
                    },
                    hour,
                    minute,
                    false
                )

            dialog.show()
        }
    }

    private fun setupFrequency() {

        edtFrequency.addTextChangedListener(
            object : android.text.TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    updateReminderFields(
                        s.toString()
                    )
                }

                override fun afterTextChanged(
                    s: android.text.Editable?
                ) {
                }
            }
        )
    }

    private fun updateReminderFields(
        frequency: String
    ) {

        val value =
            frequency.lowercase().trim()

        val frequencyCount =
            getFrequencyCount(value)

        if (frequencyCount >= 2) {

            edtReminderTime2.visibility =
                View.VISIBLE

        } else {

            edtReminderTime2.visibility =
                View.GONE

            edtReminderTime2.text = null
        }

        if (frequencyCount >= 3) {

            edtReminderTime3.visibility =
                View.VISIBLE

        } else {

            edtReminderTime3.visibility =
                View.GONE

            edtReminderTime3.text = null
        }
    }

    private fun getFrequencyCount(
        frequency: String
    ): Int {

        return when {

            frequency.contains("thrice") ||
                    frequency.contains("three") ||
                    frequency.contains("3") -> {
                3
            }

            frequency.contains("twice") ||
                    frequency.contains("two") ||
                    frequency.contains("2") -> {
                2
            }

            else -> {
                1
            }
        }
    }

    private fun addMedicine() {

        val medicineName =
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

        // Validation

        if (medicineName.isEmpty()) {

            edtMedicineName.error =
                "Enter medicine name"

            return
        }

        if (dosage.isEmpty()) {

            edtDosage.error =
                "Enter dosage"

            return
        }

        if (frequency.isEmpty()) {

            edtFrequency.error =
                "Enter frequency"

            return
        }

        if (durationText.isEmpty()) {

            edtDuration.error =
                "Enter duration"

            return
        }

        if (time1.isEmpty()) {

            edtReminderTime1.error =
                "Select reminder time"

            return
        }

        val durationDays =
            durationText.toIntOrNull()

        if (
            durationDays == null ||
            durationDays <= 0
        ) {

            edtDuration.error =
                "Enter valid number of days"

            return
        }

        val frequencyCount =
            getFrequencyCount(
                frequency.lowercase()
            )

        if (
            frequencyCount >= 2 &&
            time2.isEmpty()
        ) {

            edtReminderTime2.error =
                "Select reminder time"

            return
        }

        if (
            frequencyCount >= 3 &&
            time3.isEmpty()
        ) {

            edtReminderTime3.error =
                "Select reminder time"

            return
        }

        // Create reminder time string

        var reminderTimes =
            time1

        if (frequencyCount >= 2) {

            reminderTimes =
                "$reminderTimes|$time2"
        }

        if (frequencyCount >= 3) {

            reminderTimes =
                "$reminderTimes|$time3"
        }

        // Create medicine object

        val medicine =
            PrescriptionMedicine(
                medicineName =
                    medicineName,

                dosage =
                    dosage,

                frequency =
                    frequency,

                frequencyCount =
                    frequencyCount,

                durationDays =
                    durationDays,

                reminderTimes =
                    reminderTimes,

                takenCount =
                    0
            )

        // IMPORTANT:
        // Add medicine to list

        medicines.add(
            medicine
        )

        // Update displayed list

        updateMedicineList()

        // Show count

        Toast.makeText(
            this,
            "Medicine ${medicines.size} added",
            Toast.LENGTH_SHORT
        ).show()

        // Clear only medicine form

        clearMedicineFields()
    }

    private fun clearMedicineFields() {

        edtMedicineName.text = null
        edtDosage.text = null
        edtFrequency.text = null
        edtDuration.text = null

        edtReminderTime1.text = null
        edtReminderTime2.text = null
        edtReminderTime3.text = null

        edtReminderTime2.visibility =
            View.GONE

        edtReminderTime3.visibility =
            View.GONE
    }

    private fun updateMedicineList() {

        var text =
            "Added Medicines:\n\n"

        for (i in medicines.indices) {

            val medicine =
                medicines[i]

            text +=
                "${i + 1}. ${medicine.medicineName}\n"

            text +=
                "Dosage: ${medicine.dosage}\n"

            text +=
                "Frequency: ${medicine.frequency}\n"

            text +=
                "Duration: ${medicine.durationDays} days\n"

            text +=
                "Reminder: " +
                        medicine.reminderTimes
                            .replace(
                                "|",
                                ", "
                            )

            text += "\n\n"
        }

        txtAddedMedicines.text =
            text
    }

    private fun savePrescription() {

        val doctorName =
            edtDoctorName.text.toString().trim()

        val disease =
            edtDisease.text.toString().trim()

        val prescriptionDate =
            edtPrescriptionDate.text.toString().trim()

        if (doctorName.isEmpty()) {

            edtDoctorName.error =
                "Enter doctor name"

            return
        }

        if (disease.isEmpty()) {

            edtDisease.error =
                "Enter disease or diagnosis"

            return
        }

        if (prescriptionDate.isEmpty()) {

            edtPrescriptionDate.error =
                "Select prescription date"

            return
        }

        if (medicines.isEmpty()) {

            Toast.makeText(
                this,
                "Add at least one medicine",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Save ALL medicines

        val prescriptionId =
            databaseHelper.insertPrescription(
                doctorName,
                prescriptionDate,
                disease,
                medicines
            )

        if (prescriptionId != -1L) {

            // Set alarm for every medicine

            for (medicine in medicines) {

                setMedicineAlarms(
                    medicine
                )
            }

            Toast.makeText(
                this,
                "${medicines.size} medicines saved successfully",
                Toast.LENGTH_LONG
            ).show()

            val intent =
                Intent(
                    this,
                    PrescriptionDetailsActivity::class.java
                )

            intent.putExtra(
                "prescription_id",
                prescriptionId
            )

            startActivity(intent)

            finish()

        } else {

            Toast.makeText(
                this,
                "Failed to save prescription",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setMedicineAlarms(
        medicine: PrescriptionMedicine
    ) {

        val times =
            medicine.reminderTimes.split("|")

        for (time in times) {

            setSingleMedicineAlarm(
                medicine,
                time
            )
        }
    }

    private fun setSingleMedicineAlarm(
        medicine: PrescriptionMedicine,
        time: String
    ) {

        try {

            val parts =
                time.split(" ")

            if (parts.size < 2) {
                return
            }

            val timeParts =
                parts[0].split(":")

            var hour =
                timeParts[0].toInt()

            val minute =
                timeParts[1].toInt()

            val amPm =
                parts[1]

            if (
                amPm.equals(
                    "PM",
                    ignoreCase = true
                ) &&
                hour != 12
            ) {

                hour += 12
            }

            if (
                amPm.equals(
                    "AM",
                    ignoreCase = true
                ) &&
                hour == 12
            ) {

                hour = 0
            }

            val calendar =
                Calendar.getInstance()

            calendar.set(
                Calendar.HOUR_OF_DAY,
                hour
            )

            calendar.set(
                Calendar.MINUTE,
                minute
            )

            calendar.set(
                Calendar.SECOND,
                0
            )

            calendar.set(
                Calendar.MILLISECOND,
                0
            )

            if (
                calendar.timeInMillis <=
                System.currentTimeMillis()
            ) {

                calendar.add(
                    Calendar.DAY_OF_YEAR,
                    1
                )
            }

            val alarmManager =
                getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            val intent =
                Intent(
                    this,
                    AlarmReceiver::class.java
                )

            intent.putExtra(
                "medicine_name",
                medicine.medicineName
            )

            val requestCode =
                (
                        medicine.medicineName +
                                "_" +
                                time
                        ).hashCode()

            val pendingIntent =
                PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Invalid reminder time",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {

        databaseHelper.close()

        super.onDestroy()
    }
}