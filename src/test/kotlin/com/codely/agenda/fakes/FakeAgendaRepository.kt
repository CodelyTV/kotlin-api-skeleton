package com.codely.agenda.fakes

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaFindByCriteria.Id
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria
import com.codely.agenda.domain.AgendaSearchByCriteria.WeekAndYear
import com.codely.shared.fakes.FakeRepository

class FakeAgendaRepository : AgendaRepository, FakeRepository<Agenda> {
    override val elements = mutableListOf<Agenda>()

    override suspend fun save(agenda: Agenda): Either<Throwable, Unit> = catch { elements.add(agenda) }

    override suspend fun findBy(criteria: AgendaFindByCriteria): Either<Throwable, Agenda> = catch {
        when (criteria) {
            is Id -> elements.first { el -> el.id == criteria.id }
        }
    }

    override suspend fun searchBy(criteria: AgendaSearchByCriteria): Either<Throwable, List<Agenda>> = catch {
        when (criteria) {
            is WeekAndYear -> elements.filter { el -> el.week == criteria.week }
        }
    }
}
