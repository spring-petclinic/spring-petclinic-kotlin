package org.springframework.samples.petclinic.visit


import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.samples.petclinic.owner.PetRepository
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@DataJpaTest
class VisitRepositoryTest {

    @Autowired
    lateinit private var pets: PetRepository

    @Autowired
    lateinit private var visits: VisitRepository

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
        var pet7 = this.pets.findById(7)
        val found = pet7.getVisits().size
        val visit = Visit()
        pet7.addVisit(visit)
        visit.description = "test"
        this.visits.save(visit)
        this.pets.save(pet7)

        pet7 = this.pets.findById(7)
        assertThat(pet7.getVisits().size).isEqualTo(found + 1)
        assertThat(visit.id).isNotNull()
    }
}
