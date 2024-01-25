package com.codely.competition.ranking.application.search

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.ranking.domain.League
import com.codely.competition.ranking.domain.LeagueRanking
import com.codely.competition.ranking.domain.LeagueRankingRepository
import com.codely.competition.ranking.domain.SearchLeagueRankingCriteria.ByLeagueAndClub

context(LeagueRankingRepository)
suspend fun searchLeagueRanking(league: League, club: ClubName): LeagueRanking? =
    search(ByLeagueAndClub(league, club))
