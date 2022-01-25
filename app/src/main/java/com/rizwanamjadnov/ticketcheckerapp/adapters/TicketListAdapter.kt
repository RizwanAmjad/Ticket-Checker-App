package com.rizwanamjadnov.ticketcheckerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rizwanamjadnov.ticketcheckerapp.R
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel

class TicketListAdapter(private val dataset: List<TicketModel>):
    RecyclerView.Adapter<TicketListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ticketTitle: TextView = itemView.findViewById(R.id.ticketTitle)
        val ticketDate: TextView = itemView.findViewById(R.id.ticketDate)
        val maxScans: TextView = itemView.findViewById(R.id.maxScans)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_list_row, null, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = dataset[position]
        holder.apply {
            ticketTitle.text = ticket.ticketTitle
            ticketDate.text = ticket.ticketDate
            maxScans.text = ticket.maxScans.toString()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}