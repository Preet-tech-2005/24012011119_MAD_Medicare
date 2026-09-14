package com.example.a24012011119_mad_medicare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnPrescription: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnPrescription =
            findViewById(R.id.btnPrescription)

        btnPrescription.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MedicinesActivity::class.java
                )
            )
        }
    }
}