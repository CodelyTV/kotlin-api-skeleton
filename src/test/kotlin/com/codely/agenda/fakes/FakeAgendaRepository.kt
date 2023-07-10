package com.codely.agenda.fakes

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaRepository
import com.codely.shared.fakes.FakeRepository

class FakeAgendaRepository : AgendaRepository, FakeRepository<Agenda> {
    override val elements = mutableListOf<Agenda>()

    context
    override fun save(agenda: Agenda): Either<Throwable, Unit> = catch {
        TODO("Not yet implemented")
    }

    override fun findBy(criteria: AgendaFindByCriteria): Either<Throwable, Agenda> {
        TODO("Not yet implemented")
    }
}