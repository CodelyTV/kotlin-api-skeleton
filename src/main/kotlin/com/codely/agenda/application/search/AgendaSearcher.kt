package com.codely.agenda.application.search

import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria.ByWeekAndYear
import com.codely.agenda.domain.Week
import com.codely.agenda.domain.Year

context(AgendaRepository)
suspend fun searchAgenda(week: Week, year: Year): List<Agenda> = search(ByWeekAndYear(week, year))
