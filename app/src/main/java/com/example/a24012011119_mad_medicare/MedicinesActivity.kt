package com.example.a24012011119_mad_medicare




import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MedicinesActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView
    private lateinit var cardMedicine1: MaterialCardView
    private lateinit var cardMedicine2: MaterialCardView
    private lateinit var btnAddMedicine: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_medicines)

        txtBack = findViewById(R.id.txtBack)
        cardMedicine1 = findViewById(R.id.cardMedicine1)
        cardMedicine2 = findViewById(R.id.cardMedicine2)
        btnAddMedicine = findViewById(R.id.btnAddMedicine)

        // Back button
        txtBack.setOnClickListener {
            finish()
        }

        // Medicine 1
        cardMedicine1.setOnClickListener {
            Toast.makeText(
                this,
                "Paracetamol",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Medicine 2
        cardMedicine2.setOnClickListener {
            Toast.makeText(
                this,
                "Vitamin D",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Add Medicine
        btnAddMedicine.setOnClickListener {

            val intent = Intent(
                this,
                AddMedicineActivity::class.java
            )

            startActivity(intent)
        }
    }
}