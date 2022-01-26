package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.rizwanamjadnov.ticketcheckerapp.R
import com.rizwanamjadnov.ticketcheckerapp.database.DatabaseHandler
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel

class TicketCreateFragment : Fragment() {
    private lateinit var ticketTitleText: EditText
    private lateinit var ticketDateText: EditText
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
        createTicketButton = view.findViewById(R.id.createTicketButton)

        createTicketButton.setOnClickListener{
            val ticketTitle = ticketTitleText.text.toString()
            val ticketDate = ticketDateText.text.toString()

            val ticket = TicketModel(ticketTitle, ticketDate, 0)

            databaseHandler.addTicket(ticket)

            Snackbar.make(view, "Ticket Created Successfully", Snackbar.LENGTH_SHORT).show()
            ticketTitleText.text.clear()
            ticketDateText.text.clear()

            // open qr code fragment
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.navFragmentContainerView, QRCodeFragment().apply {
                    val bundle = Bundle()
                    bundle.putString("QR", ticket.ticketTitle+'/'+ticket.ticketDate+"/"+ticket.isScanned)
                    arguments = bundle
                })
                commit()
            }
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHandler.close()
    }
}