package com.codely.agenda.application.cancel

import arrow.core.Either
import arrow.core.flatMap
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import com.codely.agenda.domain.findByOrElse
import com.codely.agenda.domain.saveOrElse
import java.util.UUID

context(AgendaRepository)
suspend fun cancelBooking(id: UUID, name: Player, hourId: UUID): Either<CancelBookingError, Agenda> =
    findByOrElse(AgendaFindByCriteria.Id(id), onError = { CancelBookingError.AgendaNotFound } )
        .flatMap { agenda -> agenda.cancelBooking(hourId, name) }
        .flatMap { agenda -> agenda.saveOrElse { error -> CancelBookingError.Unknown(error) } }

sealed class CancelBookingError {
    data object AgendaNotFound : CancelBookingError()
    data object AvailableHourNotFound : CancelBookingError()
    data object PlayerNotBooked : CancelBookingError()
    class Unknown(val cause: Throwable) : CancelBookingError()
}