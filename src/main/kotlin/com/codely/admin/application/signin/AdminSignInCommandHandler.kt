package com.codely.admin.application.signin

import arrow.core.raise.Raise
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.Username

context(AdminRepository, Raise<SignInError>)
suspend fun handle(command: AdminSignInCommand): AdminSignInCommandResult =
    adminSignIn(
        username = Username(value = command.username),
        password = Password(value = command.password)
    ).let { key -> AdminSignInCommandResult(key.value) }

data class AdminSignInCommand(
    val password: String,
    val username: String
)

data class AdminSignInCommandResult(
    val accessKey: String
)
