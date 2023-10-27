package com.codely.admin.primaryadapter.rest.signin

import arrow.core.raise.fold
import com.codely.admin.application.signin.AdminSignInCommand
import com.codely.admin.application.signin.SignInError
import com.codely.admin.application.signin.SignInError.InvalidCredentials
import com.codely.admin.application.signin.handle
import com.codely.admin.domain.AdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_CREDENTIALS
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@ExperimentalEncodingApi
@RestController
class AdminSignInController(private val repository: AdminRepository) : BaseController() {

    @PostMapping("/admins/sign-in", headers = ["Authorization"])
    fun signIn(@RequestHeader("Authorization") authHeader: String): Response<*> = runBlocking {
        with(repository) {
            val (username, password) = extractCredentials(authHeader)
            fold(
                block = { handle(AdminSignInCommand(password, username)) },
                recover = { error -> error.toServerError() },
                transform = { key -> Response.status(OK).body(key) }
            )
        }
    }

    private fun SignInError.toServerError(): Response<*> =
        when (this) {
            is InvalidCredentials -> Response.status(BAD_REQUEST).withBody(INVALID_CREDENTIALS)
        }

    private fun extractCredentials(authorizationHeaderValue: String) =
        authorizationHeaderValue.removePrefix("Basic ")
            .let { encodedCredentials -> Base64.decode(encodedCredentials) }
            .let { decodedCredentials -> String(decodedCredentials) }
            .split(":")
}
