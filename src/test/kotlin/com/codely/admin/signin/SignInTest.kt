package com.codely.admin.signin

import com.codely.admin.AdminMother
import com.codely.admin.application.signin.AdminSignInCommandResult
import com.codely.admin.fakes.FakeAdminRepository
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_CREDENTIALS
import com.codely.admin.primaryadapter.rest.signin.AdminSignInController
import com.codely.shared.error.ServerError
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK

@ExperimentalEncodingApi
@ExperimentalCoroutinesApi
class SignInTest {

    private val repository = FakeAdminRepository()

    private val controller = AdminSignInController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should perform sign in and return access_key`() = runTest {
        // Given
        repository.save(admin)

        // When
        val result = controller.signIn(authHeader)

        // Then
        assertEquals(OK, result.statusCode)
        assertEquals(expectedResult, result.body)
    }

    @Test
    fun `should fail if credentials are not valid`() = runTest {
        // Given
        repository.save(admin)

        // When
        val result = controller.signIn(invalidAuthHeader)

        // Then
        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_CREDENTIALS), result.body)
    }

    private val admin = AdminMother.random()
    private val expectedResult = AdminSignInCommandResult(admin.key.value)

    private val authHeaderValue = "${admin.username.value}:${admin.password.value}"
    private val authHeader = "Basic ${Base64.encode(authHeaderValue.toByteArray())}"

    private val invalidAuthHeaderValue = "${admin.username.value}:123456"
    private val invalidAuthHeader = "Basic ${Base64.encode(invalidAuthHeaderValue.toByteArray())}"
}
