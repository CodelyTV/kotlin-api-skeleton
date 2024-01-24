package com.codely.competition.ranking.domain

import com.codely.competition.players.domain.PlayerId
import java.util.UUID

data class LeagueRanking(
    val id: UUID,
    val name: League,
    val players: List<RankedPlayer>
) {
    companion object {
        fun create(
            id: UUID = UUID.randomUUID(),
            name: League,
            players: List<RankedPlayer>
        ) = LeagueRanking(id, name, players)
    }
}


data class RankedPlayer(
    val id: PlayerId,
    val name: String,
    val club: String,
    val stats: GameStats,
    val rankingPoints: Int
)

data class GameStats(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Double
)

enum class League(val value: String) {
    NACIONAL("NAC"),
    LOQUESEA("TDM"),
    PREFERENT("PREF"),
    PRIMERA("1a"),
    SEGUNDA_A("2aA"),
    SEGUNDA_B("2aB"),
    TERCERA_A("3aA"),
    TERCERA_B("3aB");

    companion object {
        fun parseNames(): List<String> = values().map { it.value }
    }
}
