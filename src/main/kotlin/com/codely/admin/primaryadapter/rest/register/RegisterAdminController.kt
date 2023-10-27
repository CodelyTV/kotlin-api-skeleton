package com.codely.admin.primaryadapter.rest.register

import arrow.core.raise.fold
import com.codely.admin.application.register.RegisterAdminCommand
import com.codely.admin.application.register.RegisterAdminError
import com.codely.admin.application.register.RegisterAdminError.InvalidUUID
import com.codely.admin.application.register.handle
import com.codely.admin.domain.AccessKeyGenerator
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.PasswordEncrypter
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_IDENTIFIERS
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterAdminController(
    private val repository: AdminRepository,
    private val accessKeyGenerator: AccessKeyGenerator,
    private val passwordEncrypter: PasswordEncrypter
) : BaseController() {

    @PostMapping("/admins/{adminId}")
    fun register(@PathVariable adminId: String, @RequestBody body: RegisterAdminDTO): Response<*> = runBlocking {
        with(repository) {
            with(accessKeyGenerator) {
                with(passwordEncrypter) {
                    fold(
                        block = { handle(RegisterAdminCommand(adminId, body.password, body.username)) },
                        recover = { error -> error.toServerError() },
                        transform = { Response.status(CREATED).body(null) }
                    )
                }
            }
        }
    }

    private fun RegisterAdminError.toServerError(): Response<*> =
        when (this) {
            is InvalidUUID -> Response.status(BAD_REQUEST).withBody(INVALID_IDENTIFIERS)
        }
}
