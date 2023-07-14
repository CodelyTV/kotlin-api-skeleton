package com.codely.admin.application.register

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.admin.domain.AccessKeyGenerator
import com.codely.admin.application.register.RegisterAdminError.InvalidUUID
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.PasswordEncrypter
import com.codely.admin.domain.Username
import java.util.UUID

context(AdminRepository, PasswordEncrypter, AccessKeyGenerator, Raise<RegisterAdminError>)
suspend fun handle(command: RegisterAdminCommand) {
    val id = catch({ UUID.fromString(command.id) }) { raise(InvalidUUID) }

    return register(id = id, username = Username(command.username), password = Password(command.password))
}

data class RegisterAdminCommand(
    val id: String,
    val password: String,
    val username: String
)
