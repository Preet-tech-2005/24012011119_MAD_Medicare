package com.example.a24012011119_mad_medicare

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var cardMedicines: MaterialCardView
    private lateinit var cardPrescriptions: MaterialCardView
    private lateinit var cardHistory: MaterialCardView
    private lateinit var btnAddMedicine: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        cardMedicines = findViewById(R.id.cardMedicines)
        cardPrescriptions = findViewById(R.id.cardPrescriptions)
        cardHistory = findViewById(R.id.cardHistory)
        btnAddMedicine = findViewById(R.id.btnAddMedicine)

        cardMedicines.setOnClickListener {
            val intent = Intent(this, MedicinesActivity::class.java)
            startActivity(intent)
        }

        cardPrescriptions.setOnClickListener {
            Toast.makeText(this, "Prescriptions", Toast.LENGTH_SHORT).show()
        }

        cardHistory.setOnClickListener {
            Toast.makeText(this, "Medicine History", Toast.LENGTH_SHORT).show()
        }

        btnAddMedicine.setOnClickListener {

            val intent = Intent(
                this,
                AddMedicineActivity::class.java
            )

            startActivity(intent)
        }
    }
}