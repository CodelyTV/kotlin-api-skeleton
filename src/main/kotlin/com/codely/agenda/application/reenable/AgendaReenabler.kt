package com.codely.agenda.application.reenable

import arrow.core.raise.Raise
import com.codely.agenda.application.reenable.ReenableAgendaError.AgendaNotFound
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.findOrElse
import com.codely.agenda.domain.save
import java.util.UUID

context(AgendaRepository, Raise<ReenableAgendaError>)
suspend fun reenableAgenda(id: UUID): Agenda =
    findOrElse(ById(id)) { AgendaNotFound }
        .reenable()
        .save()

sealed class ReenableAgendaError {
    data object InvalidUUID : ReenableAgendaError()
    data object AgendaNotFound : ReenableAgendaError()
    data object ForbiddenAction : ReenableAgendaError()
}
