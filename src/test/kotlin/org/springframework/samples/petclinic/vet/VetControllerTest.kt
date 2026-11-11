package org.springframework.samples.petclinic.vet

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.xml.HasXPath.hasXPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * Test class for the [VetController]
 */
@WebMvcTest(VetController::class)
class VetControllerTest(private val mockMvc: MockMvc, @MockkBean private val vets: VetRepository) {

    @BeforeEach
    fun setup() {
        val james = Vet().apply {
            firstName = "James"
            lastName = "Carter"
            id = 1
        }
        val helen = Vet().apply {
            firstName = "Helen"
            lastName = "Leary"
            id = 2
            addSpecialty(
                Specialty().apply {
                    id = 1
                    name = "radiology"
                },
            )
        }
        every { vets.findAll() } returns listOf(james, helen)
    }

    @Test
    fun testShowVetListHtml() {
        mockMvc.get("/vets.html")
            .andExpect {
                status { isOk() }
                model { attributeExists("vets") }
                view { name("vets/vetList") }
            }
    }

    @Test
    fun testShowResourcesVetList() {
        mockMvc.get("/vets.json") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.vetList[0].id") { value(1) }
        }
    }

    @Test
    fun testShowVetListXml() {
        mockMvc.get("/vets.xml") {
            accept = MediaType.APPLICATION_XML
        }.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_XML_VALUE)
                node(hasXPath("/vets/vetList[id=1]/id"))
            }
        }
    }
}
