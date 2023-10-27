package com.codely.agenda.domain

import arrow.core.raise.Raise
import java.util.UUID

interface AgendaRepository {
    suspend fun search(criteria: AgendaSearchByCriteria): List<Agenda>
    suspend fun find(criteria: AgendaFindByCriteria): Agenda?
    suspend fun save(agenda: Agenda)
}

suspend fun AgendaRepository.save(agenda: Agenda): Agenda =
    save(agenda).let { agenda }

context(Raise<Error>)
suspend fun <Error> AgendaRepository.findOrElse(criteria: AgendaFindByCriteria, onError: () -> Error): Agenda =
    find(criteria) ?: raise(onError())

sealed class AgendaFindByCriteria {
    class ById(val id: UUID) : AgendaFindByCriteria()
}

sealed class AgendaSearchByCriteria {
    class ByWeekAndYear(val week: Week, val year: Year) : AgendaSearchByCriteria()
}
