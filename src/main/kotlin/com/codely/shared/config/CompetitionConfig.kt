package com.codely.shared.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompetitionConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "competition")
    fun competitionUrls(): CompetitionConfig = CompetitionConfig()
}

data class CompetitionConfig(
    val preferente: Liga = Liga(),
    val primera: Liga = Liga(),
    val segundaA: Liga = Liga(),
    val segundaB: Liga = Liga(),
    val terceraA: Liga = Liga(),
    val terceraB: Liga = Liga()
)

data class Liga(
    var ranking: String = "",
    var name: String = "",
    var players: MutableMap<String, String> = mutableMapOf()
)
