package org.springframework.samples.petclinic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.vet.VetRepository

@SpringBootTest
class PetclinicIntegrationTests(private val vets: VetRepository) {

    @Test
    fun testFindAll() {
        assertThat(vets.findAll()).isNotEmpty
        assertThat(vets.findAll()).isNotEmpty // served from cache
    }
}
