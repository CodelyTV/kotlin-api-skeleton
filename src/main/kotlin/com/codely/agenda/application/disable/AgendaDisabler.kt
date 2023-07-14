package com.codely.agenda.application.disable

import arrow.core.raise.Raise
import com.codely.agenda.application.disable.DisableAgendaError.AgendaNotFound
import com.codely.agenda.application.disable.DisableAgendaError.Unknown
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.findByOrElse
import com.codely.agenda.domain.saveOrElse
import java.util.UUID

context(AgendaRepository, Raise<DisableAgendaError>)
suspend fun disableAgenda(id: UUID): Agenda =
    findByOrElse(Id(id)) { AgendaNotFound }.bind()
        .disable()
        .saveOrElse { error -> Unknown(error) }.bind()

sealed class DisableAgendaError {
    data object InvalidUUID : DisableAgendaError()
    data object AgendaNotFound : DisableAgendaError()
    data object ForbiddenAction : DisableAgendaError()
    class Unknown(val cause: Throwable) : DisableAgendaError()
}
