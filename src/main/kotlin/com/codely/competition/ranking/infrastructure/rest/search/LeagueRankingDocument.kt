package com.codely.competition.ranking.infrastructure.rest.search

import com.codely.competition.ranking.domain.GameStats
import com.codely.competition.ranking.domain.LeagueRanking
import com.codely.competition.ranking.domain.RankedPlayer

data class LeagueRankingDocument(
    val id: String,
    val name: String,
    val players: List<RankedPlayerDocument>
)

data class RankedPlayerDocument(
    val id: Long,
    val name: String,
    val club: String,
    val stats: GameStatsDocument,
    val rankingPoints: Int
)

data class GameStatsDocument(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Double
)

internal fun GameStats.toDocument(): GameStatsDocument =
    GameStatsDocument(gamesPlayed = gamesPlayed, gamesWon = gamesWon, gamesLost = gamesLost, winRate = winRate)

internal fun RankedPlayer.toDocument(): RankedPlayerDocument =
    RankedPlayerDocument(id = id, name = name, club = club, stats = stats.toDocument(), rankingPoints = rankingPoints)

internal fun LeagueRanking.toDocument(): LeagueRankingDocument =
    LeagueRankingDocument(
        id = id.toString(),
        name = name.name,
        players = players.map { it.toDocument() }
    )

