package com.codely.agenda.application.book

import arrow.core.Either
import arrow.core.flatMap
import com.codely.agenda.application.book.BookAgendaError.AgendaNotFound
import com.codely.agenda.application.book.BookAgendaError.Unknown
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.PlayerName
import com.codely.agenda.domain.findByOrElse
import com.codely.agenda.domain.saveOrElse
import java.util.UUID

context(AgendaRepository)
suspend fun bookAgenda(id: UUID, name: PlayerName, hourId: UUID): Either<BookAgendaError, Agenda> =
    findByOrElse(Id(id), onError = { AgendaNotFound } )
        .flatMap { agenda -> agenda.bookAvailableHour(hourId, name) }
        .flatMap { agenda -> agenda.saveOrElse { error -> Unknown(error) } }

sealed class BookAgendaError {
    data object AgendaNotFound : BookAgendaError()
    data object MaxCapacityReached : BookAgendaError()
    data object PlayerAlreadyBooked : BookAgendaError()
    class Unknown(val cause: Throwable) : BookAgendaError()
}