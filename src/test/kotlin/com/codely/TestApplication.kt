package com.codely

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.codely"])
@EntityScan(basePackages = ["com.codely"])
class TestApplication
