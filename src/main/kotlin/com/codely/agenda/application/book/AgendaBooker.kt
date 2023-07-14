package com.codely.agenda.application.book

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.Raise
import arrow.core.raise.withError
import com.codely.agenda.application.book.BookAgendaError.AgendaNotFound
import com.codely.agenda.application.book.BookAgendaError.Unknown
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import com.codely.agenda.domain.findByOrElse
import com.codely.agenda.domain.saveOrElse
import java.util.UUID

context(AgendaRepository)
suspend fun bookAgenda(id: UUID, name: Player, hourId: UUID): Either<BookAgendaError, Agenda> =
    findByOrElse(Id(id), onError = { AgendaNotFound })
        .flatMap { agenda -> agenda.bookAvailableHour(hourId, name) }
        .flatMap { agenda -> agenda.saveOrElse { error -> Unknown(error) } }

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun bookAgendaDsl(id: UUID, name: Player, hourId: UUID): Agenda =
    findByOrElse(Id(id), onError = { AgendaNotFound }).bind()
        .bookAvailableHour(hourId, name).bind()
        .saveOrElse { error -> Unknown(error) }.bind()

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun bookAgendaDsl2(id: UUID, name: Player, hourId: UUID): Agenda {
    // Transforms Raise<Throwable> to Raise<BookAgendaError>
    val agenda = withError(block = { findByDsl(Id(id)) }, transform = { AgendaNotFound })

    val updatedAgenda = agenda.bookAvailableHour(hourId, name).bind()

    return updatedAgenda.saveOrElse { error -> Unknown(error) }.bind()
}

sealed class BookAgendaError {
    data object AgendaNotFound : BookAgendaError()
    data object MaxCapacityReached : BookAgendaError()
    data object PlayerAlreadyBooked : BookAgendaError()
    data object AvailableHourNotFound : BookAgendaError()
    class Unknown(val cause: Throwable) : BookAgendaError()
}
