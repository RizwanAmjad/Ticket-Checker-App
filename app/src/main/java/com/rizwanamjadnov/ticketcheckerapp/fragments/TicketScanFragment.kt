package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.rizwanamjadnov.ticketcheckerapp.R
import com.rizwanamjadnov.ticketcheckerapp.database.DatabaseHandler
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel


class TicketScanFragment : Fragment() {
    private lateinit var scanTicketButton: Button

    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_scan, container, false)

        databaseHandler = DatabaseHandler(requireContext())

        scanTicketButton = view.findViewById(R.id.scanTicketButton)
        scanTicketButton.setOnClickListener {
            if(Build.VERSION.SDK_INT>=23){
                if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)==
                        PackageManager.PERMISSION_GRANTED){
                    openScanner()
                }
            }
            else{
                openScanner()
            }
        }

        return view
    }

    private fun openScanner() {
        IntentIntegrator.forSupportFragment(this).initiateScan()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Blank", Toast.LENGTH_SHORT).show()
            } else {
                val resultContent = result.contents.split('~')
                try{
                    val ticket = TicketModel(resultContent[0], resultContent[1], resultContent[2].toInt())
                    val dbTicket = databaseHandler.checkTicketExistence(ticket)

                    when {
                        dbTicket == null -> {
                            // scanning another app generated ticket
                            ticket.isScanned = 1
                            databaseHandler.addTicket(ticket)
                            Snackbar.make(requireView(), "Ticket scanned from Registeration Area App", Snackbar.LENGTH_SHORT).show()
                        }
                        dbTicket.isScanned == 1 -> {
                            val dialog = AlertDialog.Builder(context).apply {
                                setTitle("Ticket Already Scanned")
                                setMessage("This ticket has already been Scanned.")
                                setNeutralButton("I understand") { di, _ ->
                                    di.dismiss()
                                }
                            }
                            dialog.show()
                        }
                        else -> {
                            databaseHandler.markAsScanned(ticket)
                            Snackbar.make(requireView(), "First Time Scan", Snackbar.LENGTH_SHORT).show()
                        }
                    }

                }catch (e: Exception){
                    val dialog = AlertDialog.Builder(context).apply {
                        setTitle("Invalid Ticket")
                        setMessage("This is not an App Generated Ticket")
                        setNeutralButton("I understand") { di, _ ->
                            di.dismiss()
                        }
                    }
                    dialog.show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Blank", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHandler.close()
    }

}