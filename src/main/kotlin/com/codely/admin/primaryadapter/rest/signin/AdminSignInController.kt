package com.codely.admin.primaryadapter.rest.signin

import arrow.core.raise.fold
import com.codely.admin.application.signin.AdminSignInCommand
import com.codely.admin.application.signin.SignInError
import com.codely.admin.application.signin.SignInError.InvalidCredentials
import com.codely.admin.application.signin.SignInError.Unknown
import com.codely.admin.application.signin.handle
import com.codely.admin.domain.AdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_CREDENTIALS
import com.codely.shared.error.ServerError
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@ExperimentalEncodingApi
@RestController
class AdminSignInController(private val repository: AdminRepository) {

    @PostMapping("/admins/sign-in", headers = ["Authorization"])
    @CrossOrigin(allowCredentials = "true", origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.POST, RequestMethod.OPTIONS])
    fun signIn(@RequestHeader("Authorization") authHeader: String): ResponseEntity<*> = runBlocking {
        with(repository) {
            val (username, password) = extractCredentials(authHeader)
            fold(
                block = { handle(AdminSignInCommand(password, username)) },
                recover = { error -> error.toServerError() },
                transform = { key -> ResponseEntity.status(OK).body(key) }
            )
        }
    }

    private fun SignInError.toServerError(): ResponseEntity<*> =
        when (this) {
            is Unknown -> throw cause
            is InvalidCredentials -> ResponseEntity.status(BAD_REQUEST).body(ServerError.of(INVALID_CREDENTIALS))
        }

    private fun extractCredentials(authorizationHeaderValue: String) =
        authorizationHeaderValue.removePrefix("Basic ")
            .let { encodedCredentials -> Base64.decode(encodedCredentials) }
            .let { decodedCredentials -> String(decodedCredentials) }
            .split(":")
}
