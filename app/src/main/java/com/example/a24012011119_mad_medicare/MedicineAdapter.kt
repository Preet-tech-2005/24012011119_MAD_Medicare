package com.example.a24012011119_mad_medicare

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicineAdapter(
    private var cursor: Cursor
) : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    class MedicineViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

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
            .inflate(
                R.layout.item_medicine,
                parent,
                false
            )

        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MedicineViewHolder,
        position: Int
    ) {

        if (cursor.moveToPosition(position)) {

            val name = cursor.getString(
                cursor.getColumnIndexOrThrow("medicine_name")
            )

            val dosage = cursor.getString(
                cursor.getColumnIndexOrThrow("dosage")
            )

            val time = cursor.getString(
                cursor.getColumnIndexOrThrow("reminder_time")
            )

            holder.txtMedicineName.text = name
            holder.txtDosage.text = dosage
            holder.txtTime.text = "⏰  $time"
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