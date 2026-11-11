package org.springframework.samples.petclinic.owner

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.samples.petclinic.visit.Visit
import org.springframework.samples.petclinic.visit.VisitRepository
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate

/**
 * Test class for [OwnerController]
 *
 * @author Colin But
 */
@WebMvcTest(OwnerController::class)
class OwnerControllerTest(
    private val mockMvc: MockMvc,
    @MockkBean private val owners: OwnerRepository,
    @MockkBean private val visits: VisitRepository,
) {

    private lateinit var george: Owner

    @BeforeEach
    fun setup() {
        george = Owner().apply {
            id = TEST_OWNER_ID
            firstName = "George"
            lastName = "Franklin"
            address = "110 W. Liberty St."
            city = "Madison"
            telephone = "6085551023"
        }
        val max = Pet().apply {
            type = PetType().apply { name = "dog" }
            name = "Max"
            birthDate = LocalDate.now()
        }
        george.addPet(max)
        max.id = 1
        every { owners.findById(TEST_OWNER_ID) } returns george
        every { owners.findById(9999) } returns null
        justRun { owners.save(any()) }
        val visit = Visit().apply { date = LocalDate.now() }
        every { visits.findByPetId(1) } returns setOf(visit)
    }

    @Test
    fun testInitCreationForm() {
        mockMvc.get("/owners/new")
            .andExpect {
                status { isOk() }
                model { attributeExists("owner") }
                view { name("owners/createOrUpdateOwnerForm") }
            }
    }

    @Test
    fun testProcessCreationFormSuccess() {
        mockMvc.post("/owners/new") {
            param("firstName", "Joe")
            param("lastName", "Bloggs")
            param("address", "123 Caramel Street")
            param("city", "London")
            param("telephone", "01316761638")
        }.andExpect {
            status { is3xxRedirection() }
        }
    }

    @Test
    fun testProcessCreationFormHasErrors() {
        mockMvc.post("/owners/new") {
            param("firstName", "Joe")
            param("lastName", "Bloggs")
            param("city", "London")
        }.andExpect {
            status { isOk() }
            model {
                attributeHasErrors("owner")
                attributeHasFieldErrors("owner", "address")
                attributeHasFieldErrors("owner", "telephone")
            }
            view { name("owners/createOrUpdateOwnerForm") }
        }
    }

    @Test
    fun testInitFindForm() {
        mockMvc.get("/owners/find")
            .andExpect {
                status { isOk() }
                model { attributeExists("owner") }
                view { name("owners/findOwners") }
            }
    }

    @Test
    fun testProcessFindFormSuccess() {
        every { owners.findByLastName("") } returns listOf(george, Owner())
        mockMvc.get("/owners")
            .andExpect {
                status { isOk() }
                view { name("owners/ownersList") }
            }
    }

    @Test
    fun testProcessFindFormByLastName() {
        every { owners.findByLastName(george.lastName) } returns listOf(george)
        mockMvc.get("/owners") {
            param("lastName", "Franklin")
        }.andExpect {
            status { is3xxRedirection() }
            view { name("redirect:/owners/$TEST_OWNER_ID") }
        }
    }

    @Test
    fun testProcessFindFormNoOwnersFound() {
        every { owners.findByLastName("Unknown Surname") } returns emptyList()
        mockMvc.get("/owners") {
            param("lastName", "Unknown Surname")
        }.andExpect {
            status { isOk() }
            model {
                attributeHasFieldErrors("owner", "lastName")
                attributeHasFieldErrorCode("owner", "lastName", "notFound")
            }
            view { name("owners/findOwners") }
        }
    }

    @Test
    fun testInitUpdateOwnerForm() {
        mockMvc.get("/owners/{ownerId}/edit", TEST_OWNER_ID)
            .andExpect {
                status { isOk() }
                model {
                    attributeExists("owner")
                    attribute("owner", hasProperty("lastName", equalTo("Franklin")))
                    attribute("owner", hasProperty("firstName", equalTo("George")))
                    attribute("owner", hasProperty("address", equalTo("110 W. Liberty St.")))
                    attribute("owner", hasProperty("city", equalTo("Madison")))
                    attribute("owner", hasProperty("telephone", equalTo("6085551023")))
                }
                view { name("owners/createOrUpdateOwnerForm") }
            }
    }

    @Test
    fun testInitUpdateOwnerFormNotFound() {
        mockMvc.get("/owners/{ownerId}/edit", 9999)
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun testProcessUpdateOwnerFormSuccess() {
        mockMvc.post("/owners/{ownerId}/edit", TEST_OWNER_ID) {
            param("firstName", "Joe")
            param("lastName", "Bloggs")
            param("address", "123 Caramel Street")
            param("city", "London")
            param("telephone", "01616291589")
        }.andExpect {
            status { is3xxRedirection() }
            view { name("redirect:/owners/{ownerId}") }
        }
    }

    @Test
    fun testProcessUpdateOwnerFormHasErrors() {
        mockMvc.post("/owners/{ownerId}/edit", TEST_OWNER_ID) {
            param("firstName", "Joe")
            param("lastName", "Bloggs")
            param("city", "London")
        }.andExpect {
            status { isOk() }
            model {
                attributeHasErrors("owner")
                attributeHasFieldErrors("owner", "address")
                attributeHasFieldErrors("owner", "telephone")
            }
            view { name("owners/createOrUpdateOwnerForm") }
        }
    }

    @Test
    fun testShowOwner() {
        mockMvc.get("/owners/{ownerId}", TEST_OWNER_ID)
            .andExpect {
                status { isOk() }
                model {
                    attribute("owner", hasProperty("lastName", equalTo("Franklin")))
                    attribute("owner", hasProperty("firstName", equalTo("George")))
                    attribute("owner", hasProperty("address", equalTo("110 W. Liberty St.")))
                    attribute("owner", hasProperty("city", equalTo("Madison")))
                    attribute("owner", hasProperty("telephone", equalTo("6085551023")))
                    attribute("owner", hasProperty("pets", not(empty<Pet>())))
                }
                view { name("owners/ownerDetails") }
            }

        verify { visits.findByPetId(1) }
    }

    @Test
    fun testShowOwnerNotFound() {
        mockMvc.get("/owners/{ownerId}", 9999)
            .andExpect {
                status { isNotFound() }
            }
    }

    companion object {

        private const val TEST_OWNER_ID = 1
    }
}
