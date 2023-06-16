package com.codely

import com.codely.shared.clock.currentMonth
import com.codely.shared.clock.currentYear
import java.util.*
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

data class ClubDay(
    val id: UUID,
    val day: DayOfWeek,
    val month: Month,
    val year: Year,
    val availableHours: List<AvailableHour> = emptyList()
) {
    
    companion object {
        fun create(id: UUID, day: DayOfWeek) = 
            when(day.value) {
                1 -> ClubDay(id, day, currentMonth(), currentYear(), AvailableHour.monday())
                2 -> ClubDay(id, day, currentMonth(), currentYear(), AvailableHour.tuesday())
                3 -> ClubDay(id, day, currentMonth(), currentYear(), AvailableHour.wednesday())
                4 -> ClubDay(id, day, currentMonth(), currentYear(), AvailableHour.thursday())
                5 -> ClubDay(id, day, currentMonth(), currentYear(), AvailableHour.friday())
                6 -> ClubDay(id, day, currentMonth(), currentYear(), AvailableHour.saturday())
                7 -> ClubDay(id, day, currentMonth(), currentYear(), emptyList())
                else -> TODO()
            }
    }
    
    fun addPlayer(hour: AvailableHour, player: RegisteredPlayer): ClubDay {
        val playerAdded = availableHours.filter { it.from == hour.from }
            .map { it.addPlayer(player) }
            .toSet()

        return copy(availableHours =
            playerAdded.union(availableHours)
                .distinctBy { availableHour -> availableHour.from }
                .toList()
        )
    }

    fun removePlayer(hour: AvailableHour, player: RegisteredPlayer): ClubDay {
        val playerAdded = availableHours.filter { it.from == hour.from }
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
    val from: Int, // 4
    val to: Int, // 5
    val capacity: MaxCapacity = MaxCapacity(8),
    val players: List<RegisteredPlayer> // max size depends on capacity
) {

    fun addPlayer(player: RegisteredPlayer): AvailableHour = copy(players = players + player)
    fun removePlayer(player: RegisteredPlayer): AvailableHour = copy(players = players - player)

    companion object {
        fun monday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList()),
            AvailableHour(from = 17, to = 18, players = emptyList())
        )

        fun tuesday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList()),
            AvailableHour(from = 17, to = 18, players = emptyList()),
            AvailableHour(from = 18, to = 19, players = emptyList())
        )

        fun wednesday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList()),
            AvailableHour(from = 17, to = 18, players = emptyList())
        )

        fun thursday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList()),
            AvailableHour(from = 17, to = 18, players = emptyList())
        )

        fun friday() = listOf(
            AvailableHour(from = 16, to = 17, players = emptyList()),
            AvailableHour(from = 17, to = 18, players = emptyList())
        )

        fun saturday() = listOf(
            AvailableHour(from = 10, to = 11, players = emptyList()),
            AvailableHour(from = 11, to = 12, players = emptyList()),
            AvailableHour(from = 12, to = 13, players = emptyList()),
            AvailableHour(from = 13, to = 14, players = emptyList())
        )
    }
}

@JvmInline
value class RegisteredPlayer(val value: String)

typealias Year = Int

@JvmInline
value class MaxCapacity(val value: Int = 8)