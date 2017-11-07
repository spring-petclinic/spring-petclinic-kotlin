package org.springframework.samples.petclinic.vet

import org.assertj.core.util.Lists
import org.hamcrest.CoreMatchers
import org.hamcrest.xml.HasXPath.hasXPath
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


/**
 * Test class for the [VetController]
 */
@RunWith(SpringRunner::class)
@WebFluxTest(VetController::class)
@Import(ThymeleafAutoConfiguration::class)
class VetControllerTest {

    @Autowired
    lateinit private var client: WebTestClient;

    @MockBean
    lateinit private var vets: VetRepository

    @Before
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
        given(this.vets.findAll()).willReturn(Lists.newArrayList(james, helen))
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
