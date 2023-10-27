package com.codely.agenda.application.search

import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Year

context(AgendaRepository)
suspend fun handle(query: SearchAgendasQuery) = searchAgenda(week = query.week, year = query.year)

data class SearchAgendasQuery(val week: Int, val year: Year)
