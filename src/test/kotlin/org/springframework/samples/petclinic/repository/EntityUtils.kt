package org.springframework.samples.petclinic.repository


import org.springframework.orm.ObjectRetrievalFailureException
import org.springframework.samples.petclinic.model.BaseEntity

/**
 * Utility methods for handling entities. Separate from the BaseEntity class mainly because of dependency on the
 * ORM-associated ObjectRetrievalFailureException.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @see org.springframework.samples.petclinic.model.BaseEntity
 *
 * @since 29.10.2003
 */
object EntityUtils {

    /**
     * Look up the entity of the given class with the given id in the given collection.
     *
     * @param entities    the collection to search
     * @param entityClass the entity class to look up
     * @param entityId    the entity id to look up
     * @return the found entity
     * @throws ObjectRetrievalFailureException if the entity was not found
     */
    @Throws(ObjectRetrievalFailureException::class)
    fun <T : BaseEntity> getById(entities: Collection<T>, entityClass: Class<T>, entityId: Int?): T {
        entities
                .filter { it.id === entityId && entityClass.isInstance(it) }
                .forEach { return it }
        throw ObjectRetrievalFailureException(entityClass, entityId)
    }

}
