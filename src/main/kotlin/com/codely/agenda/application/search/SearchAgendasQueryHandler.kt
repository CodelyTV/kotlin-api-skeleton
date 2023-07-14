package com.codely.agenda.application.search

import arrow.core.raise.Raise
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Year

context(AgendaRepository, Raise<SearchAgendaError>)
suspend fun handle(query: SearchAgendasQuery) = search(week = query.week, year = query.year)

data class SearchAgendasQuery(val week: Int, val year: Year)
