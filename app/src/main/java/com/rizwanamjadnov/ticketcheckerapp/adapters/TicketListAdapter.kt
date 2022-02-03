package com.rizwanamjadnov.ticketcheckerapp.adapters

import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rizwanamjadnov.ticketcheckerapp.R
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel
import org.json.JSONObject

class TicketListAdapter(private val dataset: List<TicketModel>):
    RecyclerView.Adapter<TicketListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ticketTitle: TextView = itemView.findViewById(R.id.ticketTitle)
        val ticketDate: TextView = itemView.findViewById(R.id.ticketDate)
        val ticketCategory: TextView = itemView.findViewById(R.id.ticketCategory)
        val tournamentName: TextView = itemView.findViewById(R.id.tournamentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_list_row, null, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = dataset[position]
        val resultContentJson = JSONObject(
            String(Base64.decode(ticket.ticketJwt, Base64.DEFAULT)))
        holder.apply {
            ticketTitle.text = "Full Name: " + resultContentJson.getString("FullName")
            ticketDate.text = "Tournament Date: "  + resultContentJson.getString("TournamentDate")
            ticketCategory.text = "Category: " + resultContentJson.getString("Category")
            tournamentName.text = "Tournament: " + resultContentJson.getString("TournamentName")
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}