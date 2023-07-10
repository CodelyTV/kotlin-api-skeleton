package com.codely.agenda.application.book

import arrow.core.Either
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.PlayerName
import java.util.UUID

context(AgendaRepository)
suspend fun handle(command: BookAgendaCommand): Either<BookAgendaError, Agenda> =
    book(id = command.id, name = PlayerName(value = command.playerName), hourId = command.hourId)


data class BookAgendaCommand(val id: UUID, val hourId: UUID, val playerName: String)