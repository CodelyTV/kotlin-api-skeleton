package com.codely.admin.register

import com.codely.admin.AdminMother
import com.codely.admin.fakes.FakeAccessKeyGenerator
import com.codely.admin.fakes.FakeAdminRepository
import com.codely.admin.fakes.FakePasswordEncrypter
import com.codely.admin.primaryadapter.rest.error.AdminServerErrors.INVALID_IDENTIFIERS
import com.codely.admin.primaryadapter.rest.register.RegisterAdminController
import com.codely.admin.primaryadapter.rest.register.RegisterAdminDTO
import com.codely.shared.error.ServerError
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED

class RegisterAdminTest {

    private val repository = FakeAdminRepository()
    private val accessKeyGenerator = FakeAccessKeyGenerator()
    private val passwordEncrypter = FakePasswordEncrypter()

    private val controller = RegisterAdminController(repository, accessKeyGenerator, passwordEncrypter)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should register admin`() {
        // Given
        accessKeyGenerator.generateKey(admin.key)

        // When
        val result = controller.register(admin.id.toString(), requestBody)

        // Then
        assertEquals(CREATED, result.statusCode)
    }

    @Test
    fun `should fail admin registration if identifier is not UUID`() {
        // When
        val result = controller.register("122234", requestBody)

        // Then
        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_IDENTIFIERS), result.body)
    }

    private val admin = AdminMother.random()
    private val requestBody = RegisterAdminDTO(admin.username.value, admin.password.value)
}
