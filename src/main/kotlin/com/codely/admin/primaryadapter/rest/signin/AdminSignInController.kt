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
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminSignInController(private val repository: AdminRepository) {

    @PostMapping("/admins/sign-in")
    fun signIn(@RequestBody body: SignInDTO): ResponseEntity<*> = runBlocking {
        with(repository) {
            fold(
                block = { handle(AdminSignInCommand(body.password, body.username)) },
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
}
