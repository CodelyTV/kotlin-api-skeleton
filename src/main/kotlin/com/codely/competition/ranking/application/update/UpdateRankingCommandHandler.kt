package com.codely.competition.ranking.application.update

import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.players.domain.PlayerRepository
import com.codely.competition.ranking.domain.League
import com.codely.competition.ranking.domain.LeagueRankingRepository
import org.springframework.stereotype.Component

@Component
class UpdateRankingCommandHandler(
    playerRepository: PlayerRepository,
    clubRepository: ClubRepository,
    leagueRankingRepository: LeagueRankingRepository
) {

    private val updateRanking = RankingUpdater(playerRepository, clubRepository, leagueRankingRepository)

    suspend fun handle(command: UpdateRankingCommand) {
        updateRanking.invoke(command.lines, League.valueOf(command.league))
    }
}

data class UpdateRankingCommand(val lines: List<String>, val league: String)
