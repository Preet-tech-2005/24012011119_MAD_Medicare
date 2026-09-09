package com.example.a24012011119_mad_medicare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnMedicines: MaterialButton
    private lateinit var btnPrescription: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnMedicines =
            findViewById(R.id.btnMedicines)

        btnPrescription =
            findViewById(R.id.btnPrescription)


        // My Medicines

        btnMedicines.setOnClickListener {

            val intent =
                Intent(
                    this,
                    MedicinesActivity::class.java
                )

            startActivity(intent)
        }


        // Prescription

        btnPrescription.setOnClickListener {

            val intent =
                Intent(
                    this,
                    PrescriptionActivity::class.java
                )

            startActivity(intent)
        }
    }
}