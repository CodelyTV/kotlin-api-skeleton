package com.codely.agenda.domain

import arrow.core.raise.Raise
import java.util.UUID

interface AgendaRepository {
    suspend fun search(criteria: AgendaSearchByCriteria): List<Agenda>
    suspend fun find(criteria: AgendaFindByCriteria): Agenda?
    suspend fun save(agenda: Agenda)
}

context(AgendaRepository)
suspend fun Agenda.save(): Agenda =
    save(this).let { this }

context(AgendaRepository, Raise<Error>)
suspend fun <Error> findOrElse(criteria: AgendaFindByCriteria, onError: () -> Error): Agenda =
    find(criteria) ?: raise(onError())

sealed class AgendaFindByCriteria {
    class ById(val id: UUID) : AgendaFindByCriteria()
}

sealed class AgendaSearchByCriteria {
    class ByWeekAndYear(val week: Week, val year: Year) : AgendaSearchByCriteria()
}
