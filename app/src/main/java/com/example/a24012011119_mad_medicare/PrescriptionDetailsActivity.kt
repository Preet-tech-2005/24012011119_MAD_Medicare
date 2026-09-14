package com.example.a24012011119_mad_medicare

import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PrescriptionDetailsActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView

    private lateinit var txtDoctorName: TextView
    private lateinit var txtPrescriptionDate: TextView
    private lateinit var txtDiagnosis: TextView

    private lateinit var txtMedicineName: TextView
    private lateinit var txtDosage: TextView
    private lateinit var txtFrequency: TextView
    private lateinit var txtDuration: TextView

    private lateinit var txtProgress: TextView
    private lateinit var txtRemaining: TextView

    private lateinit var btnTaken: com.google.android.material.button.MaterialButton

    private lateinit var databaseHelper: DatabaseHelper

    private var prescriptionId: Long = 0

    private var medicineId: Long = 0

    private var durationDays: Int = 0

    private var takenCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_prescription_details
        )

        databaseHelper =
            DatabaseHelper(this)

        prescriptionId =
            intent.getLongExtra(
                "prescription_id",
                0
            )

        txtBack =
            findViewById(R.id.txtBack)

        txtDoctorName =
            findViewById(R.id.txtDoctorName)

        txtPrescriptionDate =
            findViewById(R.id.txtPrescriptionDate)

        txtDiagnosis =
            findViewById(R.id.txtDiagnosis)

        txtMedicineName =
            findViewById(R.id.txtMedicineName)

        txtDosage =
            findViewById(R.id.txtDosage)

        txtFrequency =
            findViewById(R.id.txtFrequency)

        txtDuration =
            findViewById(R.id.txtDuration)

        txtProgress =
            findViewById(R.id.txtProgress)

        txtRemaining =
            findViewById(R.id.txtRemaining)

        btnTaken =
            findViewById(R.id.btnTaken)

        txtBack.setOnClickListener {
            finish()
        }

        loadPrescription()

        btnTaken.setOnClickListener {
            markMedicineTaken()
        }
    }

    private fun loadPrescription() {

        // Load prescription information

        val prescriptionCursor: Cursor =
            databaseHelper.getPrescription(
                prescriptionId
            )

        if (prescriptionCursor.moveToFirst()) {

            val doctorName =
                prescriptionCursor.getString(
                    prescriptionCursor
                        .getColumnIndexOrThrow(
                            "doctor_name"
                        )
                )

            val disease =
                prescriptionCursor.getString(
                    prescriptionCursor
                        .getColumnIndexOrThrow(
                            "disease"
                        )
                )

            val date =
                prescriptionCursor.getString(
                    prescriptionCursor
                        .getColumnIndexOrThrow(
                            "prescription_date"
                        )
                )

            txtDoctorName.text =
                "Doctor: $doctorName"

            txtDiagnosis.text =
                "Diagnosis: $disease"

            txtPrescriptionDate.text =
                "Date: $date"
        }

        prescriptionCursor.close()


        // Load ALL medicines

        val medicineCursor: Cursor =
            databaseHelper.getPrescriptionMedicines(
                prescriptionId
            )

        val medicineNames =
            StringBuilder()

        val dosages =
            StringBuilder()

        val frequencies =
            StringBuilder()

        val durations =
            StringBuilder()

        var number = 1

        if (medicineCursor.moveToFirst()) {

            do {

                val id =
                    medicineCursor.getLong(
                        medicineCursor
                            .getColumnIndexOrThrow(
                                "id"
                            )
                    )

                val medicineName =
                    medicineCursor.getString(
                        medicineCursor
                            .getColumnIndexOrThrow(
                                "medicine_name"
                            )
                    )

                val dosage =
                    medicineCursor.getString(
                        medicineCursor
                            .getColumnIndexOrThrow(
                                "dosage"
                            )
                    )

                val frequency =
                    medicineCursor.getString(
                        medicineCursor
                            .getColumnIndexOrThrow(
                                "frequency"
                            )
                    )

                val duration =
                    medicineCursor.getInt(
                        medicineCursor
                            .getColumnIndexOrThrow(
                                "duration_days"
                            )
                    )

                // Display all medicines

                medicineNames.append(
                    "$number. $medicineName\n"
                )

                dosages.append(
                    "$number. $dosage\n"
                )

                frequencies.append(
                    "$number. $frequency\n"
                )

                durations.append(
                    "$number. $duration days\n"
                )

                // Keep first medicine for tracker
                if (number == 1) {

                    medicineId = id

                    durationDays = duration

                    takenCount =
                        medicineCursor.getInt(
                            medicineCursor
                                .getColumnIndexOrThrow(
                                    "taken_count"
                                )
                        )
                }

                number++

            } while (
                medicineCursor.moveToNext()
            )
        }

        medicineCursor.close()


        txtMedicineName.text =
            medicineNames.toString()

        txtDosage.text =
            "Dosage:\n$dosages"

        txtFrequency.text =
            "Frequency:\n$frequencies"

        txtDuration.text =
            "Duration:\n$durations"

        updateProgress()
    }

    private fun markMedicineTaken() {

        if (takenCount >= durationDays) {

            return
        }

        takenCount++

        databaseHelper.updateTakenCount(
            medicineId,
            takenCount
        )

        updateProgress()
    }

    private fun updateProgress() {

        txtProgress.text =
            "$takenCount / $durationDays"

        val remaining =
            durationDays - takenCount

        txtRemaining.text =
            "Remaining: $remaining"

        if (takenCount >= durationDays) {

            btnTaken.text =
                "Completed ✓"

            btnTaken.isEnabled =
                false

        } else {

            btnTaken.text =
                "Mark as Taken"

            btnTaken.isEnabled =
                true
        }
    }

    override fun onDestroy() {

        databaseHelper.close()

        super.onDestroy()
    }
}