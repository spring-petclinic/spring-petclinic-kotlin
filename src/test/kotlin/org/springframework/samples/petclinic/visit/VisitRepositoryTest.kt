package org.springframework.samples.petclinic.visit

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.samples.petclinic.owner.PetRepository
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
class VisitRepositoryTest(private val pets: PetRepository, private val visits: VisitRepository) {

    @Test
    fun shouldFindVisitsByPetId() {
        val visits = this.visits.findByPetId(7)
        assertThat(visits.size).isEqualTo(2)
        val visitArr = visits.toTypedArray()
        assertThat(visitArr[0].date).isNotNull()
        assertThat(visitArr[0].petId).isEqualTo(7)
    }

    @Test
    @Transactional
    fun shouldAddNewVisitForPet() {
        var pet7 = requireNotNull(this.pets.findById(7))
        val found = pet7.visits.size
        val visit = Visit()
        pet7.addVisit(visit)
        visit.description = "test"
        this.visits.save(visit)
        this.pets.save(pet7)

        pet7 = requireNotNull(this.pets.findById(7))
        assertThat(pet7.visits.size).isEqualTo(found + 1)
        assertThat(visit.id).isNotNull()
    }
}
