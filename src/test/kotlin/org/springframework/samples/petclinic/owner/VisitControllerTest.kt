package org.springframework.samples.petclinic.owner

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.samples.petclinic.visit.VisitRepository
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Test class for [VisitController]
 *
 * @author Colin But
 */
@RunWith(SpringRunner::class)
@WebMvcTest(VisitController::class)
class VisitControllerTest {

    @Autowired
    lateinit private var mockMvc: MockMvc

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
        mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID))
                .andExpect(status().isOk)
                .andExpect(view().name("pets/createOrUpdateVisitForm"))
    }

    @Test
    fun testProcessNewVisitFormSuccess() {
        mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
                .param("name", "George")
                .param("description", "Visit Description")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/owners/{ownerId}"))
    }

    @Test
    fun testProcessNewVisitFormHasErrors() {
        mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
                .param("name", "George")
        )
                .andExpect(model().attributeHasErrors("visit"))
                .andExpect(status().isOk)
                .andExpect(view().name("pets/createOrUpdateVisitForm"))
    }

    companion object {
        const val TEST_PET_ID = 1
    }

}
