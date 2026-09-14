package com.example.a24012011119_mad_medicare

import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PrescriptionDetailsActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView
    private lateinit var txtDoctorName: TextView
    private lateinit var txtPrescriptionDate: TextView
    private lateinit var txtDiagnosis: TextView

    private lateinit var recyclerPrescriptionMedicines: RecyclerView

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var medicineAdapter: PrescriptionMedicineAdapter

    private lateinit var medicineCursor: Cursor

    private var prescriptionId: Long = 0

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

        recyclerPrescriptionMedicines =
            findViewById(
                R.id.recyclerPrescriptionMedicines
            )

        // Back button
        txtBack.setOnClickListener {
            finish()
        }

        // RecyclerView
        recyclerPrescriptionMedicines.layoutManager =
            LinearLayoutManager(this)

        loadPrescription()

        loadMedicines()
    }

    private fun loadPrescription() {

        val cursor =
            databaseHelper.getPrescription(
                prescriptionId
            )

        if (cursor.moveToFirst()) {

            val doctor =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "doctor_name"
                    )
                )

            val disease =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "disease"
                    )
                )

            val date =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "prescription_date"
                    )
                )

            txtDoctorName.text =
                "Doctor: $doctor"

            txtDiagnosis.text =
                "Diagnosis: $disease"

            txtPrescriptionDate.text =
                "Prescription Date: $date"
        }

        cursor.close()
    }

    private fun loadMedicines() {

        medicineCursor =
            databaseHelper.getPrescriptionMedicines(
                prescriptionId
            )

        medicineAdapter =
            PrescriptionMedicineAdapter(
                medicineCursor,
                databaseHelper
            )

        recyclerPrescriptionMedicines.adapter =
            medicineAdapter
    }

    override fun onDestroy() {

        if (
            ::medicineCursor.isInitialized &&
            !medicineCursor.isClosed
        ) {
            medicineCursor.close()
        }

        databaseHelper.close()

        super.onDestroy()
    }
}