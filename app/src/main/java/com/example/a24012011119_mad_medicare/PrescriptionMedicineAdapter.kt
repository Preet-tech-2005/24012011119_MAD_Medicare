package com.example.a24012011119_mad_medicare

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class PrescriptionMedicineAdapter(
    private var cursor: Cursor,
    private val databaseHelper: DatabaseHelper
) : RecyclerView.Adapter<PrescriptionMedicineAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val txtMedicineName: TextView =
            itemView.findViewById(
                R.id.txtMedicineName
            )

        val txtDosage: TextView =
            itemView.findViewById(
                R.id.txtDosage
            )

        val txtFrequency: TextView =
            itemView.findViewById(
                R.id.txtFrequency
            )

        val txtDuration: TextView =
            itemView.findViewById(
                R.id.txtDuration
            )

        val txtReminderTime: TextView =
            itemView.findViewById(
                R.id.txtReminderTime
            )

        val txtProgress: TextView =
            itemView.findViewById(
                R.id.txtProgress
            )

        val txtRemaining: TextView =
            itemView.findViewById(
                R.id.txtRemaining
            )

        val btnTaken: MaterialButton =
            itemView.findViewById(
                R.id.btnTaken
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_prescription_medicine,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        if (cursor.moveToPosition(position)) {

            val medicineId =
                cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                        "id"
                    )
                )

            val medicineName =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "medicine_name"
                    )
                )

            val dosage =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "dosage"
                    )
                )

            val frequency =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "frequency"
                    )
                )

            val frequencyCount =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "frequency_count"
                    )
                )

            val duration =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "duration_days"
                    )
                )

            val reminderTimes =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "reminder_times"
                    )
                )

            val totalDoses =
                frequencyCount * duration

            var taken =
                databaseHelper.getTakenCount(
                    medicineId
                )

            holder.txtMedicineName.text =
                medicineName

            holder.txtDosage.text =
                "Dosage: $dosage"

            holder.txtFrequency.text =
                "Frequency: $frequency"

            holder.txtDuration.text =
                "Duration: $duration days"

            holder.txtReminderTime.text =
                "Reminder: " +
                        reminderTimes.replace(
                            "|",
                            ", "
                        )

            updateProgress(
                holder,
                taken,
                totalDoses
            )

            holder.btnTaken.setOnClickListener {

                if (taken < totalDoses) {

                    taken++

                    databaseHelper.updateTakenCount(
                        medicineId,
                        taken
                    )

                    updateProgress(
                        holder,
                        taken,
                        totalDoses
                    )
                }
            }
        }
    }

    private fun updateProgress(
        holder: ViewHolder,
        taken: Int,
        totalDoses: Int
    ) {

        holder.txtProgress.text =
            "Progress: $taken / $totalDoses"

        val remaining =
            totalDoses - taken

        holder.txtRemaining.text =
            "Remaining: $remaining"

        if (taken >= totalDoses) {

            holder.btnTaken.text =
                "Completed ✓"

            holder.btnTaken.isEnabled =
                false

        } else {

            holder.btnTaken.text =
                "Mark as Taken"

            holder.btnTaken.isEnabled =
                true
        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    fun updateCursor(
        newCursor: Cursor
    ) {

        cursor.close()

        cursor = newCursor

        notifyDataSetChanged()
    }
}