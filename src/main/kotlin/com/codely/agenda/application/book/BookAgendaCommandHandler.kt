package com.codely.agenda.application.book

import arrow.core.Either
import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import java.util.UUID

context(AgendaRepository)
suspend fun handle(command: BookAgendaCommand): Either<BookAgendaError, Agenda> =
    bookAgenda(id = command.id, name = Player(name = command.playerName), hourId = command.hourId)

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun handleDsl(command: BookAgendaCommand): Agenda =
    bookAgendaDsl(id = command.id, name = Player(name = command.playerName), hourId = command.hourId)

data class BookAgendaCommand(val id: UUID, val hourId: UUID, val playerName: String)
