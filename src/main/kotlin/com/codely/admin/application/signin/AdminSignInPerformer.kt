package com.codely.admin.application.signin

import arrow.core.raise.Raise
import com.codely.admin.application.signin.SignInError.InvalidCredentials
import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.Username
import com.codely.admin.domain.signInOrElse

context(AdminRepository, Raise<SignInError>)
suspend fun adminSignIn(username: Username, password: Password): AccessKey =
    signInOrElse(username, password) { InvalidCredentials }

sealed class SignInError {
    data object InvalidCredentials : SignInError()
}
