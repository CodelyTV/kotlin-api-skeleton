package com.codely.agenda.domain

import arrow.core.Either
import kotlinx.datetime.DayOfWeek
import java.util.UUID

interface AgendaRepository {
    suspend fun save(agenda: Agenda): Either<Throwable, Unit>
    suspend fun findBy(criteria: AgendaFindByCriteria): Either<Throwable, Agenda>
}

context(AgendaRepository)
suspend fun <T> Agenda.saveOrElse(onError: (cause: Throwable) -> T): Either<T, Agenda> =
    save(this)
        .map { this }
        .mapLeft { error -> onError(error) }

context(AgendaRepository)
suspend fun <T> findByOrElse(criteria: AgendaFindByCriteria, onError: (cause: Throwable) -> T): Either<T, Agenda> =
    findBy(criteria)
        .mapLeft { error -> onError(error) }

sealed class AgendaFindByCriteria {
    class Id(val id: UUID) : AgendaFindByCriteria()
    class Weekday(val dayOfWeek: DayOfWeek) : AgendaFindByCriteria()
}
