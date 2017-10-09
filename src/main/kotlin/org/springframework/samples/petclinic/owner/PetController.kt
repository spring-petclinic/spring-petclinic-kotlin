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


import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Antoine Rey
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController(val pets: PetRepository, val owners: OwnerRepository) {

    private val VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm"

    @ModelAttribute("types")
    fun populatePetTypes(): Collection<PetType> = this.pets.findPetTypes()

    @ModelAttribute("owner")
    fun findOwner(@PathVariable("ownerId") ownerId: Int): Owner
            = owners.findById(ownerId)

    @InitBinder("owner")
    fun initOwnerBinder(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
    }

    @InitBinder("pet")
    fun initPetBinder(dataBinder: WebDataBinder) {
        dataBinder.validator = PetValidator()
    }

    @GetMapping(value = "/pets/new")
    fun initCreationForm(owner: Owner, model: ModelMap): String {
        val pet = Pet()
        owner.addPet(pet)
        model.put("pet", pet)
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @PostMapping(value = "/pets/new")
    fun processCreationForm(owner: Owner, @Valid pet: Pet, result: BindingResult, model: ModelMap): String {
        if (StringUtils.hasLength(pet.name) && pet.isNew && owner.getPet(pet.name!!, true) != null) {
            result.rejectValue("name", "duplicate", "already exists")
        }
        owner.addPet(pet)
        return if (result.hasErrors()) {
            model.put("pet", pet)
            VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            this.pets.save(pet)
            "redirect:/owners/{ownerId}"
        }
    }

    @GetMapping(value = "/pets/{petId}/edit")
    fun initUpdateForm(@PathVariable petId: Int, model: ModelMap): String {
        val pet = pets.findById(petId)
        model.put("pet", pet)
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @PostMapping(value = "/pets/{petId}/edit")
    fun processUpdateForm(@Valid pet: Pet, result: BindingResult, owner: Owner, model: ModelMap): String {
        return if (result.hasErrors()) {
            pet.owner = owner
            model.put("pet", pet)
            VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            owner.addPet(pet)
            pets.save(pet)
            "redirect:/owners/{ownerId}"
        }
    }

}
