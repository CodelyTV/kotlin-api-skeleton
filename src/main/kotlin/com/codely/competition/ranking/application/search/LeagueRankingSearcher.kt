package com.codely.competition.ranking.application.search

import com.codely.competition.ranking.domain.League
import com.codely.competition.ranking.domain.LeagueRanking
import com.codely.competition.ranking.domain.LeagueRankingRepository
import com.codely.competition.ranking.domain.SearchLeagueRankingCriteria
import com.codely.competition.ranking.domain.SearchLeagueRankingCriteria.ByLeague

context(LeagueRankingRepository)
suspend fun searchLeagueRanking(league: League): LeagueRanking? =
    search(ByLeague(league))
