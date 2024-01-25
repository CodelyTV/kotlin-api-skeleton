package com.codely.competition.ranking.infrastructure.database

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.ranking.domain.*
import com.codely.competition.ranking.domain.SearchLeagueRankingCriteria.ByLeagueAndClub
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Component
import java.util.*

interface JpaLeagueRankingRepository : MongoRepository<LeagueRankingDocument, String> {
    fun deleteByName(name: String)

    @Query("{ 'name' : ?0, 'players' : { '\$elemMatch' : { 'club' : ?1 } } }")
    fun findByNameAndPlayersClub(name: String, club: String): LeagueRankingDocument?
    fun findByName(name: String): LeagueRankingDocument?
}

@Document(collection = "Ranking")
data class LeagueRankingDocument(
    @Id
    val id: String,
    val name: String,
    val players: List<RankedPlayerDocument>
) {
    fun toLeagueRanking(): LeagueRanking =
        LeagueRanking(
            id = UUID.fromString(id),
            name = League.valueOf(name),
            players = players.map { it.toRankedPlayer() }
        )
}

data class RankedPlayerDocument(
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

internal fun LeagueRanking.toDocument(): LeagueRankingDocument =
    LeagueRankingDocument(
        id = id.toString(),
        name = name.name,
        players = players.map { it.toDocument() }
    )

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
class MongoLeagueRankingRepository(private val repository: JpaLeagueRankingRepository): LeagueRankingRepository {
    override suspend fun save(ranking: LeagueRanking) { repository.save(ranking.toDocument()) }
    override suspend fun delete(league: League) { repository.deleteByName(league.name) }
    override suspend fun search(criteria: SearchLeagueRankingCriteria): LeagueRanking? =
        when(criteria) {
            is ByLeagueAndClub -> repository.findByName(criteria.league.name).filterClub(criteria.clubName)
        }?.toLeagueRanking()

    private fun LeagueRankingDocument?.filterClub(name: ClubName): LeagueRankingDocument? =
        this?.copy(players = players.filter { it.club == name.value })
}
