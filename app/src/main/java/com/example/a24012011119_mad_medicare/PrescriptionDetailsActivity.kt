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
    private lateinit var txtPrescriptionDetails: TextView

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_prescription_details
        )

        databaseHelper = DatabaseHelper(this)

        txtBack = findViewById(R.id.txtBack)

        txtDoctorName =
            findViewById(R.id.txtDoctorName)

        txtPrescriptionDate =
            findViewById(R.id.txtPrescriptionDate)

        txtDiagnosis =
            findViewById(R.id.txtDiagnosis)

        txtPrescriptionDetails =
            findViewById(R.id.txtPrescriptionDetails)

        txtBack.setOnClickListener {
            finish()
        }

        loadPrescription()
    }

    private fun loadPrescription() {

        val cursor: Cursor =
            databaseHelper.getLatestPrescription()

        if (cursor.moveToFirst()) {

            val doctorName =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "doctor_name"
                    )
                )

            val date =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "prescription_date"
                    )
                )

            val diagnosis =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "diagnosis"
                    )
                )

            val details =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "prescription_details"
                    )
                )

            txtDoctorName.text = doctorName
            txtPrescriptionDate.text = date
            txtDiagnosis.text = diagnosis
            txtPrescriptionDetails.text = details
        }

        cursor.close()
    }

    override fun onDestroy() {

        databaseHelper.close()

        super.onDestroy()
    }
}