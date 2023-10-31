package com.codely.agenda.primaryadapter.rest.error

object AgendaServerErrors {

    // Agenda Module Errors
    val USER_ALREADY_BOOKED = Pair("10", "User already booked")
    val MAX_CAPACITY_REACHED = Pair("11", "User does not exist")
    val AGENDA_DOES_NOT_EXIST = Pair("12", "Agenda does not exist")
    val USER_NOT_BOOKED = Pair("13", "User wasn't booked")
    val AVAILABLE_HOUR_DOES_NOT_EXIST = Pair("14", "Available hour does not exist")
    val INVALID_IDENTIFIERS = Pair("16", "Invalid Identifiers")
    val INVALID_PLAYER_NAME = Pair("17", "Invalid Player Name")
}
