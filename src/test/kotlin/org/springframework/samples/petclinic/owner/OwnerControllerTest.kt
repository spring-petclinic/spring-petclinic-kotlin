package org.springframework.samples.petclinic.owner


import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.util.Lists
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import java.util.*


/**
 * Test class for [OwnerController]
 *
 * @author Colin But
 */
@RunWith(SpringRunner::class)
@WebFluxTest(OwnerController::class)
@Import(ThymeleafAutoConfiguration::class)
class OwnerControllerTest {

    @Autowired
    lateinit private var client: WebTestClient;

    @MockBean
    lateinit private var owners: OwnerRepository

    lateinit private var george: Owner

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
        george = Owner()
        george.id = TEST_OWNER_ID
        george.firstName = "George"
        george.lastName = "Franklin"
        george.address = "110 W. Liberty St."
        george.city = "Madison"
        george.telephone = "6085551023"
        given(owners.findById(TEST_OWNER_ID)).willReturn(george)
    }

    @Test
    fun testInitCreationForm() {
        client.get().uri("/owners/new")
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun testProcessCreationFormSuccess() {
        val formData = LinkedMultiValueMap<String, String>(5)
        formData.put("firstName", Arrays.asList("Joe"))
        formData.put("lastName", Arrays.asList("Bloggs"))
        formData.put("address", Arrays.asList("123 Caramel Street"))
        formData.put("city", Arrays.asList("London"))
        formData.put("telephone", Arrays.asList("01316761638"))
        client.post().uri("/owners/new")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection
    }

    @Test
    fun testProcessCreationFormHasErrors() {
        val formData = LinkedMultiValueMap<String, String>(5)
        formData.put("firstName", Arrays.asList("Joe"))
        formData.put("lastName", Arrays.asList("Bloggs"))
        formData.put("address", Arrays.asList("123 Caramel Street"))
        formData.put("city", Arrays.asList("London"))
        val res = client.post().uri("/owners/new")
                .header("Accept-Language", "en-US")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java).returnResult()

        Assertions.assertThat(res.responseBody).contains("numeric value out of bounds (&lt;10 digits&gt;.&lt;0 digits&gt; expected")
        Assertions.assertThat(res.responseBody).contains("must not be empty")
    }

    @Test
    fun testInitFindForm() {
        client.get().uri("/owners/find")
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun testProcessFindFormSuccess() {
        given(owners.findByLastName("")).willReturn(Lists.newArrayList<Owner>(george, Owner()))
        client.get().uri("/owners")
                .exchange()
                .expectStatus().isOk
    }

    @Test
    @Ignore
    fun testProcessFindFormByLastName() {
        given(owners.findByLastName(george.lastName)).willReturn(Lists.newArrayList<Owner>(george))

        // How to submit data into a GET with the Webflux client?
        client.get().uri("/owners")
                .attribute("lastname", "Franklin")
                .exchange()
                .expectStatus().is3xxRedirection
                .expectHeader().valueEquals("location", "/owners/" + TEST_OWNER_ID)
    }

    @Test
    @Ignore
    fun testProcessFindFormNoOwnersFound() {

        // How to submit data into a GET with the Webflux client?
        client.get().uri("/owners")
                .attribute("lastName", "Unknown Surname")
                .exchange()
                .expectStatus().isOk
        //.andExpect(model().attributeHasFieldErrors("owner", "lastName"))
        //.andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
        //.andExpect(view().name("owners/findOwners"))
    }

    @Test
    fun testInitUpdateOwnerForm() {
        val res = client.get().uri("/owners/{ownerId}/edit", TEST_OWNER_ID)
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java).returnResult()

        assertThat(res.responseBody).contains("<input class=\"form-control\" type=\"text\" id=\"firstName\" name=\"firstName\" value=\"George\" />")
        assertThat(res.responseBody).contains("<input class=\"form-control\" type=\"text\" id=\"lastName\" name=\"lastName\" value=\"Franklin\" />")
        assertThat(res.responseBody).contains("<input class=\"form-control\" type=\"text\" id=\"address\" name=\"address\" value=\"110 W. Liberty St.\" />")
        assertThat(res.responseBody).contains("<input class=\"form-control\" type=\"text\" id=\"city\" name=\"city\" value=\"Madison\" />")
        assertThat(res.responseBody).contains("<input class=\"form-control\" type=\"text\" id=\"telephone\" name=\"telephone\" value=\"6085551023\" />")
    }

    @Test
    fun testProcessUpdateOwnerFormSuccess() {
        val formData = LinkedMultiValueMap<String, String>(5)
        formData.put("firstName", Arrays.asList("Joe"))
        formData.put("lastName", Arrays.asList("Bloggs"))
        formData.put("address", Arrays.asList("123 Caramel Street"))
        formData.put("city", Arrays.asList("London"))
        formData.put("telephone", Arrays.asList("01316761638"))

        client.post().uri("/owners/{ownerId}/edit", TEST_OWNER_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection
                .expectHeader().valueEquals("location", "/owners/" + TEST_OWNER_ID)
    }

    @Test
    fun testProcessUpdateOwnerFormHasErrors() {
        val formData = LinkedMultiValueMap<String, String>(5)
        formData.put("firstName", Arrays.asList("Joe"))
        formData.put("lastName", Arrays.asList("Bloggs"))
        formData.put("address", Arrays.asList("123 Caramel Street"))
        formData.put("telephone", Arrays.asList("01316761638"))

        var res = client.post().uri("/owners/{ownerId}/edit", TEST_OWNER_ID)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java).returnResult()

        assertThat(res.responseBody).contains("must not be empty")
    }

    @Test
    fun testShowOwner() {
        var res = client.get().uri("/owners/{ownerId}", TEST_OWNER_ID)
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java).returnResult()

        assertThat(res.responseBody).contains("<td><b>George Franklin</b></td>")
        assertThat(res.responseBody).contains("<td>110 W. Liberty St.</td>")
        assertThat(res.responseBody).contains("<td>Madison</td>")
        assertThat(res.responseBody).contains("<td>6085551023</td>")
    }

    companion object {

        private val TEST_OWNER_ID = 1
    }

}
