package org.springframework.samples.petclinic.owner

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.samples.petclinic.visit.VisitRepository
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import java.util.*

/**
 * Test class for [VisitController]
 *
 * @author Colin But
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(VisitController::class)
@Import(ThymeleafAutoConfiguration::class)
class VisitControllerTest(@Autowired private val client: WebTestClient) {

    private val TEST_OWNER_ID = 1
    private val TEST_PET_ID = 1

    @MockBean
    private lateinit var visits: VisitRepository

    @MockBean
    private lateinit var pets: PetRepository

    @BeforeEach
    fun init() {
        given(pets.findById(TEST_PET_ID)).willReturn(Pet())
    }

    @Test
    fun testInitNewVisitForm() {
        client.get().uri("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
                .exchange()
                .expectStatus().isOk
                //.andExpect(view().name("pets/createOrUpdateVisitForm"))
    }

    @Test
    fun testProcessNewVisitFormSuccess() {
        val formData = LinkedMultiValueMap<String, String>(2)
        formData.put("name", Arrays.asList("George"))
        formData.put("description", Arrays.asList("Visit Description"))

        client.post().uri("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection
                .expectHeader().valueEquals("location", "/owners/"+TEST_OWNER_ID)
    }

    @Test
    fun testProcessNewVisitFormHasErrors() {
        val formData = LinkedMultiValueMap<String, String>(1)
        formData.put("name", Arrays.asList("George"))

        client.post().uri("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isOk
                //.andExpect(view().name("pets/createOrUpdateVisitForm"))
    }

}
