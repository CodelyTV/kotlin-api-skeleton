package com.codely.agenda.application.disable

import arrow.core.raise.Raise
import com.codely.agenda.application.disable.DisableAgendaError.AgendaNotFound
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.findOrElse
import com.codely.agenda.domain.save
import java.util.UUID

context(AgendaRepository, Raise<DisableAgendaError>)
suspend fun disableAgenda(id: UUID): Agenda =
    findOrElse(ById(id)) { AgendaNotFound }
        .disable()
        .save()

sealed class DisableAgendaError {
    data object InvalidUUID : DisableAgendaError()
    data object AgendaNotFound : DisableAgendaError()
    data object ForbiddenAction : DisableAgendaError()
}
