package com.codely.agenda.application.disable

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import java.util.UUID

context(AgendaRepository, Raise<DisableAgendaError>)
suspend fun handle(command: DisableAgendaCommand): Agenda {
    val id = catch({ UUID.fromString(command.id) }) { raise(DisableAgendaError.InvalidUUID) }
    return disableAgenda(id)
}

data class DisableAgendaCommand(val id: String)
