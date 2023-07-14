package com.codely.agenda.application.cancel

import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import com.codely.agenda.domain.findByOrElse
import com.codely.agenda.domain.saveOrElse
import java.util.UUID

context(AgendaRepository, Raise<CancelBookingError>)
suspend fun cancelBooking(id: UUID, name: Player, hourId: UUID): Agenda =
    findByOrElse(AgendaFindByCriteria.Id(id), onError = { CancelBookingError.AgendaNotFound }).bind()
        .cancelBooking(hourId, name).bind()
        .saveOrElse { error -> CancelBookingError.Unknown(error) }.bind()

sealed class CancelBookingError {
    data object InvalidUUID : CancelBookingError()
    data object AgendaNotFound : CancelBookingError()
    data object AvailableHourNotFound : CancelBookingError()
    data object PlayerNotBooked : CancelBookingError()
    class Unknown(val cause: Throwable) : CancelBookingError()
}
