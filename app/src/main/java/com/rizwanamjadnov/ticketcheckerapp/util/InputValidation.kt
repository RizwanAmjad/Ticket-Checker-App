package com.rizwanamjadnov.ticketcheckerapp.util

import com.rizwanamjadnov.ticketcheckerapp.models.TicketModel

class InputValidation {
    companion object{

        private fun validateTitle(title: String): Boolean{
            return title.matches(Regex("[a-zA-Z\\s]+"))
        }

        private fun validateDate(date: String): Boolean{
            try{
                date.split('/').also{item->
                    val dd = item[0].toInt()
                    val mm = item[1].toInt()
                    val yy = item[2].toInt()
                    if((dd in 1..31) && (mm in 1..12) && (yy in 1000..9999)){
                        return true
                    }
                }
            }catch (e: Exception){
                return false
            }
            return false
        }

        fun validateTicket(ticket: TicketModel): Boolean{
            if(validateDate(ticket.ticketDate)
                && validateTitle(ticket.ticketTitle)){
                return true
            }
            return false
        }

    }

}