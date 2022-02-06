package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.opencsv.CSVWriter
import com.rizwanamjadnov.ticketcheckerapp.R
import com.rizwanamjadnov.ticketcheckerapp.adapters.TicketListAdapter
import com.rizwanamjadnov.ticketcheckerapp.database.DatabaseHandler
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

class TicketListFragment : Fragment() {
    private lateinit var exportCSVButton: Button
    private lateinit var ticketListRecyclerView: RecyclerView

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var dataset: List<TicketModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_list, container, false)

        exportCSVButton = view.findViewById(R.id.exportCSVButton)
        ticketListRecyclerView = view.findViewById(R.id.ticketsRecyclerView)

        exportCSVButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Share too?")
                setMessage("Select Yes if You want to Share data for printing or any other Purpose")
                setPositiveButton("Yes") { _, _ ->
                    writeToCSV(true)
                    Snackbar.make(requireView(), "Exported & Now Sharing", Snackbar.LENGTH_SHORT).show()
                }
                setNegativeButton("No") { _, _ ->
                    writeToCSV(false)
                    Snackbar.make(requireView(), "Exported CSV in Internal Storage", Snackbar.LENGTH_SHORT).show()
                }
            }
            alertDialog.show()
        }

        databaseHandler = DatabaseHandler(requireContext())

        dataset = databaseHandler.allTickets().asReversed()

        ticketListRecyclerView.adapter = TicketListAdapter(dataset)

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            ticketListRecyclerView.context,
            layoutManager.orientation
        )
        ticketListRecyclerView.addItemDecoration(dividerItemDecoration)
        ticketListRecyclerView.layoutManager = layoutManager


        return view
    }

    private fun writeToCSV(doShare: Boolean){

        val baseDir = Environment.getExternalStorageDirectory().absolutePath
        val fileName = "Ticket Checking App Exported Data.csv"
        val filePath = baseDir + File.separator + fileName
        val f = File(filePath)

        // File exist
        val writer: CSVWriter = if (f.exists()&&!f.isDirectory) {
            val mFileWriter = FileWriter(filePath, false)
            CSVWriter(mFileWriter);
        } else {
            CSVWriter(FileWriter(filePath));
        }

        val tickets = databaseHandler.allTickets().asReversed()
        writer.flush()
        writer.writeNext(arrayOf(
            "Full Name",
            "Tournament Date",
            "Category",
            "Tournament",
            "Unique Code"
        ))

        for (ticket in tickets){
            val resultContentJson = JSONObject(
                String(Base64.decode(ticket.ticketJwt, Base64.DEFAULT)))
            writer.writeNext(arrayOf(
                resultContentJson.getString("FullName"),
                resultContentJson.getString("TournamentDate"),
                resultContentJson.getString("Category"),
                resultContentJson.getString("TournamentName"),
                ticket.ticketJwt
            ))
        }

        writer.close()

        if(doShare){
            //sharingFile
            val intent = Intent(Intent.ACTION_SEND)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(
                        requireContext(),
                        "com.rizwanamjadnov.ticketcheckerapp",
                        f
                    )
                )
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f))
            }
            intent.type = "*/*"
            startActivity(intent)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHandler.close()
    }

}