package com.codely.competition.clubs.infrastructure.database

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

interface JpaClubRepository : MongoRepository<ClubDocument, String>

@Document(collection = "Clubs")
data class ClubDocument(
    @Id
    val name: String
) {
    fun toClub(): Club = Club(name)
}

internal fun Club.toDocument(): ClubDocument = ClubDocument(name)

@Component
class MongoClubDatabase(private val repository: JpaClubRepository): ClubRepository {

    override suspend fun save(club: Club) { repository.save(club.toDocument()) }
    override suspend fun search(): List<Club> = repository.findAll().map { it.toClub() }
}
