package org.springframework.samples.petclinic.owner

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.samples.petclinic.repository.EntityUtils
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.util.*

@RunWith(SpringRunner::class)
@DataJpaTest
class PetRepositoryTest {

    @Autowired
    lateinit private var pets: PetRepository

    @Autowired
    lateinit private var owners: OwnerRepository


    @Test
    fun shouldFindPetWithCorrectId() {
        val pet7 = this.pets.findById(7)
        assertThat(pet7.name).startsWith("Samantha")
        assertThat(pet7.owner!!.firstName).isEqualTo("Jean")

    }

    @Test
    fun shouldFindAllPetTypes() {
        val petTypes = this.pets.findPetTypes()

        val petType1 = EntityUtils.getById(petTypes, PetType::class.java, 1)
        assertThat(petType1.name).isEqualTo("cat")
        val petType4 = EntityUtils.getById(petTypes, PetType::class.java, 4)
        assertThat(petType4.name).isEqualTo("snake")
    }

    @Test
    @Transactional
    fun shouldInsertPetIntoDatabaseAndGenerateId() {
        var owner6 = this.owners.findById(6)
        val found = owner6.getPets().size

        val types = this.pets.findPetTypes()
        val type = EntityUtils.getById(types, PetType::class.java, 2)
        val pet = Pet(birthDate = Date(), type = type)
        pet.name = "bowser"
        owner6.addPet(pet)
        assertThat(owner6.getPets().size).isEqualTo(found + 1)

        this.pets.save(pet)
        this.owners.save(owner6)

        owner6 = this.owners.findById(6)
        assertThat(owner6.getPets().size).isEqualTo(found + 1)
        // checks that id has been generated
        assertThat(pet.id).isNotNull()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun shouldUpdatePetName() {
        var pet7 = this.pets.findById(7)
        val oldName = pet7.name

        val newName = oldName + "X"
        pet7.name = newName
        this.pets.save(pet7)

        pet7 = this.pets.findById(7)
        assertThat(pet7.name).isEqualTo(newName)
    }
}
