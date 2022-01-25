package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rizwanamjadnov.ticketcheckerapp.R

class TicketCreateFragment : Fragment() {
    private lateinit var ticketTitleText: EditText
    private lateinit var ticketDateText: EditText
    private lateinit var maxScansText: EditText
    private lateinit var createTicketButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_create, container, false)

        ticketTitleText = view.findViewById(R.id.ticketTitleText)
        ticketDateText = view.findViewById(R.id.ticketDateText)
        maxScansText = view.findViewById(R.id.maxScansText)
        createTicketButton = view.findViewById(R.id.createTicketButton)

        createTicketButton.setOnClickListener{
            Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}