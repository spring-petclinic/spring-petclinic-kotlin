package org.springframework.samples.petclinic.owner


import org.assertj.core.api.Assertions
import org.assertj.core.util.Lists
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import java.util.*

/**
 * Test class for the [PetController]
 *
 * @author Colin But
 */
const val TEST_OWNER_ID = 1
const val TEST_PET_ID = 1

@RunWith(SpringRunner::class)
@WebFluxTest(PetController::class, includeFilters = arrayOf(ComponentScan.Filter(value = PetTypeFormatter::class, type = FilterType.ASSIGNABLE_TYPE)))
@Import(ThymeleafAutoConfiguration::class)
class PetControllerTest {

    @Autowired
    lateinit private var client: WebTestClient;

    @MockBean
    lateinit private var pets: PetRepository

    @MockBean
    lateinit private var owners: OwnerRepository

    @Before
    fun setup() {
        val cat = PetType()
        cat.id = 3
        cat.name = "hamster"
        given(this.pets.findPetTypes()).willReturn(Lists.newArrayList(cat))
        given(this.owners.findById(TEST_OWNER_ID)).willReturn(Owner())
        given(this.pets.findById(TEST_PET_ID)).willReturn(Pet())

    }

    @Test
    fun testInitCreationForm() {
        client.get().uri("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun testProcessCreationFormSuccess() {
        val formData = LinkedMultiValueMap<String, String>(3)
        formData.put("name", Arrays.asList("Betty"))
        formData.put("type", Arrays.asList("hamster"))
        formData.put("birthDate", Arrays.asList("2015-02-12"))

        client.post().uri("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection
                .expectHeader().valueEquals("location", "/owners/"+TEST_OWNER_ID)
    }

    @Test
    fun testProcessCreationFormHasErrors() {
        val formData = LinkedMultiValueMap<String, String>(2)
        formData.put("name", Arrays.asList("Betty"))
        formData.put("birthDate", Arrays.asList("2015-02-12"))

        val responseBody = client.post().uri("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java).returnResult().responseBody

        Assertions.assertThat(responseBody).contains("is required")
    }

    @Test
    fun testInitUpdateForm() {
        client.get().uri("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun testProcessUpdateFormSuccess() {
        val formData = LinkedMultiValueMap<String, String>(3)
        formData.put("name", Arrays.asList("Betty"))
        formData.put("type", Arrays.asList("hamster"))
        formData.put("birthDate", Arrays.asList("2015-02-12"))

        client.post().uri("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection
                .expectHeader().valueEquals("location", "/owners/"+TEST_OWNER_ID)
    }

    @Test
    fun testProcessUpdateFormHasErrors() {
        val formData = LinkedMultiValueMap<String, String>(2)
        formData.put("name", Arrays.asList("Betty"))
        formData.put("birthDate", Arrays.asList("2015-02-12"))

        val responseBody = client.post().uri("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java).returnResult().responseBody

        Assertions.assertThat(responseBody).contains("is required")
    }

}
