package org.springframework.samples.petclinic.owner

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

/**
 * Test class for the [PetController]
 *
 * @author Colin But
 */
const val TEST_OWNER_ID = 1
const val TEST_PET_ID = 1

@WebMvcTest(
    value = [PetController::class],
    includeFilters = [
        ComponentScan.Filter(value = [PetTypeFormatter::class], type = FilterType.ASSIGNABLE_TYPE),
    ],
)
class PetControllerTest(
    private val mockMvc: MockMvc,
    @MockkBean private val pets: PetRepository,
    @MockkBean private val owners: OwnerRepository,
) {

    @BeforeEach
    fun setup() {
        val cat = PetType().apply {
            id = 3
            name = "hamster"
        }
        every { pets.findPetTypes() } returns listOf(cat)
        every { owners.findById(TEST_OWNER_ID) } returns Owner()
        every { owners.findById(9999) } returns null
        every { pets.findById(TEST_PET_ID) } returns Pet()
        every { pets.findById(9999) } returns null
        justRun { pets.save(any()) }
    }

    @Test
    fun testInitCreationForm() {
        mockMvc.get("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
            .andExpect {
                status { isOk() }
                view { name("pets/createOrUpdatePetForm") }
                model { attributeExists("owner") }
            }
    }

    @Test
    fun testInitCreationFormOwnerNotFound() {
        mockMvc.get("/owners/{ownerId}/pets/new", 9999)
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun testProcessCreationFormSuccess() {
        mockMvc.post("/owners/{ownerId}/pets/new", TEST_OWNER_ID) {
            param("name", "Betty")
            param("type", "hamster")
            param("birthDate", "2015-02-12")
        }.andExpect {
            status { is3xxRedirection() }
            view { name("redirect:/owners/{ownerId}") }
        }
    }

    @Test
    fun testProcessCreationFormHasErrors() {
        mockMvc.post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID) {
            param("name", "Betty")
            param("birthDate", "2015-02-12")
        }.andExpect {
            model {
                attributeHasNoErrors("owner")
                attributeHasErrors("pet")
            }
            status { isOk() }
            view { name("pets/createOrUpdatePetForm") }
        }
    }

    @Test
    fun testInitUpdateForm() {
        mockMvc.get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
            .andExpect {
                status { isOk() }
                model { attributeExists("owner") }
                view { name("pets/createOrUpdatePetForm") }
            }
    }

    @Test
    fun testInitUpdateFormPetNotFound() {
        mockMvc.get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, 9999)
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun testProcessUpdateFormSuccess() {
        mockMvc.post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID) {
            param("name", "Betty")
            param("type", "hamster")
            param("birthDate", "2015-02-12")
        }.andExpect {
            status { is3xxRedirection() }
            view { name("redirect:/owners/{ownerId}") }
        }
    }

    @Test
    fun testProcessUpdateFormHasErrors() {
        mockMvc.post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID) {
            param("name", "Betty")
            param("birthDate", "2015-02-12")
        }.andExpect {
            model {
                attributeHasNoErrors("owner")
                attributeHasErrors("pet")
            }
            status { isOk() }
            view { name("pets/createOrUpdatePetForm") }
        }
    }
}
