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

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Transient
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.samples.petclinic.model.NamedEntity
import org.springframework.samples.petclinic.visit.Visit
import java.time.LocalDate

/**
 * Simple business object representing a owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Antoine Rey
 */
@Entity
@Table(name = "pets")
class Pet : NamedEntity() {

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var birthDate: LocalDate? = null

    @ManyToOne
    @JoinColumn(name = "type_id")
    var type: PetType? = null

    @ManyToOne
    @JoinColumn(name = "owner_id")
    var owner: Owner? = null

    @Transient
    private val _visits: MutableSet<Visit> = LinkedHashSet()

    val visits: List<Visit>
        get() = _visits.sortedBy { it.date }

    fun addVisit(visit: Visit) {
        _visits.add(visit)
        visit.petId = this.id
    }
}
