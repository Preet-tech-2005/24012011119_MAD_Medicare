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
    private lateinit var recyclerMedicines: RecyclerView
    private lateinit var btnAddMedicine: MaterialButton

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var medicineAdapter: MedicineAdapter

    private lateinit var cursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_medicines)

        txtBack = findViewById(R.id.txtBack)
        recyclerMedicines = findViewById(R.id.recyclerMedicines)
        btnAddMedicine = findViewById(R.id.btnAddMedicine)

        databaseHelper = DatabaseHelper(this)

        recyclerMedicines.layoutManager =
            LinearLayoutManager(this)

        loadMedicines()

        // Back button
        txtBack.setOnClickListener {
            finish()
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

    private fun loadMedicines() {

        cursor = databaseHelper.getAllMedicines()

        medicineAdapter = MedicineAdapter(cursor)

        recyclerMedicines.adapter = medicineAdapter
    }

    override fun onResume() {
        super.onResume()

        if (::databaseHelper.isInitialized &&
            ::medicineAdapter.isInitialized
        ) {
            val newCursor =
                databaseHelper.getAllMedicines()

            medicineAdapter.updateCursor(newCursor)
        }
    }

    override fun onDestroy() {

        if (::cursor.isInitialized &&
            !cursor.isClosed
        ) {
            cursor.close()
        }

        databaseHelper.close()

        super.onDestroy()
    }
}