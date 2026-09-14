package com.example.a24012011119_mad_medicare

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MedicinesActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView
    private lateinit var recyclerPrescriptions: RecyclerView
    private lateinit var btnAddPrescription: MaterialButton

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var prescriptionAdapter: PrescriptionAdapter
    private lateinit var cursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_medicines
        )

        txtBack =
            findViewById(R.id.txtBack)

        recyclerPrescriptions =
            findViewById(R.id.recyclerMedicines)

        btnAddPrescription =
            findViewById(R.id.btnAddMedicine)

        databaseHelper =
            DatabaseHelper(this)

        recyclerPrescriptions.layoutManager =
            LinearLayoutManager(this)

        loadPrescriptions()

        txtBack.setOnClickListener {
            finish()
        }

        btnAddPrescription.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PrescriptionActivity::class.java
                )
            )
        }
    }

    private fun loadPrescriptions() {

        cursor =
            databaseHelper.getAllPrescriptions()

        prescriptionAdapter =
            PrescriptionAdapter(cursor)

        recyclerPrescriptions.adapter =
            prescriptionAdapter
    }

    override fun onResume() {

        super.onResume()

        if (
            ::databaseHelper.isInitialized &&
            ::prescriptionAdapter.isInitialized
        ) {

            val newCursor =
                databaseHelper.getAllPrescriptions()

            prescriptionAdapter.updateCursor(
                newCursor
            )
        }
    }

    override fun onDestroy() {

        if (
            ::cursor.isInitialized &&
            !cursor.isClosed
        ) {
            cursor.close()
        }

        databaseHelper.close()

        super.onDestroy()
    }
}