package com.codely.admin.application.register

import arrow.core.raise.Raise
import com.codely.admin.domain.AccessKeyGenerator
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.PasswordEncrypter
import com.codely.admin.domain.Username
import java.util.UUID

context(AdminRepository, PasswordEncrypter, AccessKeyGenerator, Raise<RegisterAdminError>)
suspend fun register(id: UUID, username: Username, password: Password) {
    val accessKey = generateKey()
    val encryptedPassword = encrypt(password)

    val admin = Admin(id, username, encryptedPassword, accessKey)

    return save(admin)
}

sealed class RegisterAdminError {
    data object InvalidUUID : RegisterAdminError()
}
