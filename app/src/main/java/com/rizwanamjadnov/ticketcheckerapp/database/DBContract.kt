package com.rizwanamjadnov.ticketcheckerapp.database

import android.provider.BaseColumns

class DBContract {
    class TicketEntry: BaseColumns{
        companion object{
            const val TABLE_NAME = "TicketTable"
            const val KEY_ID = "Id"
            const val KEY_TICKET_JWT = "TicketJwt"
        }
    }
}