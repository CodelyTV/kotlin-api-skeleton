package com.codely.agenda.application.book

import arrow.core.raise.Raise
import com.codely.agenda.application.book.BookAgendaError.AgendaNotFound
import com.codely.agenda.application.book.BookAgendaError.Unknown
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import com.codely.agenda.domain.findByOrElse
import com.codely.agenda.domain.saveOrElse
import java.util.UUID

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun bookAgenda(id: UUID, name: Player, hourId: UUID): Agenda =
    findByOrElse(Id(id), onError = { AgendaNotFound }).bind()
        .bookAvailableHour(hourId, name).bind()
        .saveOrElse { error -> Unknown(error) }.bind()

sealed class BookAgendaError {
    data object InvalidUUID : BookAgendaError()
    data object AgendaNotFound : BookAgendaError()
    data object MaxCapacityReached : BookAgendaError()
    data object PlayerAlreadyBooked : BookAgendaError()
    data object AvailableHourNotFound : BookAgendaError()
    class Unknown(val cause: Throwable) : BookAgendaError()
}
