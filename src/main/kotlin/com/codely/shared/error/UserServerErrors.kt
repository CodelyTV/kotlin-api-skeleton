package com.codely.shared.error

object UserServerErrors {

    // Agenda Module Errors
    val USER_ALREADY_BOOKED = Pair("10", "User already booked")
    val MAX_CAPACITY_REACHED = Pair("11", "User does not exist")
    val AGENDA_DOES_NOT_EXIST = Pair("12", "Agenda does not exist")
    val USER_NOT_BOOKED = Pair("13", "User wasn't booked")
    val AVAILABLE_HOUR_DOES_NOT_EXIST = Pair("14", "Available hour does not exist")
    val USER_CANNOT_BOOK_CONSECUTIVE_HOURS = Pair("15", "User can't book consecutive hours")
}
