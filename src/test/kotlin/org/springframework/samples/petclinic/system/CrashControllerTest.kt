package org.springframework.samples.petclinic.system


import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Test class for [CrashController]
 *
 * @author Colin But
 */
@RunWith(SpringRunner::class)
@WebMvcTest(controllers = arrayOf(CrashController::class))
class CrashControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    // Waiting https://github.com/spring-projects/spring-boot/issues/5574
    @Ignore
    fun testTriggerException() {
        mockMvc.perform(get("/oups"))
                .andExpect(view().name("exception"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(forwardedUrl("exception")).andExpect(status().isOk)
    }
}
