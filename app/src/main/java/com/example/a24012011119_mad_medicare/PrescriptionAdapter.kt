package com.example.a24012011119_mad_medicare

import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PrescriptionAdapter(
    private var cursor: Cursor
) : RecyclerView.Adapter<PrescriptionAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val txtDisease: TextView =
            itemView.findViewById(
                R.id.txtDisease
            )

        val txtDoctor: TextView =
            itemView.findViewById(
                R.id.txtDoctor
            )

        val txtDate: TextView =
            itemView.findViewById(
                R.id.txtDate
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
                R.layout.item_prescription,
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

            val id =
                cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                        "id"
                    )
                )

            val disease =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "disease"
                    )
                )

            val doctor =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "doctor_name"
                    )
                )

            val date =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "prescription_date"
                    )
                )

            holder.txtDisease.text =
                disease

            holder.txtDoctor.text =
                "Doctor: $doctor"

            holder.txtDate.text =
                "Date: $date"

            holder.itemView.setOnClickListener {

                val intent =
                    Intent(
                        holder.itemView.context,
                        PrescriptionDetailsActivity::class.java
                    )

                intent.putExtra(
                    "prescription_id",
                    id
                )

                holder.itemView.context.startActivity(
                    intent
                )
            }
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