package com.codely.admin.secondaryadapter

import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminFindByCriteria
import com.codely.admin.domain.AdminFindByCriteria.ByKey
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.Username
import com.codely.admin.secondaryadapter.document.JpaAdminRepository
import com.codely.admin.secondaryadapter.document.toDocument
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class MongoAdminRepository(private val repository: JpaAdminRepository) : AdminRepository {

    override suspend fun save(admin: Admin) { repository.save(admin.toDocument()) }

    override suspend fun find(criteria: AdminFindByCriteria): Admin? =
        when (criteria) {
            is ByKey -> repository.findByAccessKey(criteria.accessKey.value)?.toAdmin()
        }

    override suspend fun signIn(
        username: Username,
        password: Password
    ): AccessKey? {
        val admin = repository.findByUsername(username.value)

        return if (BCrypt.checkpw(password.value, admin.password)) AccessKey(admin.accessKey)
        else null
    }
}
