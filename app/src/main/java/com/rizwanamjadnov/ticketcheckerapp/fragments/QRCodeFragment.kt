package com.rizwanamjadnov.ticketcheckerapp.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.rizwanamjadnov.ticketcheckerapp.R
import android.content.Intent
import android.content.pm.PackageManager

import androidx.core.content.FileProvider

import android.os.Build

import android.graphics.Bitmap
import android.net.Uri

import android.os.Environment
import android.widget.TextView
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream


class QRCodeFragment : Fragment() {

    private lateinit var qrCodeImage: ImageView
    private lateinit var shareQRCodeButton: Button

    private lateinit var ticketTitle: TextView
    private lateinit var ticketDate: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_qr_code, container, false)

        qrCodeImage = view.findViewById(R.id.qrCodeImage)
        shareQRCodeButton = view.findViewById(R.id.shareQRCodeButton)

        ticketTitle = view.findViewById(R.id.ticketTitle)
        ticketDate = view.findViewById(R.id.ticketDate)

        shareQRCodeButton.setOnClickListener {
            if(Build.VERSION.SDK_INT>=23){
                if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    shareQrCode()
                }
            }
            else{
                shareQrCode();
            }
        }

        val dataInCode = requireArguments().get("QR")

        val dateAndTitle = dataInCode.toString()

        dateAndTitle.split('~').also { item->
            ticketTitle.text = item[0]
            ticketDate.text = item[1]
        }

        val multiFormatWriter = MultiFormatWriter();
        try{
            val bitMatrix=multiFormatWriter.encode(dataInCode as String?, BarcodeFormat.QR_CODE,200,200);
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            qrCodeImage.setImageBitmap(bitmap);
        }
        catch (e: Exception){
            e.printStackTrace()
        }

        return view
    }

    private fun shareQrCode() {
        //create a file provider
        qrCodeImage.setDrawingCacheEnabled(true)
        val bitmap: Bitmap = qrCodeImage.getDrawingCache()
        val file = File(Environment.getExternalStorageDirectory(), "bar_code.jpg")
        try {
            file.createNewFile()
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
            //sharingFile
            val intent = Intent(Intent.ACTION_SEND)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(
                        requireContext(),
                        "com.rizwanamjadnov.ticketcheckerapp",
                        file
                    )
                )
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            }
            intent.type = "image/*"
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }
}