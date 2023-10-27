package com.codely.agenda.application.cancel

import arrow.core.raise.Raise
import com.codely.agenda.application.cancel.CancelBookingError.AgendaNotFound
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import com.codely.agenda.domain.findOrElse
import java.util.UUID

context(AgendaRepository, Raise<CancelBookingError>)
suspend fun cancelBooking(id: UUID, name: Player, hourId: UUID): Agenda {
    val agenda = findOrElse(ById(id)) { AgendaNotFound }

    return agenda.cancelBooking(hourId, name)
        .also { agenda -> save(agenda) }
}

sealed class CancelBookingError {
    data object InvalidUUID : CancelBookingError()
    data object AgendaNotFound : CancelBookingError()
    data object AvailableHourNotFound : CancelBookingError()
    data object PlayerNotBooked : CancelBookingError()
}
