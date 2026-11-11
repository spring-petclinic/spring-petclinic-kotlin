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

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

private const val VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm"

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Antoine Rey
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController(private val pets: PetRepository, private val owners: OwnerRepository) {

    @ModelAttribute("types")
    fun populatePetTypes(): Collection<PetType> = this.pets.findPetTypes()

    @ModelAttribute("owner")
    fun findOwner(@PathVariable ownerId: Int): Owner = owners.findById(ownerId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Owner $ownerId not found")

    @InitBinder("owner")
    fun initOwnerBinder(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
    }

    @InitBinder("pet")
    fun initPetBinder(dataBinder: WebDataBinder) {
        dataBinder.validator = PetValidator()
    }

    @GetMapping("/pets/new")
    fun initCreationForm(owner: Owner, model: Model): String {
        val pet = Pet()
        owner.addPet(pet)
        model["pet"] = pet
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/pets/new")
    fun processCreationForm(owner: Owner, @Valid pet: Pet, result: BindingResult, model: Model): String {
        val petName = pet.name
        if (!petName.isNullOrBlank() && pet.isNew && owner.getPet(petName, true) != null) {
            result.rejectValue("name", "duplicate", "already exists")
        }
        owner.addPet(pet)
        return if (result.hasErrors()) {
            model["pet"] = pet
            VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            this.pets.save(pet)
            "redirect:/owners/{ownerId}"
        }
    }

    @GetMapping("/pets/{petId}/edit")
    fun initUpdateForm(@PathVariable petId: Int, model: Model): String {
        val pet = pets.findById(petId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Pet $petId not found")
        model["pet"] = pet
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/pets/{petId}/edit")
    fun processUpdateForm(@Valid pet: Pet, result: BindingResult, owner: Owner, model: Model): String =
        if (result.hasErrors()) {
            pet.owner = owner
            model["pet"] = pet
            VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            owner.addPet(pet)
            pets.save(pet)
            "redirect:/owners/{ownerId}"
        }
}
