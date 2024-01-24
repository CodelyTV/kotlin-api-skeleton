package com.codely.competition.calendar

import com.codely.competition.clubs.domain.Club
import java.time.ZonedDateTime
import java.util.UUID

data class Calendar(
    val id: UUID,
    val name: String,
    val matches: List<Match>
)

data class Match(
    val localClub: Club,
    val visitorClub: Club,
    val result: Result?,
    val dateTime: ZonedDateTime
)

data class Result(
    val winner: Club,
    val winnerGames: Int,
    val loserGames: Int
)
