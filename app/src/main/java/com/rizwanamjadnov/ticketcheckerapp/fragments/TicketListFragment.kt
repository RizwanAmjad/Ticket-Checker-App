package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rizwanamjadnov.ticketcheckerapp.R
import com.rizwanamjadnov.ticketcheckerapp.adapters.TicketListAdapter
import com.rizwanamjadnov.ticketcheckerapp.database.DatabaseHandler
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel

class TicketListFragment : Fragment() {
    private lateinit var ticketListRecyclerView: RecyclerView

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var dataset: List<TicketModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_list, container, false)

        ticketListRecyclerView = view.findViewById(R.id.ticketsRecyclerView)

        databaseHandler = DatabaseHandler(requireContext())

        dataset = databaseHandler.allTickets()

        ticketListRecyclerView.adapter = TicketListAdapter(dataset)
        ticketListRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHandler.close()
    }

}