package com.codely.competition.ranking.domain

interface LeagueRankingRepository {
    suspend fun save(ranking: LeagueRanking)
    suspend fun delete(league: League)
    suspend fun search(criteria: SearchLeagueRankingCriteria): LeagueRanking?
}

sealed interface SearchLeagueRankingCriteria {
    class ByLeague(val league: League): SearchLeagueRankingCriteria
}
