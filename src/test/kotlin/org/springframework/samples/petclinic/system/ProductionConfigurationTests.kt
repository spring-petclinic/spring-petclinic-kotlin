package org.springframework.samples.petclinic.system

import org.junit.Test
import org.junit.runner.RunWith

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.vet.VetRepository
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class ProductionConfigurationTests {

    @Autowired
    lateinit private var vets: VetRepository

    @Test
    fun testFindAll() {
        vets.findAll()
        vets.findAll() // served from cache
    }
}
