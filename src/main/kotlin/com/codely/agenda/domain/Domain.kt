package com.codely.agenda.domain

import com.codely.shared.clock.currentMonth
import com.codely.shared.clock.currentYear
import kotlinx.datetime.Month
import java.util.*

data class Agenda(
    val id: UUID,
    val day: Day,
    val month: Month,
    val year: Year,
    val availableHours: List<AvailableHour> = emptyList()
) {
    
    companion object {
        fun create(id: UUID, day: Day) =
            when(day.dayOfWeek.value) {
                1 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.monday())
                2 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.tuesday())
                3 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.wednesday())
                4 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.thursday())
                5 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.friday())
                6 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.saturday())
                7 -> Agenda(id, day, currentMonth(), currentYear(), emptyList())
                else -> TODO()
            }
    }
    
    fun addPlayer(hourId: UUID, player: PlayerName): Agenda {
        val playerAdded = availableHours.filter { it.id == hourId }
            .map { it.addPlayer(player) }
            .toSet()

        return copy(availableHours =
            playerAdded.union(availableHours)
                .distinctBy { availableHour -> availableHour.from }
                .toList()
        )
    }

    fun removePlayer(hourId: UUID, player: PlayerName): Agenda {
        val playerAdded = availableHours.filter { it.id == hourId }
            .map { it.removePlayer(player) }
            .toSet()

        return copy(availableHours =
        playerAdded.union(availableHours)
            .distinctBy { availableHour -> availableHour.from }
            .toList()
        )
    }

}

data class AvailableHour(
    val id: UUID = UUID.randomUUID(),
    val from: Int, // 4
    val to: Int, // 5
    val capacity: MaxCapacity = MaxCapacity(8),
    val type: HourType,
    val players: List<PlayerName> // max size depends on capacity
) {

    fun addPlayer(player: PlayerName): AvailableHour = copy(players = players + player)
    fun removePlayer(player: PlayerName): AvailableHour = copy(players = players - player)

    companion object {
        fun monday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, players = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun tuesday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 18, to = 19, players = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun wednesday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, players = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun thursday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, players = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun friday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, players = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun saturday() = listOf(
            AvailableHour(from = 10, to = 11, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 11, to = 12, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 12, to = 13, players = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 13, to = 14, players = emptyList(), type = HourType.MEMBERS_TIME)
        )
    }
}

@JvmInline
value class PlayerName(val value: String)

typealias Year = Int

enum class HourType { TEAM_TRAINING, ADULT_ACADEMY, KIDS_ACADEMY, MEMBERS_TIME }

@JvmInline
value class MaxCapacity(val value: Int = 8)