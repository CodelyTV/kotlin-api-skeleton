package com.codely.agenda.fakes

import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria
import com.codely.agenda.domain.AgendaSearchByCriteria.ByWeekAndYear
import com.codely.shared.fakes.FakeRepository

class FakeAgendaRepository : AgendaRepository, FakeRepository<Agenda> {
    override val elements = mutableListOf<Agenda>()

    override suspend fun save(agenda: Agenda) { elements.add(agenda) }

    override suspend fun search(criteria: AgendaSearchByCriteria): List<Agenda> =
        when (criteria) {
            is ByWeekAndYear -> elements.filter { el -> el.week == criteria.week }
        }

    override suspend fun find(criteria: AgendaFindByCriteria): Agenda? =
        when (criteria) {
            is ById -> elements.firstOrNull { el -> el.id == criteria.id }
        }
}
