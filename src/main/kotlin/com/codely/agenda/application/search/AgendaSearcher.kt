package com.codely.agenda.application.search

import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.application.search.SearchAgendaError.Unknown
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria.WeekAndYear as WeekCriteria
import com.codely.agenda.domain.Week
import com.codely.agenda.domain.Year
import com.codely.agenda.domain.searchByOrElse

context(AgendaRepository, Raise<SearchAgendaError>)
suspend fun search(week: Week, year: Year): List<Agenda> =
    searchByOrElse(WeekCriteria(week, year)) { error -> Unknown(error) }
        .bind()

sealed class SearchAgendaError {
    class Unknown(val cause: Throwable) : SearchAgendaError()
}
