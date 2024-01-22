package com.codely.competition.ranking.domain

import com.codely.competition.players.domain.PlayerId

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
