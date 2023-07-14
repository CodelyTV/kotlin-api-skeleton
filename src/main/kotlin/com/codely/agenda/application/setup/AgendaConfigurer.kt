package com.codely.agenda.application.setup

import arrow.core.raise.Raise
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.saveOrElse
import com.codely.agenda.application.setup.ConfigureAgendaError.Unknown
import com.codely.agenda.domain.Year
import com.codely.shared.clock.currentDay
import java.time.LocalDate

context(AgendaRepository, Raise<ConfigureAgendaError>)
suspend fun configureAgenda(year: Year) {

    val currentDate = LocalDate.now()
    val endDate = LocalDate.of(year, 12, 31)

    var date = currentDate

    while (date <= endDate) {
        val currentDay = currentDay()

        Agenda.create(currentDay)
            .saveOrElse { error -> Unknown(error) }
            .bind()

        date = date.plusDays(1)
    }
}

sealed class ConfigureAgendaError {
    class Unknown(val cause: Throwable) : ConfigureAgendaError()
}
