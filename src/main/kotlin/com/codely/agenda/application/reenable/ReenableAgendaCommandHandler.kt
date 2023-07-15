package com.codely.agenda.application.reenable

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.agenda.application.reenable.ReenableAgendaError.InvalidUUID
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import java.util.UUID

context(AgendaRepository, Raise<ReenableAgendaError>)
suspend fun handle(command: ReenableAgendaCommand): Agenda {
    val id = catch({ UUID.fromString(command.id) }) { raise(InvalidUUID) }
    return reenableAgenda(id)
}

data class ReenableAgendaCommand(val id: String)
