package com.codely.competition.ranking.application.search

import com.codely.competition.ranking.domain.League
import com.codely.competition.ranking.domain.LeagueRanking
import com.codely.competition.ranking.domain.LeagueRankingRepository


context(LeagueRankingRepository)
suspend fun handle(query: SearchLeagueRankingQuery): LeagueRanking? =
    searchLeagueRanking(League.valueOf(query.league))

data class SearchLeagueRankingQuery(val league: String)
