package org.springframework.samples.petclinic.system


import org.junit.jupiter.api.Disabled
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
@WebMvcTest(controllers = [(CrashController::class)])
class CrashControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    // Waiting https://github.com/spring-projects/spring-boot/issues/5574
    @Disabled
    fun testTriggerException() {
        mockMvc.perform(get("/oups"))
                .andExpect(view().name("exception"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(forwardedUrl("exception")).andExpect(status().isOk)
    }
}
