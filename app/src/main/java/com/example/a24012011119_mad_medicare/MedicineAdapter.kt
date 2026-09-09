package com.example.a24012011119_mad_medicare

import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicineAdapter(private var cursor: Cursor) :
    RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtMedicineName: TextView =
            itemView.findViewById(R.id.txtMedicineName)

        val txtDosage: TextView =
            itemView.findViewById(R.id.txtDosage)

        val txtTime: TextView =
            itemView.findViewById(R.id.txtTime)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicineViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)

        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MedicineViewHolder,
        position: Int
    ) {

        if (cursor.moveToPosition(position)) {

            val id =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                )

            val name =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("medicine_name")
                )

            val dosage =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("dosage")
                )

            val startDate =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("start_date")
                )

            val endDate =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("end_date")
                )

            val reminderTime =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("reminder_time")
                )

            val frequency =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("frequency")
                )

            val notes =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("notes")
                )

            holder.txtMedicineName.text = name
            holder.txtDosage.text = dosage
            holder.txtTime.text = "⏰  $reminderTime"

            holder.itemView.setOnClickListener {

                val intent = Intent(
                    holder.itemView.context,
                    MedicineDetailsActivity::class.java
                )

                intent.putExtra("id", id)
                intent.putExtra("medicine_name", name)
                intent.putExtra("dosage", dosage)
                intent.putExtra("start_date", startDate)
                intent.putExtra("end_date", endDate)
                intent.putExtra("reminder_time", reminderTime)
                intent.putExtra("frequency", frequency)
                intent.putExtra("notes", notes)

                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    fun updateCursor(newCursor: Cursor) {
        cursor.close()
        cursor = newCursor
        notifyDataSetChanged()
    }
}