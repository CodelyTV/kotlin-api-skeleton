package com.codely.agenda.database

import com.codely.agenda.secondaryadapter.database.document.JpaAgendaRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container


@Configuration
class MongoContainerConfig {

    companion object {
        @Container
        val mongoDBContainer = MongoDBContainer("mongo:latest").withExposedPorts(27017)

        @JvmStatic
        @DynamicPropertySource
        fun mongoProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongo.host") { mongoDBContainer.host }
            registry.add("spring.data.mongo.port") { mongoDBContainer.firstMappedPort }
            registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
            registry.add("spring.data.mongo.database") { "app_db" }
            registry.add("spring.data.mongo.username") { "datm_root" }
            registry.add("spring.data.mongo.password") { "datm_root_password" }
        }

        @JvmStatic
        @BeforeAll
        fun setUp() {
            mongoDBContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            mongoDBContainer.stop()
        }
    }
}
