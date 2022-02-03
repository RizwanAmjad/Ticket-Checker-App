package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
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
import org.json.JSONObject


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
                val resultContent = result.contents
                try{
                    val ticket = TicketModel(resultContent)

                    val resultContentJson = JSONObject(
                        String(Base64.decode(resultContent, Base64.DEFAULT)))

                    when (databaseHandler.checkTicketExistence(ticket)) {
                        null -> {
                            // scanning another app generated ticket

                            resultContentJson.apply {
                                getString("Category")
                                getString("FullName")
                                getString("TournamentDate")
                                getString("TournamentName")
                            }
                            databaseHandler.addTicket(ticket)
                            Snackbar.make(requireView(), "Ticket scanned from Registration Area", Snackbar.LENGTH_SHORT).show()
                        }
                        else -> {
                            val dialog = AlertDialog.Builder(context).apply {
                                setTitle("Ticket Already Scanned")
                                setMessage("This ticket has already been Scanned.")
                                setNeutralButton("I understand") { di, _ ->
                                    di.dismiss()
                                }
                            }
                            dialog.show()
                        }
                    }

                }catch (e: Exception){
                    Log.d("HERE", e.message.toString())
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