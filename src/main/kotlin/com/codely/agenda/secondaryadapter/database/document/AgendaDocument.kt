package com.codely.agenda.secondaryadapter.database.document

import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AvailableHour
import com.codely.agenda.domain.Day
import com.codely.agenda.domain.HourType
import com.codely.agenda.domain.MaxCapacity
import com.codely.agenda.domain.Player
import java.util.UUID
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Agendas")
class AgendaDocument(
    @Id
    val id: String,
    val dayNumber: Int,
    val dayWeek: String,
    val month: String,
    val week: Int,
    val year: Int,
    val availableHours: List<AvailableHourDocument>
) {

    fun toAgenda(): Agenda =
        Agenda(
            id = UUID.fromString(id),
            day = Day(number = dayNumber, dayOfWeek = DayOfWeek.valueOf(dayWeek)),
            month = Month.valueOf(month),
            year = year,
            week = week,
            availableHours = availableHours.map { hour -> hour.toAvailableHour() }
        )
}

data class AvailableHourDocument(
    val id: String,
    val from: Int,
    val to: Int,
    val capacity: Int,
    val type: String,
    val registeredPlayers: List<String>
) {
    fun toAvailableHour() =
        AvailableHour(
            id = UUID.fromString(id),
            from = from,
            to = to,
            capacity = MaxCapacity(value = capacity),
            type = HourType.valueOf(type),
            registeredPlayers = registeredPlayers.map { Player(it) }
        )
}

internal fun Agenda.toDocument() =
    AgendaDocument(
        id = id.toString(),
        dayNumber = day.number,
        dayWeek = day.dayOfWeek.name,
        month = month.name,
        year = year,
        week = week,
        availableHours = availableHours.map { hour -> hour.toDocument() }
    )

internal fun AvailableHour.toDocument() =
    AvailableHourDocument(
        id = id.toString(),
        from = from,
        to = to,
        capacity = capacity.value,
        type = type.name,
        registeredPlayers = registeredPlayers.map { player -> player.name }
    )
