package com.codely

import com.codely.agenda.secondaryadapter.database.document.JpaAgendaRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = [JpaAgendaRepository::class])
@EntityScan(basePackageClasses = [JpaAgendaRepository::class])
class TestApplication
