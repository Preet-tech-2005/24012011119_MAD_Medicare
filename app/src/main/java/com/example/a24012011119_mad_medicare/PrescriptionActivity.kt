package com.example.a24012011119_mad_medicare

import android.content.Intent
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PrescriptionActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView

    private lateinit var edtDoctorName: TextInputEditText
    private lateinit var edtPrescriptionDate: TextInputEditText
    private lateinit var edtDiagnosis: TextInputEditText
    private lateinit var edtPrescriptionDetails: TextInputEditText

    private lateinit var btnSavePrescription: MaterialButton

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prescription)

        databaseHelper = DatabaseHelper(this)

        txtBack = findViewById(R.id.txtBack)

        edtDoctorName =
            findViewById(R.id.edtDoctorName)

        edtPrescriptionDate =
            findViewById(R.id.edtPrescriptionDate)

        edtDiagnosis =
            findViewById(R.id.edtDiagnosis)

        edtPrescriptionDetails =
            findViewById(R.id.edtPrescriptionDetails)

        btnSavePrescription =
            findViewById(R.id.btnSavePrescription)


        // Back Button

        txtBack.setOnClickListener {
            finish()
        }


        // Date Picker

        edtPrescriptionDate.setOnClickListener {
            showDatePicker()
        }


        // Save Button

        btnSavePrescription.setOnClickListener {
            savePrescription()
        }
    }

    private fun showDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->

                val date = Calendar.getInstance()

                date.set(
                    year,
                    month,
                    day
                )

                val format = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

                edtPrescriptionDate.setText(
                    format.format(date.time)
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun savePrescription() {

        val doctorName =
            edtDoctorName.text.toString().trim()

        val date =
            edtPrescriptionDate.text.toString().trim()

        val diagnosis =
            edtDiagnosis.text.toString().trim()

        val details =
            edtPrescriptionDetails.text.toString().trim()


        if (doctorName.isEmpty()) {

            edtDoctorName.error =
                "Enter doctor name"

            return
        }

        if (date.isEmpty()) {

            edtPrescriptionDate.error =
                "Select prescription date"

            return
        }

        if (diagnosis.isEmpty()) {

            edtDiagnosis.error =
                "Enter diagnosis"

            return
        }

        if (details.isEmpty()) {

            edtPrescriptionDetails.error =
                "Enter prescription details"

            return
        }


        val inserted =
            databaseHelper.insertPrescription(
                doctorName,
                date,
                diagnosis,
                details
            )


        if (inserted) {

            Toast.makeText(
                this,
                "Prescription Added Successfully",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(
                this,
                PrescriptionDetailsActivity::class.java
            )

            startActivity(intent)

            finish()

        }

         else {

            Toast.makeText(
                this,
                "Failed to add prescription",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {

        databaseHelper.close()

        super.onDestroy()
    }
}