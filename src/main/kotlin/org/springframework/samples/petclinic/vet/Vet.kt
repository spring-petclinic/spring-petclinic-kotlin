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
package org.springframework.samples.petclinic.vet

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.xml.bind.annotation.XmlElement

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 * @author Antoine Rey
 */
@Entity
@Table(name = "vets")
data class Vet(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @get:XmlElement
        val id: Int? = null,

        @Column(name = "first_name")
        @get:NotEmpty
        @get:XmlElement
        val firstName: String = "",

        @Column(name = "last_name")
        @get:NotEmpty
        @get:XmlElement
        val lastName: String = "",

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "vet_specialties", joinColumns = arrayOf(JoinColumn(name = "vet_id")), inverseJoinColumns = arrayOf(JoinColumn(name = "specialty_id")))
        @OrderBy("name ASC")
        @XmlElement
        @get:XmlElement
        val specialties: MutableList<Specialty> = ArrayList()
) : Serializable {

    fun getNrOfSpecialties(): Int =
            specialties.size


    fun addSpecialty(specialty: Specialty) =
            specialties.add(specialty)

}
