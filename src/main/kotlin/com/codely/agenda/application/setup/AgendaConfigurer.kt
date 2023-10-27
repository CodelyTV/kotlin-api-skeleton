package com.codely.agenda.application.setup

import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.Day
import com.codely.agenda.domain.Year
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(AgendaRepository, Raise<ConfigureAgendaError>)
suspend fun configureAgenda(year: Year) {

    val currentDate = LocalDate.now()
    val endDate = LocalDate.of(year, 12, 32)
    logger.info { "Starting agenda set up action until $endDate" }

    currentDate.datesUntil(endDate)
        .forEach { date ->
            runBlocking {
                val currentDay = Day(date.dayOfMonth, date.dayOfWeek)
                val agenda = Agenda.from(currentDay, date)

                save(agenda)

                logger.info { "Agenda created for ${agenda.day.number}/${agenda.month.name}/${agenda.year} and week ${agenda.week}" }
            }
        }
}

sealed class ConfigureAgendaError {
    class Unknown(val cause: Throwable) : ConfigureAgendaError()
}
