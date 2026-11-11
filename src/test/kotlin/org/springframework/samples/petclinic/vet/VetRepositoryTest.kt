package org.springframework.samples.petclinic.vet

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest

@DataJpaTest
class VetRepositoryTest(private val vets: VetRepository) {

    @Test
    fun shouldFindVets() {
        val vets = this.vets.findAll()

        val vet = vets.first { it.id == 3 }
        assertThat(vet.lastName).isEqualTo("Douglas")
        assertThat(vet.nrOfSpecialties).isEqualTo(2)
        assertThat(vet.specialties[0].name).isEqualTo("dentistry")
        assertThat(vet.specialties[1].name).isEqualTo("surgery")
    }
}
