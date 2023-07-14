package com.codely.agenda.application.book

import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import java.util.UUID

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun handle(command: BookAgendaCommand): Agenda =
    bookAgenda(id = command.id, name = Player(name = command.playerName), hourId = command.hourId)

data class BookAgendaCommand(val id: UUID, val hourId: UUID, val playerName: String)
