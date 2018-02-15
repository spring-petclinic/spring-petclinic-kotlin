package org.springframework.samples.petclinic.vet

import org.aspectj.lang.annotation.Before
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.xml.HasXPath.hasXPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


/**
 * Test class for the [VetController]
 */
@ExtendWith(SpringExtension::class)
@WebFluxTest(VetController::class)
@Import(ThymeleafAutoConfiguration::class)
class VetControllerTest(@Autowired private val client: WebTestClient) {

    @MockBean
    private lateinit var vets: VetRepository

    @BeforeEach
    fun setup() {
        val james = Vet()
        james.firstName = "James"
        james.lastName = "Carter"
        james.id = 1
        val helen = Vet()
        helen.firstName = "Helen"
        helen.lastName = "Leary"
        helen.id = 2
        val radiology = Specialty()
        radiology.id = 1
        radiology.name = "radiology"
        helen.addSpecialty(radiology)
        given(this.vets.findAll()).willReturn(listOf(james, helen))
    }

    @Test
    fun testShowVetListHtml() {
        val html = client.get().uri("/vets.html")
                .exchange()
                .expectStatus().isOk
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        assertThat(html, CoreMatchers.containsString("<td>James Carter</td>"))
        assertThat(html, CoreMatchers.containsString("<td>Helen Leary</td>"))
    }

    @Test
    fun testShowVetListJson() {
        client.get().uri("/vets.json")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("\$.vetList[0].id").isEqualTo(1)

    }

    @Test
    fun testShowVetListXml() {
        val result = client.get().uri("/vets.xml")
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_XML)
                .returnResult(String::class.java)

        val node = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(InputSource(StringReader(result.responseBody.blockFirst())))
        assertThat(node, hasXPath("/vets/vetList[id=1]/id"))
    }

}
