package com.codely.agenda.application.book

import arrow.core.raise.Raise
import com.codely.agenda.application.book.BookAgendaError.AgendaNotFound
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import java.util.UUID

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun bookAgenda(id: UUID, name: Player, hourId: UUID): Agenda {
    val agenda = find(ById(id)) ?: raise(AgendaNotFound)

    return agenda.bookAvailableHour(hourId, name)
        .also { agenda -> save(agenda) }
}

sealed class BookAgendaError {
    data object InvalidUUID : BookAgendaError()
    data object AgendaNotFound : BookAgendaError()
    data object MaxCapacityReached : BookAgendaError()
    data object PlayerAlreadyBooked : BookAgendaError()
    data object AvailableHourNotFound : BookAgendaError()
}
