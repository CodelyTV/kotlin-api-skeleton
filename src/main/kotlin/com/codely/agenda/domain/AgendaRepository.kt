package com.codely.agenda.domain


import arrow.core.Either
import kotlinx.datetime.DayOfWeek
import java.util.*

interface AgendaRepository {
    fun save(agenda: Agenda): Either<Throwable, Unit>
    fun findBy(criteria: AgendaFindByCriteria): Either<Throwable, Agenda>
}

sealed class AgendaFindByCriteria {
    class Id(id: UUID) : AgendaFindByCriteria()
    class DayOfWeek(dayOfWeek: DayOfWeek) : AgendaFindByCriteria()
}