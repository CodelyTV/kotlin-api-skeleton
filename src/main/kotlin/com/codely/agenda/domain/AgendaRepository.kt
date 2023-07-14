package com.codely.agenda.domain

import arrow.core.Either
import java.util.UUID

interface AgendaRepository {
    suspend fun save(agenda: Agenda): Either<Throwable, Unit>
    suspend fun findBy(criteria: AgendaFindByCriteria): Either<Throwable, Agenda>
    suspend fun searchBy(criteria: AgendaSearchByCriteria): Either<Throwable, List<Agenda>>
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

context(AgendaRepository)
suspend fun <T> searchByOrElse(criteria: AgendaSearchByCriteria, onError: (cause: Throwable) -> T): Either<T, List<Agenda>> =
    searchBy(criteria)
        .mapLeft { error -> onError(error) }

sealed class AgendaFindByCriteria {
    class Id(val id: UUID) : AgendaFindByCriteria()
}

sealed class AgendaSearchByCriteria {
    class WeekAndYear(val week: Week, val year: Year) : AgendaSearchByCriteria()
}
