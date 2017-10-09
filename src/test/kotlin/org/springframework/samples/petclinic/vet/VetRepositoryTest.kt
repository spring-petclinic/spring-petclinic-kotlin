package org.springframework.samples.petclinic.vet

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.samples.petclinic.repository.EntityUtils
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class VetRepositoryTest {

    @Autowired
    lateinit private var vets: VetRepository

    @Test
    fun shouldFindVets() {
        val vets = this.vets.findAll()

        val vet = vets.first { it.id == 3 }
        assertThat(vet.lastName).isEqualTo("Douglas")
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2)
        assertThat(vet.getSpecialties()[0].name).isEqualTo("dentistry");
        assertThat(vet.getSpecialties()[1].name).isEqualTo("surgery");
    }

}
