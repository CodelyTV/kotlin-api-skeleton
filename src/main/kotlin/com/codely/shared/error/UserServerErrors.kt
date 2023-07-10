package com.codely.shared.error

object UserServerErrors {

    // Agenda Module Errors
    val USER_ALREADY_BOOKED = Pair("10", "User already booked")
    val MAX_CAPACITY_REACHED = Pair("11", "User does not exist")
    val AGENDA_DOES_NOT_EXIST = Pair("12", "Agenda does not exist")
}