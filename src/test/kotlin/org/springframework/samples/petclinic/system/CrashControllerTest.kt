package org.springframework.samples.petclinic.system


import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Test class for [CrashController]
 *
 * @author Colin But
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [MockMvcValidationConfiguration::class, CrashController::class])
class CrashControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun testTriggerException() {
        mockMvc.perform(get("/oups"))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"))
                .andExpect(content().string(containsString("Whitelabel Error Page")))
                .andExpect(content().string(containsString("Expected: controller used to showcase what happens when an exception is thrown")))
    }
}
