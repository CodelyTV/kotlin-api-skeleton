package com.codely.admin.domain

import arrow.core.raise.Raise

interface AdminRepository {
    suspend fun save(admin: Admin)
    suspend fun find(criteria: AdminFindByCriteria): Admin?
    suspend fun signIn(username: Username, password: Password): AccessKey?
}

sealed class AdminFindByCriteria {
    class ByKey(val accessKey: AccessKey) : AdminFindByCriteria()
}

suspend fun AdminRepository.save(admin: Admin): Admin = save(admin).let { admin }

context(Raise<Error>)
suspend fun <Error> AdminRepository.findByOrElse(criteria: AdminFindByCriteria, onError: () -> Error): Admin =
    find(criteria) ?: raise(onError())

context(Raise<Error>)
suspend fun <Error> AdminRepository.signInOrElse(username: Username, password: Password, onError: () -> Error): AccessKey =
    signIn(username, password) ?: raise(onError())
