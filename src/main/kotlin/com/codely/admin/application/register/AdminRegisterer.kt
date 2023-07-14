package com.codely.admin.application.register

import arrow.core.raise.Raise
import com.codely.admin.application.register.RegisterAdminError.Unknown
import com.codely.admin.domain.AccessKeyGenerator
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.PasswordEncrypter
import com.codely.admin.domain.Username
import com.codely.admin.domain.encryptOrElse
import com.codely.admin.domain.generateKeyOrElse
import com.codely.admin.domain.saveOrElse
import java.util.UUID

context(AdminRepository, PasswordEncrypter, AccessKeyGenerator, Raise<RegisterAdminError>)
suspend fun register(id: UUID, username: Username, password: Password) {
    val accessKey = generateKeyOrElse { error -> Unknown(error) }.bind()
    val encryptedPassword = password.encryptOrElse { error -> Unknown(error) }.bind()

    return Admin(id, username, encryptedPassword, accessKey)
        .saveOrElse { error -> Unknown(error) }
        .map { }
        .bind()
}

sealed class RegisterAdminError {
    data object InvalidUUID : RegisterAdminError()
    class Unknown(val cause: Throwable) : RegisterAdminError()
}
