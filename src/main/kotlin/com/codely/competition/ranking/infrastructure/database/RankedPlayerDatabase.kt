package com.codely.competition.ranking.infrastructure.database

import com.codely.competition.players.domain.FindPlayerCriteria
import com.codely.competition.players.domain.Player
import com.codely.competition.players.domain.PlayerRepository
import com.codely.competition.players.domain.SearchPlayerCriteria
import com.codely.competition.players.infrastructure.database.JpaPlayerRepository
import com.codely.competition.players.infrastructure.database.PlayerDocument
import com.codely.competition.players.infrastructure.database.toDocument
import com.codely.competition.ranking.domain.GameStats
import com.codely.competition.ranking.domain.RankedPlayer
import com.codely.competition.ranking.domain.RankedPlayerRepository
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

interface JpaRankedPlayerRepository : MongoRepository<RankedPlayerDocument, Long>

@Document(collection = "Ranking")
data class RankedPlayerDocument(
    @Id
    val id: Long,
    val name: String,
    val club: String,
    val stats: GameStatsDocument,
    val rankingPoints: Int
) {
    fun toRankedPlayer(): RankedPlayer =
        RankedPlayer(
            id = id,
            name = name,
            club = club,
            stats = stats.toGameStats(),
            rankingPoints = rankingPoints
        )
}

data class GameStatsDocument(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Double
) {
    fun toGameStats(): GameStats =
        GameStats(gamesPlayed = gamesPlayed, gamesWon = gamesWon, gamesLost = gamesLost, winRate = winRate)
}

internal fun RankedPlayer.toDocument(): RankedPlayerDocument =
    RankedPlayerDocument(
        id = id,
        name = name,
        club = club,
        stats = stats.toDocument(),
        rankingPoints = rankingPoints
    )
internal fun GameStats.toDocument(): GameStatsDocument =
    GameStatsDocument(gamesPlayed = gamesPlayed, gamesWon = gamesWon, gamesLost = gamesLost, winRate = winRate)

@Component
class MongoRankedPlayerRepository(private val repository: JpaRankedPlayerRepository): RankedPlayerRepository {
    override suspend fun save(player: RankedPlayer) { repository.save(player.toDocument()) }
}
