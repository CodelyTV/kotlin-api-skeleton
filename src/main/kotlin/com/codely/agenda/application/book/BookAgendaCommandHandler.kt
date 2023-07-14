package com.codely.agenda.application.book

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.agenda.application.book.BookAgendaError.InvalidUUID
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Player
import java.util.UUID

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun handle(command: BookAgendaCommand): Agenda {
    val id = catch(block = { UUID.fromString(command.id) }) { raise(InvalidUUID) }
    val hourId = catch(block = { UUID.fromString(command.hourId) }) { raise(InvalidUUID) }

    return bookAgenda(id = id, name = Player(name = command.playerName), hourId = hourId)
}

data class BookAgendaCommand(val id: String, val hourId: String, val playerName: String)
