package com.example.a24012011119_mad_medicare

import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

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

    private lateinit var btnTaken: MaterialButton

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

        // Get prescription ID
        prescriptionId =
            intent.getLongExtra(
                "prescription_id",
                0
            )

        // Find views
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


        // Back
        txtBack.setOnClickListener {

            finish()
        }


        loadPrescription()


        // Mark medicine as taken
        btnTaken.setOnClickListener {

            markMedicineTaken()
        }
    }


    private fun loadPrescription() {

        // Get prescription
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


        // Get medicines
        val medicineCursor: Cursor =
            databaseHelper.getPrescriptionMedicines(
                prescriptionId
            )


        if (medicineCursor.moveToFirst()) {

            medicineId =
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

            durationDays =
                medicineCursor.getInt(
                    medicineCursor
                        .getColumnIndexOrThrow(
                            "duration_days"
                        )
                )

            takenCount =
                medicineCursor.getInt(
                    medicineCursor
                        .getColumnIndexOrThrow(
                            "taken_count"
                        )
                )


            txtMedicineName.text =
                medicineName

            txtDosage.text =
                "Dosage: $dosage"

            txtFrequency.text =
                "Frequency: $frequency"

            txtDuration.text =
                "Duration: $durationDays days"


            updateProgress()
        }

        medicineCursor.close()
    }


    private fun markMedicineTaken() {

        if (takenCount >= durationDays) {

            Toast.makeText(
                this,
                "Medicine course completed",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        takenCount++


        databaseHelper.updateTakenCount(
            medicineId,
            takenCount
        )


        updateProgress()


        if (takenCount >= durationDays) {

            Toast.makeText(
                this,
                "Medicine course completed!",
                Toast.LENGTH_LONG
            ).show()

        } else {

            Toast.makeText(
                this,
                "Medicine marked as taken",
                Toast.LENGTH_SHORT
            ).show()
        }
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
        }
    }


    override fun onDestroy() {

        databaseHelper.close()

        super.onDestroy()
    }
}