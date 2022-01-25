package com.rizwanamjadnov.ticketcheckerapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel
import java.sql.SQLException

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        const val DB_VERSION = 1
        const val DB_NAME = "Ticket"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ${DBContract.TicketEntry.TABLE_NAME} " +
                "(" +
                DBContract.TicketEntry.KEY_ID + " INTEGER PRIMARY KEY, "+
                DBContract.TicketEntry.KEY_TICKET_TITLE + " TEXT,"+
                DBContract.TicketEntry.KEY_TICKET_DATE +" TEXT,"+
                DBContract.TicketEntry.KEY_MAX_SCANS +" INTEGER"+
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${DBContract.TicketEntry.TABLE_NAME}")
        onCreate(db)
    }

    fun addTicket(ticket: TicketModel){
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(DBContract.TicketEntry.KEY_TICKET_TITLE, ticket.ticketTitle)
        contentValues.put(DBContract.TicketEntry.KEY_TICKET_DATE, ticket.ticketDate)
        contentValues.put(DBContract.TicketEntry.KEY_MAX_SCANS, ticket.maxScans)

        db.insert(DBContract.TicketEntry.TABLE_NAME, null, contentValues)
        db.close()
    }

    fun allTickets(): ArrayList<TicketModel>{
        val db = this.readableDatabase
        val cursor: Cursor
        try{
            cursor = db.rawQuery("SELECT * FROM ${DBContract.TicketEntry.TABLE_NAME}", null)
        }catch (e:SQLException){
            return ArrayList()
        }
        val data = ArrayList<TicketModel>()

        if(cursor.moveToFirst()) {
            do{
                val ticketTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.TicketEntry.KEY_TICKET_TITLE))
                val ticketDate = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.TicketEntry.KEY_TICKET_DATE))
                val maxScans = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.TicketEntry.KEY_MAX_SCANS))

                data.add(TicketModel(ticketTitle, ticketDate, maxScans))
            }while (cursor.moveToNext())
        }
        cursor.close()
        return data
    }
}