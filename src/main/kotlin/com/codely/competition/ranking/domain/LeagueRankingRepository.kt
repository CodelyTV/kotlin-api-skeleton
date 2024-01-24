package com.codely.competition.ranking.domain

import com.codely.competition.clubs.domain.Club

interface LeagueRankingRepository {
    suspend fun save(ranking: LeagueRanking)
    suspend fun delete(league: League)
    suspend fun search(criteria: SearchLeagueRankingCriteria): LeagueRanking?
}

sealed interface SearchLeagueRankingCriteria {
    class ByLeagueAndClub(val league: League, val club: Club): SearchLeagueRankingCriteria
}
