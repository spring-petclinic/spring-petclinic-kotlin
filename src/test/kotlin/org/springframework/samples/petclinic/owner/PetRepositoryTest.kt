package org.springframework.samples.petclinic.owner

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@DataJpaTest
class PetRepositoryTest(private val pets: PetRepository, private val owners: OwnerRepository) {

    @Test
    fun shouldFindPetWithCorrectId() {
        val pet7 = requireNotNull(this.pets.findById(7))
        assertThat(pet7.name).startsWith("Samantha")
        assertThat(pet7.owner?.firstName).isEqualTo("Jean")
    }

    @Test
    fun shouldReturnNullWhenPetNotFound() {
        assertThat(this.pets.findById(9999)).isNull()
    }

    @Test
    fun shouldFindAllPetTypes() {
        val petTypes = this.pets.findPetTypes()

        val petType1 = petTypes.first { it.id == 1 }
        assertThat(petType1.name).isEqualTo("cat")
        val petType4 = petTypes.first { it.id == 4 }
        assertThat(petType4.name).isEqualTo("snake")
    }

    @Test
    @Transactional
    fun shouldInsertPetIntoDatabaseAndGenerateId() {
        var owner6 = requireNotNull(this.owners.findById(6))
        val found = owner6.pets.size

        val pet = Pet()
        pet.name = "bowser"
        val types = this.pets.findPetTypes()
        pet.type = types.first { it.id == 2 }
        pet.birthDate = LocalDate.now()
        owner6.addPet(pet)
        assertThat(owner6.pets.size).isEqualTo(found + 1)

        this.pets.save(pet)
        this.owners.save(owner6)

        owner6 = requireNotNull(this.owners.findById(6))
        assertThat(owner6.pets.size).isEqualTo(found + 1)
        // checks that id has been generated
        assertThat(pet.id).isNotNull()
    }

    @Test
    @Transactional
    fun shouldUpdatePetName() {
        var pet7 = requireNotNull(this.pets.findById(7))
        val oldName = pet7.name

        val newName = oldName + "X"
        pet7.name = newName
        this.pets.save(pet7)

        pet7 = requireNotNull(this.pets.findById(7))
        assertThat(pet7.name).isEqualTo(newName)
    }
}
