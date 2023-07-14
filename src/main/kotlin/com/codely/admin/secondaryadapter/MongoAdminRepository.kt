package com.codely.admin.secondaryadapter

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.raise.either
import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminFindByCriteria
import com.codely.admin.domain.AdminFindByCriteria.Key
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.secondaryadapter.document.JpaAdminRepository
import com.codely.admin.secondaryadapter.document.toDocument
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class MongoAdminRepository(private val repository: JpaAdminRepository) : AdminRepository {

    override suspend fun save(admin: Admin): Either<Throwable, Unit> = catch { repository.save(admin.toDocument()) }

    override suspend fun findBy(criteria: AdminFindByCriteria): Either<Throwable, Admin> = catch {
        when (criteria) {
            is Key -> repository.findByAccessKey(criteria.accessKey.value).toAdmin()
        }
    }

    override suspend fun signIn(
        username: com.codely.admin.domain.Username,
        password: Password
    ): Either<Throwable, AccessKey> = either {
        val admin = repository.findByUsername(username.value)

        if (BCrypt.checkpw(password.value, admin.password))
            AccessKey(admin.accessKey)
        else raise(Throwable())
    }
}
