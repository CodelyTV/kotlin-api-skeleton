package com.codely.agenda.application.reenable

import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.application.reenable.ReenableAgendaError.AgendaNotFound
import com.codely.agenda.application.reenable.ReenableAgendaError.Unknown
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.findByOrElse
import com.codely.agenda.domain.saveOrElse
import java.util.UUID

context(AgendaRepository, Raise<ReenableAgendaError>)
suspend fun reenableAgenda(id: UUID): Agenda =
    findByOrElse(AgendaFindByCriteria.Id(id)) { AgendaNotFound }.bind()
        .reenable()
        .saveOrElse { error -> Unknown(error) }.bind()

sealed class ReenableAgendaError {
    data object InvalidUUID : ReenableAgendaError()
    data object AgendaNotFound : ReenableAgendaError()
    data object ForbiddenAction : ReenableAgendaError()
    class Unknown(val cause: Throwable) : ReenableAgendaError()
}
