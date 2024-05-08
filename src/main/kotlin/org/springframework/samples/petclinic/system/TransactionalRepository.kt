package org.springframework.samples.petclinic.system

import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.samples.petclinic.owner.Pet
import org.springframework.samples.petclinic.owner.PetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionalRepository(val pets: PetRepository, val owners: OwnerRepository) {
    fun saveOwnerAndPet(pet: Pet, owner: Owner) {
        extracted(owner, pet)
    }

    @Transactional
    fun extracted(
        owner: Owner,
        pet: Pet
    ) {
        owner.pets.add(pet)
        pets.save(pet)
        owners.save(owner)
    }
}
