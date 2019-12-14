package junit5

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AppTest {
    @Test fun `test greeting`() {
        val classUnderTest = App()
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
    }
}
