package com.codely.integration.healthcheck

import com.codely.shared.Application
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(classes = [Application::class])
@AutoConfigureMockMvc()
class HealthcheckAcceptanceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should successfully access healthcheck`() {

        mockMvc.perform(MockMvcRequestBuilders.get("/health-check"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect {
                assertEquals("OK", it.response.contentAsString)
            }
    }
}
