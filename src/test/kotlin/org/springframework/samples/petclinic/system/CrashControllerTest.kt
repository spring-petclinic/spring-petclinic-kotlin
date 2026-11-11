package org.springframework.samples.petclinic.system

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * Test class for [CrashController]
 *
 * @author Colin But
 */
@ActiveProfiles("test")
@WebMvcTest(controllers = [MockMvcValidationConfiguration::class, CrashController::class])
class CrashControllerTest(private val mockMvc: MockMvc) {

    @Test
    fun testTriggerException() {
        mockMvc.get("/oups")
            .andExpect {
                view { name("error") }
                model { attributeExists("error") }
                content {
                    string(containsString("PetClinic :: a Spring Framework demonstration"))
                    string(containsString("Something happened..."))
                    string(
                        containsString(
                            "Expected: controller used to showcase what happens when an exception is thrown",
                        ),
                    )
                }
            }
    }
}
