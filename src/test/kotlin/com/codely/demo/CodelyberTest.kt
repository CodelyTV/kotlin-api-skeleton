package com.codely.demo

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class CodelyberTest {
    @Test
    fun `it should greet successfully`() {
        val expectedGreet = "Welcome to kotlin skeleton!!!"
        assertEquals(expectedGreet, Codelyber().greet())
    }
}
