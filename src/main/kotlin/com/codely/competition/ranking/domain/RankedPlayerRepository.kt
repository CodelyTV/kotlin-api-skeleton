package com.codely.competition.ranking.domain

interface RankedPlayerRepository {
    suspend fun save(player: RankedPlayer)
}
