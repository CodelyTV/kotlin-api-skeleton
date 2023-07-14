package com.codely.admin.secondaryadapter.document

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaAdminRepository : MongoRepository<AdminDocument, String> {
    fun findByUsername(username: String): AdminDocument
    fun findByAccessKey(accessKey: String): AdminDocument
}
