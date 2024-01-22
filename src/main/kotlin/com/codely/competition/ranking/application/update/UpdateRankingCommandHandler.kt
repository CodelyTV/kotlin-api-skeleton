package com.codely.competition.ranking.application.update

import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.players.domain.PlayerRepository
import com.codely.competition.ranking.WeeklyRankingUpdater
import com.codely.competition.ranking.domain.RankedPlayerRepository
import org.springframework.stereotype.Component

@Component
class UpdateRankingCommandHandler(
    playerRepository: PlayerRepository,
    clubRepository: ClubRepository,
    rankedPlayerRepository: RankedPlayerRepository
) {

    private val updateRanking = RankingUpdater(playerRepository, clubRepository, rankedPlayerRepository)

    suspend fun handle(command: UpdateRankingCommand) {
        updateRanking.invoke(command.lines)
    }
}

data class UpdateRankingCommand(val lines: List<String>)
