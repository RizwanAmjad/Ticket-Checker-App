package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.rizwanamjadnov.ticketcheckerapp.R
import com.rizwanamjadnov.ticketcheckerapp.database.DBContract
import com.rizwanamjadnov.ticketcheckerapp.database.DatabaseHandler
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel

class TicketCreateFragment : Fragment() {
    private lateinit var ticketTitleText: EditText
    private lateinit var ticketDateText: EditText
    private lateinit var maxScansText: EditText
    private lateinit var createTicketButton: Button

    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_create, container, false)

        databaseHandler = DatabaseHandler(requireContext())

        ticketTitleText = view.findViewById(R.id.ticketTitleText)
        ticketDateText = view.findViewById(R.id.ticketDateText)
        maxScansText = view.findViewById(R.id.maxScansText)
        createTicketButton = view.findViewById(R.id.createTicketButton)

        createTicketButton.setOnClickListener{
            val ticketTitle = ticketTitleText.text.toString()
            val ticketDate = ticketDateText.text.toString()
            val maxScans = maxScansText.text.toString().toInt()

            val ticket = TicketModel(ticketTitle, ticketDate, maxScans)

            databaseHandler.addTicket(ticket)

            Snackbar.make(view, "Ticket Created Successfully", Snackbar.LENGTH_SHORT).show()
            ticketTitleText.text.clear()
            ticketDateText.text.clear()
            maxScansText.text.clear()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHandler.close()
    }
}