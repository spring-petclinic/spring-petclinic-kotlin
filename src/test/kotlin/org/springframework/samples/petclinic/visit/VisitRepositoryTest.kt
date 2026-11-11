package org.springframework.samples.petclinic.visit


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.samples.petclinic.owner.PetRepository
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@DataJpaTest
class VisitRepositoryTest(@param:Autowired private val pets: PetRepository,
                          @param:Autowired private val visits: VisitRepository) {

    @Test
    @Throws(Exception::class)
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
