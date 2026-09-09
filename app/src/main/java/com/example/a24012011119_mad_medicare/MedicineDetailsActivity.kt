package com.example.a24012011119_mad_medicare

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MedicineDetailsActivity : AppCompatActivity() {

    private lateinit var txtBack: TextView
    private lateinit var txtMedicineName: TextView
    private lateinit var txtDosage: TextView
    private lateinit var txtStartDate: TextView
    private lateinit var txtEndDate: TextView
    private lateinit var txtReminderTime: TextView
    private lateinit var txtFrequency: TextView
    private lateinit var txtNotes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_details)

        txtBack = findViewById(R.id.txtBack)
        txtMedicineName = findViewById(R.id.txtMedicineName)
        txtDosage = findViewById(R.id.txtDosage)
        txtStartDate = findViewById(R.id.txtStartDate)
        txtEndDate = findViewById(R.id.txtEndDate)
        txtReminderTime = findViewById(R.id.txtReminderTime)
        txtFrequency = findViewById(R.id.txtFrequency)
        txtNotes = findViewById(R.id.txtNotes)

        txtMedicineName.text =
            intent.getStringExtra("medicine_name") ?: ""

        txtDosage.text =
            intent.getStringExtra("dosage") ?: ""

        txtStartDate.text =
            intent.getStringExtra("start_date") ?: ""

        txtEndDate.text =
            intent.getStringExtra("end_date") ?: ""

        txtReminderTime.text =
            intent.getStringExtra("reminder_time") ?: ""

        txtFrequency.text =
            intent.getStringExtra("frequency") ?: ""

        txtNotes.text =
            intent.getStringExtra("notes") ?: ""

        txtBack.setOnClickListener {
            finish()
        }
    }
}