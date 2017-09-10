/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner


import org.hibernate.validator.constraints.NotEmpty
import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PropertyComparator
import org.springframework.samples.petclinic.model.Person
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Digits

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Antoine Rey
 */
@Entity
@Table(name = "owners")
class Owner : Person() {
    @Column(name = "address")
    @NotEmpty
    var address: String = ""

    @Column(name = "city")
    @NotEmpty
    var city: String = ""

    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    var telephone: String = ""

    @OneToMany(cascade = arrayOf(CascadeType.ALL), mappedBy = "owner")
    var pets: MutableSet<Pet> = HashSet()


    fun getPets(): List<Pet> {
        val sortedPets = ArrayList(pets)
        PropertyComparator.sort(sortedPets, MutableSortDefinition("name", true, true))
        return Collections.unmodifiableList(sortedPets)
    }

    fun addPet(pet: Pet) {
        if (pet.isNew) {
            pets.add(pet)
        }
        pet.owner = this
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if owner name is already in use
     */
    fun getPet(name: String): Pet? =
            getPet(name, false)

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if owner name is already in use
     */
    fun getPet(name: String, ignoreNew: Boolean): Pet? {
        val lname = name.toLowerCase()
        for (pet in pets) {
            if (!ignoreNew || !pet.isNew) {
                val compName = pet.name?.toLowerCase()
                if (compName == lname) {
                    return pet
                }
            }
        }
        return null
    }

}
