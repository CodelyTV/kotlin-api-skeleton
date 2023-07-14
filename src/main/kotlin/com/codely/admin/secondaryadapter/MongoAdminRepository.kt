package com.codely.admin.secondaryadapter

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminFindByCriteria
import com.codely.admin.domain.AdminFindByCriteria.Key
import com.codely.admin.domain.AdminFindByCriteria.Username
import com.codely.admin.domain.AdminRepository
import com.codely.admin.secondaryadapter.document.JpaAdminRepository
import com.codely.admin.secondaryadapter.document.toDocument
import org.springframework.stereotype.Component

@Component
class MongoAdminRepository(private val repository: JpaAdminRepository) : AdminRepository {

    override suspend fun save(admin: Admin): Either<Throwable, Unit> = catch { repository.save(admin.toDocument()) }

    override suspend fun findBy(criteria: AdminFindByCriteria): Either<Throwable, Admin> = catch {
        when (criteria) {
            is Key -> repository.findByAccessKey(criteria.accessKey.value).toAdmin()
            is Username -> repository.findByUsername(criteria.username.value).toAdmin()
        }
    }
}
