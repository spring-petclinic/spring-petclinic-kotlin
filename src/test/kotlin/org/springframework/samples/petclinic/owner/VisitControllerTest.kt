package org.springframework.samples.petclinic.owner

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.samples.petclinic.visit.VisitRepository
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import java.util.*

/**
 * Test class for [VisitController]
 *
 * @author Colin But
 */
@RunWith(SpringRunner::class)
@WebFluxTest(VisitController::class)
@Import(ThymeleafAutoConfiguration::class)
class VisitControllerTest {

    @Autowired
    lateinit private var client: WebTestClient;

    @MockBean
    lateinit private var visits: VisitRepository

    @MockBean
    lateinit private var pets: PetRepository

    @Before
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

    companion object {
        const val TEST_PET_ID = 1
    }

}
