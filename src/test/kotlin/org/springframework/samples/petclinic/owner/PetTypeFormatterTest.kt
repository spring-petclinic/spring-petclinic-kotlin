package org.springframework.samples.petclinic.owner


import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.text.ParseException
import java.util.*

/**
 * Test class for [PetTypeFormatter]
 *
 * @author Colin But
 */
@RunWith(MockitoJUnitRunner::class)
class PetTypeFormatterTest {

    @Mock
    lateinit private var pets: PetRepository

    lateinit private var petTypeFormatter: PetTypeFormatter

    @Before
    fun setup() {
        this.petTypeFormatter = PetTypeFormatter(pets)
    }

    @Test
    fun testPrint() {
        val petType = PetType()
        petType.name = "Hamster"
        val petTypeName = this.petTypeFormatter.print(petType, Locale.ENGLISH)
        assertEquals("Hamster", petTypeName)
    }

    @Test
    @Throws(ParseException::class)
    fun shouldParse() {
        Mockito.`when`(this.pets.findPetTypes()).thenReturn(makePetTypes())
        val petType = petTypeFormatter.parse("Bird", Locale.ENGLISH)
        assertEquals("Bird", petType.name)
    }

    @Test(expected = ParseException::class)
    @Throws(ParseException::class)
    fun shouldThrowParseException() {
        Mockito.`when`(this.pets.findPetTypes()).thenReturn(makePetTypes())
        petTypeFormatter.parse("Fish", Locale.ENGLISH)
    }

    /**
     * Helper method to produce some sample owner types just for test purpose
     *
     * @return [List] of [PetType]
     */
    private fun makePetTypes(): List<PetType> {
        val petTypes = ArrayList<PetType>()
        petTypes.add(object : PetType() {
            init {
                name = "Dog"
            }
        })
        petTypes.add(object : PetType() {
            init {
                name = "Bird"
            }
        })
        return petTypes
    }

}
