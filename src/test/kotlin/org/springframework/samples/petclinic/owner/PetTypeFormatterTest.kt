package org.springframework.samples.petclinic.owner

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.ParseException
import java.util.Locale

/**
 * Test class for [PetTypeFormatter]
 *
 * @author Colin But
 */
class PetTypeFormatterTest {

    private val pets = mockk<PetRepository>()

    private val petTypeFormatter = PetTypeFormatter(pets)

    @Test
    fun testPrint() {
        val petType = PetType().apply { name = "Hamster" }
        val petTypeName = petTypeFormatter.print(petType, Locale.ENGLISH)
        assertThat(petTypeName).isEqualTo("Hamster")
    }

    @Test
    fun shouldParse() {
        every { pets.findPetTypes() } returns makePetTypes()
        val petType = petTypeFormatter.parse("Bird", Locale.ENGLISH)
        assertThat(petType.name).isEqualTo("Bird")
    }

    @Test
    fun shouldThrowParseException() {
        every { pets.findPetTypes() } returns makePetTypes()
        assertThrows<ParseException> {
            petTypeFormatter.parse("Fish", Locale.ENGLISH)
        }
    }

    /**
     * Helper method to produce some sample owner types just for test purpose
     *
     * @return [List] of [PetType]
     */
    private fun makePetTypes(): List<PetType> = listOf(
        PetType().apply { name = "Dog" },
        PetType().apply { name = "Bird" },
    )
}
