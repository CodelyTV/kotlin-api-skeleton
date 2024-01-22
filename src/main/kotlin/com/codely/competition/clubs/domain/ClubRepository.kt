package com.codely.competition.clubs.domain

interface ClubRepository {
    suspend fun save(club: Club)
    suspend fun search(): List<Club>
}
