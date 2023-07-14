package com.codely.admin.domain

import arrow.core.Either

interface AdminRepository {
    suspend fun save(admin: Admin): Either<Throwable, Unit>
    suspend fun findBy(criteria: AdminFindByCriteria): Either<Throwable, Admin>
}

sealed class AdminFindByCriteria {
    class Username(val username: com.codely.admin.domain.Username) : AdminFindByCriteria()
    class Key(val accessKey: AccessKey) : AdminFindByCriteria()
}

context(AdminRepository)
suspend fun <T> Admin.saveOrElse(onError: (cause: Throwable) -> T): Either<T, Admin> =
    save(this)
        .map { this }
        .mapLeft { error -> onError(error) }

context(AdminRepository)
suspend fun <T> findByOrElse(criteria: AdminFindByCriteria, onError: (cause: Throwable) -> T): Either<T, Admin> =
    findBy(criteria)
        .mapLeft { error -> onError(error) }
