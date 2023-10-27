package com.codely.agenda.application.disable

import arrow.core.raise.Raise
import com.codely.agenda.application.disable.DisableAgendaError.AgendaNotFound
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.findOrElse
import java.util.UUID

context(AgendaRepository, Raise<DisableAgendaError>)
suspend fun disableAgenda(id: UUID): Agenda {
    val agenda = findOrElse(ById(id)) { AgendaNotFound }

    return agenda.disable()
        .also { disabledAgenda -> save(disabledAgenda) }
}


sealed class DisableAgendaError {
    data object InvalidUUID : DisableAgendaError()
    data object AgendaNotFound : DisableAgendaError()
    data object ForbiddenAction : DisableAgendaError()
}
