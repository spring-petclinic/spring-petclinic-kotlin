package org.springframework.samples.petclinic.owner

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.samples.petclinic.visit.VisitRepository
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

/**
 * Test class for [VisitController]
 *
 * @author Colin But
 */
@WebMvcTest(VisitController::class)
class VisitControllerTest(
    private val mockMvc: MockMvc,
    @MockkBean private val visits: VisitRepository,
    @MockkBean private val pets: PetRepository,
) {

    @BeforeEach
    fun init() {
        every { pets.findById(TEST_PET_ID) } returns Pet()
        every { pets.findById(9999) } returns null
        justRun { visits.save(any()) }
    }

    @Test
    fun testInitNewVisitForm() {
        mockMvc.get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
            .andExpect {
                status { isOk() }
                view { name("pets/createOrUpdateVisitForm") }
            }
    }

    @Test
    fun testInitNewVisitFormPetNotFound() {
        mockMvc.get("/owners/*/pets/{petId}/visits/new", 9999)
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun testProcessNewVisitFormSuccess() {
        mockMvc.post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID) {
            param("name", "George")
            param("description", "Visit Description")
        }.andExpect {
            status { is3xxRedirection() }
            view { name("redirect:/owners/{ownerId}") }
        }
    }

    @Test
    fun testProcessNewVisitFormHasErrors() {
        mockMvc.post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID) {
            param("name", "George")
        }.andExpect {
            model { attributeHasErrors("visit") }
            status { isOk() }
            view { name("pets/createOrUpdateVisitForm") }
        }
    }
}
