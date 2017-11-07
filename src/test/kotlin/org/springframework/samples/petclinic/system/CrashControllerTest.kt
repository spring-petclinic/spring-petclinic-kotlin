package org.springframework.samples.petclinic.system


import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * Test class for [CrashController]
 *
 * @author Colin But
 * @author Antoine Rey
 */
@RunWith(SpringRunner::class)
@WebFluxTest(CrashController::class)
@Import(ThymeleafAutoConfiguration::class, ErrorWebFluxAutoConfiguration::class)
class CrashControllerTest {

    @Autowired
    lateinit private var client: WebTestClient;

    @Test
    fun testTriggerException() {
        val res = client.get().uri("/oups")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().is5xxServerError
                .expectBody(String::class.java).returnResult()

        assertThat(res.responseBody).contains("<h2>Something happened...</h2>")
    }
}
